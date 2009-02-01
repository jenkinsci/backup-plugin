package org.jvnet.hudson.plugins.backup.utils;

import java.io.IOException;
import java.io.PrintWriter;

public class BackupLogger {
	private PrintWriter logFile;
	private boolean debug;

	public BackupLogger(String logFileName, boolean debug) throws IOException {
		logFile = new PrintWriter(logFileName);
		this.debug = debug;
	}
	
	public void debug(String message) {
		if (debug) {
			log("[DEBUG]", message);
		}
	}
	
	public void warn(String message) {
		log("[ WARN]", message);
		logFile.flush();
	}

	public void info(String message) {
		log("[ INFO]", message);
		logFile.flush();
	}
	
	public void error(String message) {
		log("[ERROR]", message);
		logFile.flush();
	}
	
	public PrintWriter getWriter() {
		return logFile;
	}

	public void close() {
		logFile.flush();
		logFile.close();
	}
	
	private void log(String level, String message) {
		logFile.println(level + " " + message);
	}
	
}
