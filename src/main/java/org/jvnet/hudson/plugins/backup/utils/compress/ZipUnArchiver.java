package org.jvnet.hudson.plugins.backup.utils.compress;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.jvnet.hudson.plugins.backup.utils.BackupTask;

public class ZipUnArchiver implements UnArchiver {
    protected final static Logger LOGGER = Logger.getLogger(BackupTask.class
            .getName());

	public void unArchive(File archive, String toDir) throws ArchiverException {
		org.codehaus.plexus.archiver.zip.ZipUnArchiver unarchiver =
			new org.codehaus.plexus.archiver.zip.ZipUnArchiver(archive);
		File destDir = new File(toDir);
		
		if (!destDir.exists()) {
			try {
				FileUtils.forceMkdir(destDir);
			} catch (IOException e) {
				String message = "Unable to created directory " + destDir.getAbsolutePath(); 
				LOGGER.severe(message);
				throw new ArchiverException(message, e);
					
			}
		}
		
		unarchiver.setDestDirectory(destDir);
	}

}
