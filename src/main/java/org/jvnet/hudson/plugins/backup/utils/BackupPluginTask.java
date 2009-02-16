package org.jvnet.hudson.plugins.backup.utils;

import java.util.Date;
import java.util.logging.Logger;

/**
 * abstract class for backup or restore tasks
 */
public abstract class BackupPluginTask implements Runnable {
    protected final static Logger LOGGER = Logger.getLogger(BackupTask.class
            .getName());

    protected boolean verbose;
    protected String logFileName;
    protected String fileName;
    protected String configurationDirectory;
    protected BackupLogger logger;
    protected Date startDate;
    protected Date endDate;
    protected boolean finished = false;

    public abstract void run();

    public void setLogFileName(String logFileName) {
        this.logFileName = logFileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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

    /**
	 * Gets the formatted current time stamp.
     */
    protected static String getTimestamp(Date date) {
        return String.format("[%1$tD %1$tT]", date);
    }
}
