package org.jvnet.hudson.plugins.backup.utils.compress;


public class ZipUnArchiver extends AbstractUnArchiver implements UnArchiver {
	@Override
	public org.codehaus.plexus.archiver.AbstractUnArchiver getUnArchiver() {
		org.codehaus.plexus.archiver.zip.ZipUnArchiver unArchiver =
			new org.codehaus.plexus.archiver.zip.ZipUnArchiver();
		return unArchiver;
	}

}
