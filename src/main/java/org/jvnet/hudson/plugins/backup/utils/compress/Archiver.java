package org.jvnet.hudson.plugins.backup.utils.compress;

import java.io.File;
import java.io.IOException;


/**
 * The contract for the different archivers.
 * 
 * @author vsellier
 */
public interface Archiver {
	/**
	 * Init the archiver.
	 * @param destination the final file
	 */
	public void init(File destination) throws ArchiverException;
	/**
	 * close all the archiver stuff
	 */
	public void close() throws ArchiverException;
	/**
	 * Add a file into the archive
	 * 
	 * @param destinationName the name in the archive
	 * @param source the file containing the data to add in the archive
	 */
	public void addFile(String destinationName, File source) throws ArchiverException;
}
