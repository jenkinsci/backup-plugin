package org.jvnet.hudson.plugins.backup.utils.compress;


public class TarGzipUnArchiver extends AbstractUnArchiver implements UnArchiver {
	@Override
	public org.codehaus.plexus.archiver.AbstractUnArchiver getUnArchiver() {
		org.codehaus.plexus.archiver.tar.TarGZipUnArchiver unArchiver =
			new org.codehaus.plexus.archiver.tar.TarGZipUnArchiver();
		return unArchiver;
	}

}
