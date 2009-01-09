package org.jvnet.hudson.plugins.backup.utils;

import hudson.slaves.CommandLauncher;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.logging.Logger;

/**
 * This is the backupu task, run in background and logging to a file
 * 
 * @author vsellier
 *
 */
public class BackupTask implements Runnable {
	private final static Logger LOGGER = Logger.getLogger(BackupTask.class.getName());
		
	private String logFileName;
	private String targetFileName;
	
	// TODO add exclusions
	// TODO add inclusions
	
	private PrintWriter logFile;

	private Date startDate;
	private Date endDate;
	
	public void run() {
		assert(logFileName != null);
		assert(targetFileName != null);
	
		startDate = new Date();
		
		try {
			logFile = new PrintWriter(logFileName);
		} catch(IOException e) {
			LOGGER.severe("Unable to open log file for writing : " + logFileName);
			return;
		}
		
		logFile.println("Backup started at " + getTimestamp(startDate));
		
		
		endDate = new Date();
		logFile.println("Backup end at " + getTimestamp(endDate));
		logFile.flush();
		logFile.close();
	}

	public void setLogFileName(String logFileName) {
		this.logFileName = logFileName;
	}

	public void setTargetFileName(String targetFileName) {
		this.targetFileName = targetFileName;
	}
	
    /**
     * Gets the formatted current time stamp.
     */
    private static String getTimestamp(Date date) {
        return String.format("[%1$tD %1$tT]", date);
    }
}
