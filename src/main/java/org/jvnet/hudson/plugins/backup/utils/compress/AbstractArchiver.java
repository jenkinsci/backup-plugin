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

public abstract class AbstractArchiver implements Archiver {

	public void addFile(String destinationName, File source) throws ArchiverException {
		try {
			getArchiver().addFile(source, destinationName);
		} catch (org.codehaus.plexus.archiver.ArchiverException e) {
			throw new ArchiverException(e);
		}		
	}

	public void close() throws ArchiverException {
		try {
			getArchiver().createArchive();
		} catch (org.codehaus.plexus.archiver.ArchiverException e) {
			throw new ArchiverException(e);
		} catch (IOException e) {
			throw new ArchiverException(e);
		}
	}

	public abstract void init(File destination) throws ArchiverException;

	public abstract org.codehaus.plexus.archiver.Archiver getArchiver();
	
}
