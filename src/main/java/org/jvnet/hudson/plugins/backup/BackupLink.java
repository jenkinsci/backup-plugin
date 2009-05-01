package org.jvnet.hudson.plugins.backup;

import hudson.model.Hudson;
import hudson.model.ManagementLink;
import hudson.util.FormFieldValidator;
import org.apache.commons.lang.StringUtils;
import org.jvnet.hudson.plugins.backup.utils.BackupPersistence;
import org.jvnet.hudson.plugins.backup.utils.BackupPluginTask;
import org.jvnet.hudson.plugins.backup.utils.BackupTask;
import org.jvnet.hudson.plugins.backup.utils.RestoreTask;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import org.kohsuke.stapler.framework.io.LargeText;

import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class BackupLink extends ManagementLink {
    private final static Logger LOGGER = Logger.getLogger(BackupLink.class
            .getName());

    private static BackupLink instance;

    private BackupPluginTask task;
    private boolean fileNameOk = false;

    private BackupLink() {
    }

    public static BackupLink getInstance() {
        if (instance == null) {
            instance = new BackupLink();
        }
        return instance;
    }

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

    public boolean doBackupDirectoryCheck(StaplerRequest req, StaplerResponse rsp) {

        return false;
    }

    public void doSaveSettings(StaplerRequest res, StaplerResponse rsp, @QueryParameter("backupDirectoryPath") String backupPath
            , @QueryParameter("verbose") boolean verbose, @QueryParameter("fileNameTemplate") String fileNameTemplate) throws IOException {
        Hudson.getInstance().checkPermission(Hudson.ADMINISTER);

        BackupConfig configuration = new BackupConfig();

        configuration.setTargetDirectory(backupPath);
        configuration.setVerbose(verbose);
        configuration.setFileNameTemplate(fileNameTemplate);

        BackupPluginImpl.getInstance().setConfiguration(configuration);        

        LOGGER.info("Backup configuration saved.");

        rsp.sendRedirect("/backup");
    }

    

    /**
     * Checks if the backup filename entered is valid or not
     */
    public boolean doBackupFileNameCheck(StaplerRequest req, StaplerResponse rsp)
            throws IOException, ServletException {
        Hudson.getInstance().checkPermission(Hudson.ADMINISTER);
        // this can be used to check the existence of a file on the server, so
        // needs to be protected
        fileNameOk = false;
        new FormFieldValidator(req, rsp, true) {
            public void check() throws IOException, ServletException {
                File f = getFileParameter("backupFileName");
                LOGGER.fine("Filename : " + f.getAbsolutePath());

                fileNameOk = false;
                if (StringUtils.isBlank(f.getName())) {
                    error(Messages.emptyFileName());
                    return;
                }

                if (f.isDirectory()) {
                    error(Messages.fileIsADirectory());
                    return;
                }

                fileNameOk = true;
                if (f.exists()) {
                    warning(Messages.fileAlreadyExists());
                    return;
                }

                ok();
            }
        }.process();
        return fileNameOk;
    }

    /**
     * Checks if the restore file entered is valid or not
     */
    public boolean doRestoreFileNameCheck(StaplerRequest req, StaplerResponse rsp)
            throws IOException, ServletException {
        Hudson.getInstance().checkPermission(Hudson.ADMINISTER);
        // this can be used to check the existence of a file on the server, so
        // needs to be protected
        fileNameOk = true;
        new FormFieldValidator(req, rsp, true) {
            public void check() throws IOException, ServletException {
                File f = getFileParameter("restoreFileName");
                LOGGER.fine("Filename : " + f.getName());

                fileNameOk = false;
                if (StringUtils.isBlank(f.getName())) {
                    error(Messages.emptyFileName());
                    return;
                }

                if (f.isDirectory()) {
                    error(Messages.fileIsADirectory());
                    return;
                }

                if (!f.exists()) {
                    warning(Messages.fileNotExists());
                    return;
                }

                fileNameOk = true;
                ok();
            }
        }.process();

        return fileNameOk;
    }

    public void doDoBackup(StaplerRequest req, StaplerResponse rsp,
                           @QueryParameter("backupFileName") final String fileName, @QueryParameter("verbose") final boolean verbose)
            throws IOException, ServletException {
        Hudson.getInstance().checkPermission(Hudson.ADMINISTER);
        LOGGER.info("Backuping hudson files into " + fileName + "....");

        if (!doBackupFileNameCheck(req, rsp)) {
            rsp.sendRedirect("configurebackup");
            return;
        }

        // configuring backup
        task = new BackupTask();

        task.setVerbose(verbose);
        task.setLogFileName(getBackupLogFile().getAbsolutePath());
        task.setFileName(fileName);
        task.setConfigurationDirectory(getRootDirectory());

        // Launching the task
        Thread thread = Executors.defaultThreadFactory().newThread(task);
        thread.start();

        // redirect to observation page
        rsp.sendRedirect("backup");
    }

    public void doDoRestore(StaplerRequest req, StaplerResponse rsp,
                            @QueryParameter("restoreFileName") final String fileName, @QueryParameter("verbose") final boolean verbose)
            throws IOException, ServletException {
        Hudson.getInstance().checkPermission(Hudson.ADMINISTER);
        LOGGER.info("Restoring hudson files from " + fileName + "....");

        if (!doRestoreFileNameCheck(req, rsp)) {
            rsp.sendRedirect("configurerestore");
            return;
        }

        // Configuring Restore task
        task = new RestoreTask(req.getServletContext());

        task.setFileName(fileName);
        task.setVerbose(verbose);
        task.setLogFileName(getRestoreLogFile().getAbsolutePath());
        task.setConfigurationDirectory(getRootDirectory());

        // Launching the task
        Thread thread = Executors.defaultThreadFactory().newThread(task);
        thread.start();

        // redirect to observation page
        rsp.sendRedirect("restore");
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
        new LargeText(file, task.isFinished()).doProgressText(
                req, rsp);
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
