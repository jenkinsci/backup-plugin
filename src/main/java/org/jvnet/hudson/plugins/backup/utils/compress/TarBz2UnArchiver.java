package org.jvnet.hudson.plugins.backup.utils.compress;


public class TarBz2UnArchiver extends AbstractUnArchiver implements UnArchiver {
	@Override
	public org.codehaus.plexus.archiver.AbstractUnArchiver getUnArchiver() {
		org.codehaus.plexus.archiver.tar.TarBZip2UnArchiver unArchiver =
			new org.codehaus.plexus.archiver.tar.TarBZip2UnArchiver();
		return unArchiver;
	}

}
