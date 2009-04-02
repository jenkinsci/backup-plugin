package org.jvnet.hudson.plugins.backup.utils.compress;

import java.util.logging.Logger;

import org.jvnet.hudson.plugins.backup.utils.BackupTask;

public class ZipUnArchiver extends AbstractUnArchiver implements UnArchiver {
	@Override
	public org.codehaus.plexus.archiver.AbstractUnArchiver getUnArchiver() {
		org.codehaus.plexus.archiver.zip.ZipUnArchiver unArchiver =
			new org.codehaus.plexus.archiver.zip.ZipUnArchiver();
		return unArchiver;
	}

}
