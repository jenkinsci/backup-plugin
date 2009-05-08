package org.jvnet.hudson.plugins.backup.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.zip.ZipEntry;

import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.AutoCloseInputStream;
import org.jvnet.hudson.plugins.backup.BackupException;
import org.jvnet.hudson.plugins.backup.utils.compress.Archiver;
import org.jvnet.hudson.plugins.backup.utils.compress.ArchiverException;
import org.jvnet.hudson.plugins.backup.utils.compress.CompressionMethodEnum;

public class BackupEngine extends DirectoryWalker {
	private BackupLogger logger;
	private File source;
	private int nbFiles = 0;
	private int nbErrors = 0;

	/**
	 * the length of the source string, to speed up walk
	 */
	private int sourceLength;
	// private ZipOutputStream target;
	private Archiver archiver;

	public BackupEngine(BackupLogger logger, String sourceDirectory,
			String targetName, Archiver archiver, FileFilter filter) throws BackupException {
		super(filter, -1);
		this.logger = logger;
		this.source = new File(sourceDirectory);
		this.sourceLength = sourceDirectory.length();

		File targetFile = new File(targetName);
		logger.info("Full backup file name : " + targetFile.getAbsolutePath());
		
		this.archiver = archiver;
		
		try {
			archiver.init(targetFile);
		} catch (ArchiverException e) {
			throw new BackupException(e);
		}
		
	}

	@Override
	protected void handleFile(File file, int depth, Collection results)
			throws IOException {

		if (!file.exists()) {
			logger.warn("inconsistent file " + file.getAbsolutePath());
			nbErrors++;
			super.handleFile(file, depth, results);
			return;
		}

		String name = getInArchiveName(file.getAbsolutePath());

		logger.debug(name + " file");

		try {
			archiver.addFile(name, file);
		} catch(ArchiverException e) {
			logger.error(e.getMessage());
			throw new IOException("Unable to add " + name);
		}

		nbFiles++;

		super.handleFile(file, depth, results);
	}

	@Override
	protected void handleEnd(Collection results) throws IOException {
		try {
			archiver.close();
		} catch(ArchiverException e) {
			logger.error(e.getMessage());
			throw new IOException(e.getMessage());
		}
		logger.info("Saved files : " + nbFiles);
		logger.info("Number of errors : " + nbErrors);
	}

	public void doBackup() throws BackupException {
		
		try {
			this.walk(source, new ArrayList<Object>());
		} catch (IOException e) {
			throw new BackupException(e);
		}
	}

	/**
	 * Suppress the path to hudson working dir on the beginning of the path. +1
	 * because of the ending /
	 * 
	 * @param absoluteName
	 *            the name including dat path
	 * @return the archive name
	 */
	private String getInArchiveName(String absoluteName) {
		return absoluteName.substring(sourceLength + 1);
	}

}
