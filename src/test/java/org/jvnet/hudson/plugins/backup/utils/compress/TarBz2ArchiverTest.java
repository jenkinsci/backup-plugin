package org.jvnet.hudson.plugins.backup.utils.compress;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.codehaus.plexus.archiver.tar.TarBZip2UnArchiver;
import org.codehaus.plexus.logging.console.ConsoleLogger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class TarBz2ArchiverTest {
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
		archiveFile = new File(OUTPUT_DIRECTORY, "test.tar.gz");

		targetDirectory = new File(UNCOMPRESS_DIRECTORY);
		targetDirectory.mkdir();
	}

	@Test
	public void testArchiver() throws Exception {
		TarBz2Archiver archiver = new TarBz2Archiver();
		archiver.init(archiveFile);

		ArchiverTestUtil.addTestFiles(archiver);
		
		TarBZip2UnArchiver unarchiver = new TarBZip2UnArchiver();
		unarchiver.setSourceFile(archiveFile);

		unarchiver.setDestDirectory(targetDirectory);
		unarchiver.enableLogging(new ConsoleLogger(ConsoleLogger.LEVEL_DEBUG,
				"Logger"));
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
