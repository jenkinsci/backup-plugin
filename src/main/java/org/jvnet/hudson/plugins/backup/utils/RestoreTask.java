package org.jvnet.hudson.plugins.backup.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.input.AutoCloseInputStream;
import org.jvnet.hudson.plugins.backup.utils.compress.ArchiverException;
import org.jvnet.hudson.plugins.backup.utils.compress.CompressionMethodEnum;
import org.jvnet.hudson.plugins.backup.utils.compress.UnArchiver;

/**
 * This is the restore task, run in background and log to a file
 *
 * @author vsellier
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

        // Not using tools like FileUtils.deleteDirectory
        // because they failed with non existing symbolic links
        delete(directory);

        if (!directory.mkdir()) {
            logger.error("Unable to create " + configurationDirectory + " directory.");
            return;
        }

        File archive = new File(fileName);

        logger.info("Uncompressing archive file...");
        UnArchiver unAchiver = CompressionMethodEnum.ZIP.getUnArchiver();

        try {
			unAchiver.unArchive(archive, configurationDirectory);
		} catch (ArchiverException e) {
			logger.error("Error uncompressiong archive.");
			logger.error("Look to hudson global logs for more informations.");
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
