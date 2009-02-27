package org.jvnet.hudson.plugins.backup.utils.compress;

import static org.junit.Assert.*;

import org.junit.Test;

public class CompressionMethodEnumTest {

	@Test
	public void testGetZipBackupEngine() {
		assertEquals(ZipArchiver.class, CompressionMethodEnum.ZIP.getArchiver().getClass());
		assertEquals(ZipArchiver.class, CompressionMethodEnum.getArchiver(CompressionMethodEnum.ZIP).getClass());
		
	}

}
