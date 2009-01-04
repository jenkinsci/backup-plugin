package org.jvnet.hudson.plugins.backup;

import hudson.model.ManagementLink;

import java.util.logging.Logger;

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

}
