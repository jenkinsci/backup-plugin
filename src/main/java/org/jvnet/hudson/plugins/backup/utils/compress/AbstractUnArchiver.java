package org.jvnet.hudson.plugins.backup.utils.compress;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;

import org.apache.commons.io.FileUtils;
import org.codehaus.plexus.logging.console.ConsoleLogger;

public abstract class AbstractUnArchiver implements UnArchiver {
	protected final static Logger LOGGER = Logger.getLogger(AbstractUnArchiver.class
			.getName());

	public abstract org.codehaus.plexus.archiver.AbstractUnArchiver getUnArchiver();
	
	public void unArchive(File archive, String toDir) throws ArchiverException {
		org.codehaus.plexus.archiver.AbstractUnArchiver unarchiver = getUnArchiver();
		
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
		
		unarchiver.enableLogging(new ConsoleLogger(org.codehaus.plexus.logging.Logger.LEVEL_INFO, "UnArchiver"));
		unarchiver.setSourceFile(archive);
		unarchiver.setDestDirectory(destDir);
	
		try {
			unarchiver.extract();
		} catch (org.codehaus.plexus.archiver.ArchiverException e) {
			String message = "Unable to extract " + archive.getAbsolutePath() + " content to " +
			toDir;
			LOGGER.log(Level.SEVERE, message, e);
			throw new ArchiverException(message, e);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe(e.getMessage());
		}
	}
}
