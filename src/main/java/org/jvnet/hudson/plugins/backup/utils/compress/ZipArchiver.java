package org.jvnet.hudson.plugins.backup.utils.compress;

import java.io.File;

public class ZipArchiver extends AbstractArchiver implements Archiver {

	org.codehaus.plexus.archiver.zip.ZipArchiver archiver;
	
	@Override
	public void init(File destination) throws ArchiverException {
		archiver = new org.codehaus.plexus.archiver.zip.ZipArchiver();
		archiver.setDestFile(destination);		
	}

	@Override
	public org.codehaus.plexus.archiver.Archiver getArchiver() {
		return archiver;
	}
	
	
}
