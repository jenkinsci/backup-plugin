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

import org.apache.commons.io.FileUtils;

public class ArchiverTestUtil {

	public static void addTestFiles(Archiver archiver) throws ArchiverException {
		archiver.addFile("file1", new File(Thread.currentThread()
				.getContextClassLoader().getResource("data/file1").getFile()));
		archiver.addFile("dir1/file1", new File(Thread.currentThread()
				.getContextClassLoader().getResource("data/dir1/file1")
				.getFile()));
		archiver.addFile("dir1/file2", new File(Thread.currentThread()
				.getContextClassLoader().getResource("data/dir1/file2")
				.getFile()));
        archiver.addFile("dir$dollar/file$dollar.txt", new File(Thread.currentThread()
                .getContextClassLoader().getResource("data/dir$dollar/file$dollar.txt")
                .getFile()));

		archiver.close();
	}
	
	/**
	 * Compare the content of 2 dufferents directory
	 * @param directory1
	 * @param directory2
	 * @return
	 * @throws IOException
	 */
	public static boolean compareDirectoryContent(File directory1, File directory2)
			throws IOException {
		if (!directory1.isDirectory() || !directory2.isDirectory()) {
			return false;
		}

		File list1[] = directory1.listFiles();
		File list2[] = directory2.listFiles();

		if (list1.length != list2.length) {
			return false;
		}

		for (int i = 0; i < list1.length; i++) {
			File file1 = list1[i];
			File file2 = list2[i];

			if (!file1.getName().equals(file2.getName())) {
				return false;
			}

			if (file1.isDirectory()) {
				return compareDirectoryContent(file1, file2);
			} else {
				return FileUtils.contentEquals(file1, file2);
			}
		}
		return true;
	}

}
