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
public class RestoreTask implements Runnable {
	private final static Logger LOGGER = Logger.getLogger(RestoreTask.class
			.getName());

	private boolean verbose;
	private String logFileName;
	private String sourceFileName;
	private String configurationDirectory;

	private List<String> exclusions = new ArrayList<String>();

	private BackupLogger logger;

	private Date startDate;
	private Date endDate;

	private boolean finished = false;

	public RestoreTask() {
		exclusions.addAll(getDefaultsExclusions());
	}

	public void run() {
		assert (logFileName != null);
		assert (sourceFileName != null);

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
		
		try {
			FileUtils.deleteDirectory(directory);
		} catch (IOException e) {
			logger.error("Unable to delete old configuration directory");
			e.printStackTrace(logger.getWriter());
			logger.error("Hudson could be in a inconsistent state.");
			logger.error("Try to uncompress manually " + sourceFileName + " into " + configurationDirectory + " directory.");
			return;
		}
		
		if ( ! directory.mkdir()) {
			logger.error("Unable to create " + configurationDirectory + " directory.");
			return;
		}
		
		File archive = new File(sourceFileName);
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
			}
		} catch (IOException e) {
			logger.error("Error uncompressing zip file.");
			return;
		}
		
//		FileFilter filter = createFileFilter(exclusions);
//
//		try {
//			ZipBackupEngine backupEngine = new ZipBackupEngine(logger,
//					configurationDirectory, targetFileName, filter);
//			backupEngine.doBackup();
//		} catch (IOException e) {
//			e.printStackTrace(logger.getWriter());
//		}
//
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

	public void setLogFileName(String logFileName) {
		this.logFileName = logFileName;
	}

	public void setSourceFileName(String sourceFileName) {
		this.sourceFileName = sourceFileName;
	}

	public void setConfigurationDirectory(String configurationDirectory) {
		this.configurationDirectory = configurationDirectory;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	public List<String> getDefaultsExclusions() {
		List<String> defaultExclusion = new ArrayList<String>();

		defaultExclusion.add("workspace");

		return defaultExclusion;
	}

	/**
	 * Gets the formatted current time stamp.
	 */
	private static String getTimestamp(Date date) {
		return String.format("[%1$tD %1$tT]", date);
	}
}
