package org.jvnet.hudson.plugins.backup.utils.compress;

import java.io.File;
import java.io.IOException;

public class ZipArchiver implements Archiver {

	org.codehaus.plexus.archiver.zip.ZipArchiver archiver;
	
	public void addFile(String destinationName, File source) throws ArchiverException {
		try {
			archiver.addFile(source, destinationName);
		} catch (org.codehaus.plexus.archiver.ArchiverException e) {
			throw new ArchiverException(e);
		}		
	}

	public void close() throws ArchiverException {
		try {
			archiver.createArchive();
		} catch (org.codehaus.plexus.archiver.ArchiverException e) {
			throw new ArchiverException(e);
		} catch (IOException e) {
			throw new ArchiverException(e);
		}
	}

	public void init(File destination) throws ArchiverException {
		archiver = new org.codehaus.plexus.archiver.zip.ZipArchiver();
		archiver.setDestFile(destination);		
	}
}
