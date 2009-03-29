package org.jvnet.hudson.plugins.backup.utils.compress;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.codehaus.plexus.archiver.zip.ZipUnArchiver;
import org.codehaus.plexus.logging.console.ConsoleLogger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

public class ZipArchiverTest {
	private final static String OUTPUT_DIRECTORY = "target";
	private final static String UNCOMPRESS_DIRECTORY = OUTPUT_DIRECTORY
			+ "/uncompress";

	/**
	 * The archive result of the test, deleted at the end
	 */
	File archiveFile;
	/**
	 * Place to unarchive the test, deleted at the end
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
		unarchiver.setSourceFile(archiveFile);

		unarchiver.setDestDirectory(targetDirectory);
		unarchiver.enableLogging(new ConsoleLogger(ConsoleLogger.LEVEL_DEBUG,
				"Logger"));
		unarchiver.extract();

		Assert.assertTrue(compareDirectoryContent(new File(Thread
				.currentThread().getContextClassLoader().getResource("data")
				.getFile()), targetDirectory));

	}

	@After
	public void tearDown() throws Exception {
		archiveFile.delete();
		FileUtils.deleteDirectory(targetDirectory);
	}

	private boolean compareDirectoryContent(File directory1, File directory2)
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
