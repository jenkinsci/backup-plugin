/*
 * The MIT License
 *
 * Copyright (c) 2009-2010, Vincent Sellier, Manufacture Française des Pneumatiques Michelin, Romain Seguy
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

package org.jvnet.hudson.plugins.backup;

import hudson.Extension;
import hudson.model.Hudson;
import hudson.model.ManagementLink;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jvnet.hudson.plugins.backup.utils.BackupPluginTask;
import org.jvnet.hudson.plugins.backup.utils.BackupTask;
import org.jvnet.hudson.plugins.backup.utils.LastModifiedFileComparator;
import org.jvnet.hudson.plugins.backup.utils.RestoreTask;
import org.jvnet.hudson.plugins.backup.utils.compress.CompressionMethodEnum;
import org.jvnet.hudson.plugins.backup.utils.filename.FileNameManager;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import org.kohsuke.stapler.framework.io.LargeText;

@Extension
public class BackupLink extends ManagementLink {
    private final static Logger LOGGER = Logger.getLogger(BackupLink.class
            .getName());

    private BackupPluginTask task;
    private Boolean backupRunning = Boolean.FALSE;
    
    public BackupConfig getConfiguration() {
        return BackupPluginImpl.getInstance().getConfiguration();
    }

    @Override
    public String getIconFileName() {
        return "/plugin/backup/images/backup-48x48.png";
    }

    @Override
    public String getUrlName() {
        return "backup";
    }

    public String getDisplayName() {
        return Messages.display_name();
    }

    @Override
    public String getDescription() {
        return Messages.description();
    }

    public List<String> getExtensions() {
        List<String> extensions = new ArrayList();
        for (CompressionMethodEnum method : CompressionMethodEnum.values()) {
            if(method.isSupportedByPlatform()) { // HUDSON-5305
                extensions.add(method.getCode());
            }
        }
        return extensions;
    }

    public void doLaunchBackup(StaplerRequest req, StaplerResponse rsp) throws IOException {
        Hudson.getInstance().checkPermission(Hudson.ADMINISTER);

        if (task != null) {
                if (!task.isFinished()) {
                        // redirect to observation page
                        rsp.sendRedirect("backup");
                        return;
                }
        }
        
        BackupConfig configuration = getConfiguration();

        String fileNameTemplate = configuration.getTargetDirectory() + File.separator + configuration.getFileNameTemplate();

        String fileName = new FileNameManager().getFileName(fileNameTemplate, configuration);
        LOGGER.info("backup file name = " + fileName + " (generated from template :" + fileNameTemplate + ")");
        // configuring backup
        task = new BackupTask(configuration, getRootDirectory(), fileName, getBackupLogFile().getAbsolutePath());

        // Launching the task
        Thread thread = Executors.defaultThreadFactory().newThread(task);
        thread.start();
        
        // redirect to observation page
        rsp.sendRedirect("backup");
    }

    public void doRestoreFile(StaplerRequest res, StaplerResponse rsp,
                              @QueryParameter("file") String backupFile)
            throws IOException {
        Hudson.getInstance().checkPermission(Hudson.ADMINISTER);

        BackupConfig configuration = getConfiguration();

        String path = configuration.getTargetDirectory();
        String filePath = path + File.separator + backupFile;

        LOGGER.log(Level.INFO, "Selected file : {0}", backupFile);

        task = new RestoreTask(configuration, Hudson.getInstance().getRootDir().getAbsolutePath(),
                filePath, getRestoreLogFile().getAbsolutePath(), res.getServletContext());

        // Launching the restoration
        Thread thread = Executors.defaultThreadFactory().newThread(task);
        thread.start();

        // redirect to observation page
        rsp.sendRedirect("restore");
    }


    /**
     * search into the declared backup directory for backup archives
     */
    public List<File> getFileList() throws IOException {
        LOGGER.log(Level.INFO, "Listing files of {0}", getConfiguration().getTargetDirectory());
        Hudson.getInstance().checkPermission(Hudson.ADMINISTER);

        BackupConfig configuration = getConfiguration();

        File backupDirectory = new File(configuration.getTargetDirectory());
        File[] backupFiles = backupDirectory.listFiles();

        List fileList;
        if (backupFiles == null) {
            fileList = new ArrayList();
        } else {
            fileList = Arrays.asList(backupFiles);
        }

        // Sort file list
        Collections.sort(fileList, new LastModifiedFileComparator());        

        return fileList;
    }

    public void doSaveSettings(StaplerRequest res, StaplerResponse rsp,
                               @QueryParameter("backupDirectoryPath") String backupPath,
                               @QueryParameter("archive_format") String format,
                               @QueryParameter("customExclusionsString") String customExclusionsString,
                               @QueryParameter("verbose") boolean verbose,
                               @QueryParameter("fileNameTemplate") String fileNameTemplate,
                               @QueryParameter("keepWorkspaces") boolean keepWorkspaces,
                               @QueryParameter("keepFingerprints") boolean keepFingerprints,
                               @QueryParameter("keepBuilds") boolean keepBuilds,
                               @QueryParameter("keepArchives") boolean keepArchives,
                               @QueryParameter("jobIncludes") String jobIncludes,
                               @QueryParameter("jobExcludes") String jobExcludes,
                               @QueryParameter("caseSensitive") boolean caseSensitive)
    		throws IOException {
    	LOGGER.info("BackupLink.doSaveSetting");
    	Hudson.getInstance().checkPermission(Hudson.ADMINISTER);

        BackupConfig configuration = new BackupConfig();

        configuration.setTargetDirectory(backupPath);
        configuration.setVerbose(verbose);
        configuration.setFileNameTemplate(fileNameTemplate);

        CompressionMethodEnum archiveType = CompressionMethodEnum.getFromCode(format);
        configuration.setArchiveType(archiveType);
        configuration.setCustomExclusionsString(customExclusionsString);
        configuration.setKeepWorkspaces(keepWorkspaces);
        configuration.setKeepFingerprints(keepFingerprints);
        configuration.setKeepBuilds(keepBuilds);
        configuration.setKeepArchives(keepArchives);

        configuration.setJobIncludes(jobIncludes);
        configuration.setJobExcludes(jobExcludes);
        configuration.setCaseSensitive(caseSensitive);

        BackupPluginImpl.getInstance().setConfiguration(configuration);

        LOGGER.info("Backup configuration saved.");

        rsp.sendRedirect(res.getContextPath() + "/backup");
    }


    public void doProgressiveBackupLog(StaplerRequest req, StaplerResponse rsp)
            throws IOException {
        doProgressiveLog(req, rsp, getBackupLogFile());
    }

    /**
     * Show restore status.
     * When restore is done, reload config from disk via {@link Hudson#doReload(StaplerRequest, StaplerResponse)}
     */
    public void doProgressiveRestoreLog(StaplerRequest req, StaplerResponse rsp)
            throws IOException {
        doProgressiveLog(req, rsp, getRestoreLogFile());
    }

    private void doProgressiveLog(StaplerRequest req, StaplerResponse rsp, File file)
            throws IOException {
        boolean backupFinished = task.isFinished();
    	
    	new LargeText(file, backupFinished).doProgressText(
                req, rsp);
        
    	if (backupFinished) {
    		task = null;
    	}
    }

    public String getRootDirectory() {
        return Hudson.getInstance().getRootDir().getAbsolutePath();
    }

    private File getBackupLogFile() {
        return new File(Hudson.getInstance().getRootDir().getAbsolutePath(),
                "/backup.log");
    }

    private File getRestoreLogFile() {
        return new File(System.getProperty("java.io.tmpdir"), "/hudson_restore.log");
    }

}
