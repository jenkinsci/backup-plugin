package org.jvnet.hudson.plugins.backup.utils;

import java.io.FileFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.math.BigDecimal;

import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.io.filefilter.NotFileFilter;

/**
 * This is the backupu task, run in background and logging to a file
 * 
 * @author vsellier
 * 
 */
public class BackupTask implements Runnable {
	private final static Logger LOGGER = Logger.getLogger(BackupTask.class
			.getName());

	private String logFileName;
	private String targetFileName;
	private String configurationDirectory;

	private List<String> exclusions = new ArrayList<String>();
	
	private PrintWriter logFile;

	private Date startDate;
	private Date endDate;
	
	private boolean finished = false;
	
	public BackupTask() {
		exclusions.addAll(getDefaultsExclusions());
	}
	
	public void run() {
		assert (logFileName != null);
		assert (targetFileName != null);

		startDate = new Date();

		try {
			logFile = new PrintWriter(logFileName);
		} catch (IOException e) {
			LOGGER.severe("Unable to open log file for writing : "
					+ logFileName);
			return;
		}

		logFile.println("Backup started at " + getTimestamp(startDate));

		FileFilter filter = createFileFilter(exclusions);
		
		try {
			ZipBackupEngine backupEngine = new ZipBackupEngine(logFile, configurationDirectory, targetFileName, filter);
			backupEngine.doBackup();
		} catch (IOException e) {
			e.printStackTrace(logFile);
		}
		

		endDate = new Date();
		logFile.println("Backup end at " + getTimestamp(endDate));
        BigDecimal delay = new BigDecimal(
                endDate.getTime() - startDate.getTime());
        delay = delay.setScale(2, BigDecimal.ROUND_HALF_UP);
        delay = delay.divide(new BigDecimal("1000"));

        logFile.println("[" + delay.toPlainString() + "s]");
		logFile.flush();
		logFile.close();
		finished = true;
	}

	public void setLogFileName(String logFileName) {
		this.logFileName = logFileName;
	}

	public void setTargetFileName(String targetFileName) {
		this.targetFileName = targetFileName;
	}

	public void setConfigurationDirectory(String configurationDirectory) {
		this.configurationDirectory = configurationDirectory;
	}
	
	
	public boolean isFinished() {
		return finished;
	}
	
	/**
	 * Gets the formatted current time stamp.
	 */
	private static String getTimestamp(Date date) {
		return String.format("[%1$tD %1$tT]", date);
	}
	
	public List<String> getDefaultsExclusions() {
		List<String> defaultExclusion = new ArrayList<String>();
		
		defaultExclusion.add("workspace");
		
		return defaultExclusion;
	}
	
	private FileFilter createFileFilter(List<String> exclusions) {		
		// creating the filter
		IOFileFilter filter = new NameFileFilter(exclusions.toArray(new String[] {})); 
		// revert it because they are exclusions
		filter = new NotFileFilter(filter);
		
		return filter;
	}
}
