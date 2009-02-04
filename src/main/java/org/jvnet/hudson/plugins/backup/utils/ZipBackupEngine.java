package org.jvnet.hudson.plugins.backup.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.AutoCloseInputStream;

public class ZipBackupEngine extends DirectoryWalker {
	private BackupLogger logger;
	private File source;
	private int nbFiles = 0;
	private int nbErrors = 0;

	/**
	 * the length of the source string, to speed up walk
	 */
	private int sourceLength;
	private ZipOutputStream target;

	public ZipBackupEngine(BackupLogger logger, String sourceDirectory,
			String targetName, FileFilter filter) throws IOException {
		super(filter, -1);
		this.logger = logger;
		this.source = new File(sourceDirectory);
		this.sourceLength = sourceDirectory.length();

		File targetFile = new File(targetName);
		logger.info("Full backup file name : " + targetFile.getAbsolutePath());

		target = new ZipOutputStream(new FileOutputStream(targetFile));
		target.setLevel(9);

	}

	@Override
	protected void handleFile(File file, int depth, Collection results)
			throws IOException {

		if (!file.exists()) {
			logger.warn("inconsistent file " + file.getAbsolutePath());
			nbErrors ++;
			super.handleFile(file, depth, results);
			return;
		}

		String name = getInArchiveName(file.getAbsolutePath());

		logger.debug(name + " file");

		ZipEntry entry = new ZipEntry(name);
		entry.setTime(file.lastModified());
		target.putNextEntry(entry);

		InputStream stream = new AutoCloseInputStream(new FileInputStream(file));
		IOUtils.copy(stream, target);

		nbFiles++;
		
		super.handleFile(file, depth, results);
	}

	@Override
	protected void handleEnd(Collection results) throws IOException {
		target.close();
		logger.info("Saved files : " + nbFiles);
		logger.info("Number of errors : " + nbErrors);
	}

	public void doBackup() throws IOException {
		this.walk(source, new ArrayList<Object>());
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
