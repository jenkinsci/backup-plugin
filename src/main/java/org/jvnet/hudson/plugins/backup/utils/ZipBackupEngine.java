package org.jvnet.hudson.plugins.backup.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.DirectoryWalker;

public class ZipBackupEngine extends DirectoryWalker {
	private PrintWriter logFile;
	private ZipOutputStream output;
	private File source;
	
	public ZipBackupEngine(PrintWriter logFile, String sourceDirectory, String targetName, FileFilter filter) throws IOException {
		super(filter, -1);
		this.logFile = logFile;
		this.source = new File(sourceDirectory);
		
	}

	@Override
	protected boolean handleDirectory(File directory, int depth,
			Collection results) throws IOException {
		logFile.println(directory.getAbsolutePath() + " directory");
		return super.handleDirectory(directory, depth, results);
	}

	@Override
	protected void handleFile(File file, int depth, Collection results)
			throws IOException {
		logFile.println(file.getAbsolutePath() + " file");

		super.handleFile(file, depth, results);
	}

	public void doBackup() throws IOException {
		this.walk(source, new ArrayList<Object>());
	}

	
}
