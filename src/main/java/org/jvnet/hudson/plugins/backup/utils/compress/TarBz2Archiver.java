package org.jvnet.hudson.plugins.backup.utils.compress;

import java.io.File;

import org.codehaus.plexus.archiver.tar.TarArchiver;
import org.codehaus.plexus.archiver.tar.TarArchiver.TarCompressionMethod;

public class TarBz2Archiver extends AbstractArchiver implements Archiver {

	TarArchiver archiver;

	@Override
	public void init(File destination) throws ArchiverException {
		archiver = new TarArchiver();
		archiver.setDestFile(destination);

		TarCompressionMethod compression = new TarCompressionMethod();
		try {
			compression.setValue("bzip2");
		} catch (org.codehaus.plexus.archiver.ArchiverException e) {
			throw new RuntimeException("Unknown compression mode bzip2");
		}

		archiver.setCompression(compression);
	}

	@Override
	public org.codehaus.plexus.archiver.Archiver getArchiver() {
		return archiver;
	}

}
