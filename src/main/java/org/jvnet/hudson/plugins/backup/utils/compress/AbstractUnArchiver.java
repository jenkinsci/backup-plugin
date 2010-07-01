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

package org.jvnet.hudson.plugins.backup.utils.compress;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
			LOGGER.severe(e.getMessage());
		}
	}
}
