/*
 * The MIT License
 *
 * Copyright (c) 2009-2010, Vincent Sellier
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.jvnet.hudson.plugins.backup.utils;

import java.util.Date;
import java.util.logging.Logger;

import org.jvnet.hudson.plugins.backup.BackupConfig;

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
