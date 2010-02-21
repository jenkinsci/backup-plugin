/*
 * The MIT License
 *
 * Copyright (c) 2009-2010, Vincent Sellier
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.jvnet.hudson.plugins.backup.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.jvnet.hudson.plugins.backup.BackupException;
import org.jvnet.hudson.plugins.backup.utils.compress.Archiver;
import org.jvnet.hudson.plugins.backup.utils.compress.ArchiverException;

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
			String targetName, Archiver archiver, IOFileFilter filter) throws BackupException {
		super(filter, filter, -1);
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
