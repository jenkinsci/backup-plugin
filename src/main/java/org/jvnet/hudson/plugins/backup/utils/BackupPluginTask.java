package org.jvnet.hudson.plugins.backup.utils;

import org.jvnet.hudson.plugins.backup.BackupConfig;

import java.util.Date;
import java.util.logging.Logger;

/**
 * abstract class for backup or restore tasks
 */
public abstract class BackupPluginTask implements Runnable {
    protected final static Logger LOGGER = Logger.getLogger(BackupTask.class
            .getName());

    protected String logFilePath;
    protected String hudsonWorkDir;
    protected String backupFileName;
    protected BackupLogger logger;
    protected Date startDate;
    protected Date endDate;
    protected boolean finished = false;
    protected BackupConfig configuration;

    protected BackupPluginTask(BackupConfig configuration, String hudsonWorkDir, String backupFileName, String logFilePath) {
        this.configuration = configuration;
        setHudsonWorkDir(hudsonWorkDir);
        setLogFilePath(logFilePath);
        setBackupFileName(backupFileName);
    }

    public abstract void run();

    private void setLogFilePath(String logFilePath) {
        this.logFilePath = logFilePath;
    }

    private void setHudsonWorkDir(String hudsonWorkDir) {
        this.hudsonWorkDir = hudsonWorkDir;
    }

    private void setBackupFileName(String backupFileName) {
        this.backupFileName = backupFileName;
    }

    public boolean isFinished() {
        return finished;
    }

    /**
     * Gets the formatted current time stamp.
     */
    protected static String getTimestamp(Date date) {
        return String.format("[%1$tD %1$tT]", date);
    }
}
