package org.jvnet.hudson.plugins.backup.utils.compress;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class TarGzipUnArchiverTest {
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
		TarGzipArchiver archiver = new TarGzipArchiver();
		archiver.init(archiveFile);

		ArchiverTestUtil.addTestFiles(archiver);
		
		TarGzipUnArchiver unarchiver = new TarGzipUnArchiver();
		
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
