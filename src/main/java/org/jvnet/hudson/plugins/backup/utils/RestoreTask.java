package org.jvnet.hudson.plugins.backup.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.input.AutoCloseInputStream;

/**
 * This is the restore task, run in background and log to a file
 * 
 * @author vsellier
 * 
 */
public class RestoreTask extends BackupPluginTask {
	private final static Logger LOGGER = Logger.getLogger(RestoreTask.class
			.getName());
    private static final int BUFFER_LENGTH = 1024;

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

		logger.info("Removing old configuration files");
		File directory = new File(configurationDirectory);
		
//		try {
            // Not using tools like FileUtils.deleteDirectory
            // because they failed with non existing symbolic links
			delete(directory);
//		} catch (IOException e) {
//			logger.error("Unable to delete old configuration directory");
//			e.printStackTrace(logger.getWriter());
//			logger.error("Hudson could be in a inconsistent state.");
//			logger.error("Try to uncompress manually " + fileName + " into " + configurationDirectory + " directory.");
//			return;
//		}
		
		if ( ! directory.mkdir()) {
			logger.error("Unable to create " + configurationDirectory + " directory.");
			return;
		}
		
		File archive = new File(fileName);
		ZipInputStream input;
		try {
			input = new ZipInputStream(new AutoCloseInputStream(new FileInputStream(archive))); 
		} catch (IOException e) {
			logger.error("Unable to open archive.");
			return;
		}

		ZipEntry entry;

		try {
			while ((entry = input.getNextEntry()) != null) {
				logger.debug("Decompression de " + entry.getName());
                long entrySize = entry.getSize();

                String destination = entry.getName();

                File absoluteDestination = new File(directory, destination);
                File absoluteDirectory = absoluteDestination.getParentFile();
                // create the directory
                FileUtils.forceMkdir(absoluteDirectory);

                // uncompress the file
                int count;
                byte[] data = new byte[BUFFER_LENGTH];
                
                

                
			}
		} catch (IOException e) {
			logger.error("Error uncompressing zip file.");
			return;
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
            for (int i = 0; i<files.length; i++) {
                delete(files[i]);
            }
        }
        if (verbose) {
            logger.debug("Deleting " + file.getAbsolutePath());
        }
        file.delete();
    }


}
