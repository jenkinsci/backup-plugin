package org.jvnet.hudson.plugins.backup.utils.compress;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class ZipUnArchiverTest {
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
	public void testUnArchiver() throws Exception {
		// TODO use java zip API
		ZipArchiver archiver = new ZipArchiver();
		archiver.init(archiveFile);

		archiver.addFile("file1", new File(Thread.currentThread()
				.getContextClassLoader().getResource("data/file1").getFile()));
		archiver.addFile("dir1/file1", new File(Thread.currentThread()
				.getContextClassLoader().getResource("data/dir1/file1")
				.getFile()));
		archiver.addFile("dir1/file2", new File(Thread.currentThread()
				.getContextClassLoader().getResource("data/dir1/file2")
				.getFile()));

		archiver.close();

		ZipUnArchiver unarchiver = new ZipUnArchiver();
		
		unarchiver.unArchive(archiveFile, targetDirectory.getAbsolutePath());

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
