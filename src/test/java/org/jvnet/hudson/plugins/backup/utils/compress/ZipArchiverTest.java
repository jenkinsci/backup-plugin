package org.jvnet.hudson.plugins.backup.utils.compress;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ZipArchiverTest {
	File destFile;

	@Before
	public void tearUp() throws Exception {
		String tmpDir = System.getProperty("java.io.tmpdir");
		destFile = new File(tmpDir, "testzip.zip");
	}

	@Test
	public void testArchiver() throws Exception {
		ZipArchiver archiver = new ZipArchiver();
		archiver.init(destFile);

		archiver.addFile("file1", new File(Thread.currentThread()
				.getContextClassLoader().getResource("data/file1").getFile()));
		archiver.addFile("dir1/file1", new File(Thread.currentThread()
				.getContextClassLoader().getResource("data/dir1/file1").getFile()));
		archiver.addFile("dir1/file2", new File(Thread.currentThread()
				.getContextClassLoader().getResource("data/dir1/file2").getFile()));
		
		archiver.close();
		
		// TODO add content of the archive tests 
	}

	@After
	public void tearDown() throws Exception {
//		destFile.delete();
	}

}
