package org.jvnet.hudson.plugins.backup;

import hudson.model.Hudson;
import hudson.model.ManagementLink;
import hudson.util.FormFieldValidator;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import javax.servlet.ServletException;

import org.apache.commons.lang.StringUtils;
import org.jvnet.hudson.plugins.backup.utils.BackupTask;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import org.kohsuke.stapler.framework.io.LargeText;

public class BackupLink extends ManagementLink {
	private final static Logger LOGGER = Logger.getLogger(BackupLink.class
			.getName());

	private final BackupTask backupTask;

	boolean fileNameOk = false;

	public BackupLink() {
		// Creating backup task
		backupTask = new BackupTask();
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

	/**
	 * Checks if the backup filename entered is valid or not
	 */
	public boolean doBackupFileNameCheck(StaplerRequest req, StaplerResponse rsp)
			throws IOException, ServletException {
		Hudson.getInstance().checkPermission(Hudson.ADMINISTER);
		// this can be used to check the existence of a file on the server, so
		// needs to be protected
		new FormFieldValidator(req, rsp, true) {
			public void check() throws IOException, ServletException {
				File f = getFileParameter("backupFileName");
				LOGGER.info("Filename : " + f.getAbsolutePath());

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
		new FormFieldValidator(req, rsp, true) {
			public void check() throws IOException, ServletException {
				File f = getFileParameter("restoreFileName");
				LOGGER.info("Filename : " + f.getName());

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
				if (! f.exists()) {
					warning(Messages.fileNotExists());
					return;
				}

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

		// configuring backup configuring
		backupTask.setVerbose(verbose);
		backupTask.setLogFileName(getLogFile().getAbsolutePath());
		backupTask.setTargetFileName(fileName);
		backupTask.setConfigurationDirectory(getRootDirectory());

		// Launching the task
		Thread thread = Executors.defaultThreadFactory().newThread(backupTask);
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

		throw new RuntimeException("Not implemented");
	}
	
	
	public void doProgressiveLog(StaplerRequest req, StaplerResponse rsp)
			throws IOException {
		new LargeText(getLogFile(), backupTask.isFinished()).doProgressText(
				req, rsp);
	}

	public String getRootDirectory() {
		return Hudson.getInstance().getRootDir().getAbsolutePath();
	}

	private File getLogFile() {
		return new File(Hudson.getInstance().getRootDir().getAbsolutePath(),
				"/backup.log");
	}
}
