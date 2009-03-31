package org.jvnet.hudson.plugins.backup.utils.compress;

import java.io.File;
import java.io.IOException;

public abstract class AbstractArchiver implements Archiver {

	public void addFile(String destinationName, File source) throws ArchiverException {
		try {
			getArchiver().addFile(source, destinationName);
		} catch (org.codehaus.plexus.archiver.ArchiverException e) {
			throw new ArchiverException(e);
		}		
	}

	public void close() throws ArchiverException {
		try {
			getArchiver().createArchive();
		} catch (org.codehaus.plexus.archiver.ArchiverException e) {
			throw new ArchiverException(e);
		} catch (IOException e) {
			throw new ArchiverException(e);
		}
	}

	public abstract void init(File destination) throws ArchiverException;

	public abstract org.codehaus.plexus.archiver.Archiver getArchiver();
	
}
