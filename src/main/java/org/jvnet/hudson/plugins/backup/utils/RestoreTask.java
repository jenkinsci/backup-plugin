package org.jvnet.hudson.plugins.backup.utils;

import hudson.model.Hudson;
import hudson.util.HudsonIsLoading;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.logging.Logger;

import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.jvnet.hudson.plugins.backup.utils.compress.CompressionMethodEnum;
import org.jvnet.hudson.plugins.backup.utils.compress.UnArchiver;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

/**
 * This is the restore task, run in background and log to a file
 *
 * @author vsellier
 */
public class RestoreTask extends BackupPluginTask {
    private final static Logger LOGGER = Logger.getLogger(RestoreTask.class
            .getName());

    /**
     * This context will be passed to the StaplerRequest mock
     * at the end of the restore. It is used be {@link Hudson} to put
     * the {@link HudsonIsLoading} object. 
     */
    private ServletContext servletContext;
    
    public RestoreTask(ServletContext servletContext) {
    	this.servletContext = servletContext;
    }
    
    public void run() {
        assert (logFileName != null);
        assert (fileName != null);

        startDate = new Date();
        try {
            logger = new BackupLogger(logFileName, verbose);
        } catch (IOException e) {
            LOGGER.severe("Unable to open log file for writing : "
                    + logFileName);
            return;
        }

        logger.info("Restore started at " + getTimestamp(startDate));

        File directory = new File(configurationDirectory);
        
        String tempDirectoryPath = configurationDirectory + "_restore";
        logger.info("Working into " + tempDirectoryPath + " directory");
        
        File temporary_directory = new File(tempDirectoryPath);

        if (temporary_directory.exists()) {
        	logger.info("A old restore working dir exists, cleaning ...");
        	try {
				FileUtils.deleteDirectory(temporary_directory);
			} catch (IOException e) {
				logger.error("Unable to delete " + tempDirectoryPath);
				e.printStackTrace(logger.getWriter());
				return;
			}
        }
        temporary_directory.mkdir();
        
        File archive = new File(fileName);

        logger.info("Uncompressing archive file...");
        UnArchiver unAchiver = CompressionMethodEnum.ZIP.getUnArchiver();

        try {
			unAchiver.unArchive(archive, tempDirectoryPath);
		} catch (Exception e) {
			logger.error("Error uncompressiong archive.");
			logger.error("Look to hudson global logs for more informations.");
		}
        
        // Not using tools like FileUtils.deleteDirectory
        // because it is failing with non existing symbolic links
        logger.info("Removing old configuration files...");
        delete(directory);

        logger.info("Making temporary directory the hudson home...");
        temporary_directory.renameTo(directory); 
		
		logger.info("*****************************************");
		logger.info("Reloading hudson configuration from disk.");
		logger.info("*****************************************");

		StaplerRequest request = FakeObject.getStaplerRequestFake(servletContext);
		StaplerResponse response = FakeObject.getStaplerResponseFake();
		
		try {
			Hudson.getInstance().doReload(request, response);
		} catch (IOException e) {
			logger.error("Error reloading config files from disk.");
			logger.error("Call this method manually");
			e.printStackTrace(logger.getWriter());
		}
		
        endDate = new Date();
        logger.info("Backup end at " + getTimestamp(endDate));
        BigDecimal delay = new BigDecimal(endDate.getTime()
                - startDate.getTime());
        delay = delay.setScale(2, BigDecimal.ROUND_HALF_UP);
        delay = delay.divide(new BigDecimal("1000"));

        logger.info("[" + delay.toPlainString() + "s]");
        finished = true;
        logger.close();
    }

    private void delete(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                delete(files[i]);
            }
        }
        if (verbose) {
            logger.debug("Deleting " + file.getAbsolutePath());
        }
        file.delete();
    }


}
