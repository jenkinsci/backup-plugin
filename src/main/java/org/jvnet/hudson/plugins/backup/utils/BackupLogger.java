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

import java.io.IOException;
import java.io.PrintWriter;

/**
 * This class is used to change the verbosity of the backup plugin. 
 * <br>
 * This apply to the backup log only, not on the hudson console logger
 * 
 * @author vsellier
 *
 */
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
