package org.jvnet.hudson.plugins.backup;

import hudson.model.Hudson;
import hudson.model.ManagementLink;

import java.io.IOException;
import java.util.logging.Logger;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

public class BackupLink extends ManagementLink {
	private final static Logger LOGGER = Logger.getLogger(BackupLink.class
			.getName());

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

	public void doDoBackup(StaplerRequest req, StaplerResponse rsp) throws IOException {
		LOGGER.info("Backuping config files....");
		rsp.sendRedirect("backup");
	}
	
	public String getRootDirectory() {
		return Hudson.getInstance().getRootDir().getAbsolutePath();
	}
	
}
