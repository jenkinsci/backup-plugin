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

import org.apache.commons.io.FileUtils;
import org.codehaus.plexus.archiver.zip.ZipUnArchiver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ZipArchiverTest {
	private final static String OUTPUT_DIRECTORY = "target";
	private final static String UNCOMPRESS_DIRECTORY = OUTPUT_DIRECTORY
			+ "/uncompress";

	/**
	 * The archive result of the test, deleted at the end if every thing is ok
	 */
	File archiveFile;
	/**
	 * Place to unarchive the test, deleted at the end if every thing is ok
	 */
	File targetDirectory;

	@Before
	public void tearUp() throws Exception {
		archiveFile = new File(OUTPUT_DIRECTORY, "testzip.zip");

		targetDirectory = new File(UNCOMPRESS_DIRECTORY);
		targetDirectory.mkdir();
	}

	@Test
	public void testArchiver() throws Exception {
		ZipArchiver archiver = new ZipArchiver();
		archiver.init(archiveFile);

		ArchiverTestUtil.addTestFiles(archiver);
		
		ZipUnArchiver unarchiver = new ZipUnArchiver();
		unarchiver.setSourceFile(archiveFile);

		unarchiver.setDestDirectory(targetDirectory);
		unarchiver.extract();

		Assert.assertTrue(ArchiverTestUtil.compareDirectoryContent(new File(Thread
				.currentThread().getContextClassLoader().getResource("data")
				.getFile()), targetDirectory));

	}

	@After
	public void tearDown() throws Exception {
		archiveFile.delete();
		FileUtils.deleteDirectory(targetDirectory);
	}

}
