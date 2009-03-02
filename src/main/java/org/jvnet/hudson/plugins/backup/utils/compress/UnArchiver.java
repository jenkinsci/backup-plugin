package org.jvnet.hudson.plugins.backup.utils.compress;

import java.io.File;


public interface UnArchiver {
	/**
	 * Uncompress the content of archive into the toDir directory	
	 * 
	 * @param archive
	 * @param toDir
	 */
	public void unArchive(File archive, String toDir) throws ArchiverException;

}
