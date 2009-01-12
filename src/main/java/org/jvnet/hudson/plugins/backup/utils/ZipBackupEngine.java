package org.jvnet.hudson.plugins.backup.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.DirectoryWalker;

public class ZipBackupEngine extends DirectoryWalker {
	private PrintWriter logFile;
	private File source;
	
	/**
	 * the lengt of the source string, to speed up walk
	 */
	private int sourceLength;
	private ZipOutputStream target;

	public ZipBackupEngine(PrintWriter logFile, String sourceDirectory,
			String targetName, FileFilter filter) throws IOException {
		super(filter, -1);
		this.logFile = logFile;
		this.source = new File(sourceDirectory);
		this.sourceLength = sourceDirectory.length();

		if (!targetName.endsWith(".zip")) {
			targetName = targetName + ".zip";
		}
		
		File targetFile = new File(targetName);
		logFile.println("Full backup file name : " + targetFile.getAbsolutePath());
		
		target = new ZipOutputStream(new FileOutputStream(targetFile));
		target.setLevel(9);
		
	}

	@Override
	protected boolean handleDirectory(File directory, int depth,
			Collection results) throws IOException {
		logFile.println(getInArchiveName(directory.getAbsolutePath()) + " directory");
		return super.handleDirectory(directory, depth, results);
	}

	@Override
	protected void handleFile(File file, int depth, Collection results)
			throws IOException {
		logFile.println(getInArchiveName(file.getAbsolutePath()) + " file");

		super.handleFile(file, depth, results);
	}

	public void doBackup() throws IOException {
		this.walk(source, new ArrayList<Object>());
	}
	
	/**
	 * Suppress the path to hudson working dir on the beginning of the path.
	 * +1 because of the ending /
	 * 
	 * @param absoluteName the name including dat path
	 * @return the archive name
	 */
	private String getInArchiveName(String absoluteName) {
		return absoluteName.substring(sourceLength + 1);
	}

}
