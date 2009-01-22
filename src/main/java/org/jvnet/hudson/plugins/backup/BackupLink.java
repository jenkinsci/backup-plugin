package org.jvnet.hudson.plugins.backup;

import hudson.model.Hudson;
import hudson.model.ManagementLink;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import org.jvnet.hudson.plugins.backup.utils.BackupTask;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;                   
import org.kohsuke.stapler.framework.io.LargeText;

public class BackupLink extends ManagementLink {
	private final static Logger LOGGER = Logger.getLogger(BackupLink.class
			.getName());

	private final BackupTask backupTask;

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

	public void doDoBackup(StaplerRequest req, StaplerResponse rsp,
			@QueryParameter("fileName") final String fileName)
			throws IOException {
		LOGGER.info("Backuping hudson files into " + fileName + "....");

		// configuring backup configuring
		backupTask.setLogFileName(getLogFile().getAbsolutePath());
		backupTask.setTargetFileName(fileName);
		backupTask.setConfigurationDirectory(getRootDirectory());

		// Launching the task
		Thread thread = Executors.defaultThreadFactory().newThread(backupTask);
		thread.start();

		// redirect to observation page
		rsp.sendRedirect("backup");
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
