package org.jvnet.hudson.plugins.backup.utils.filename;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import org.jvnet.hudson.plugins.backup.BackupConfig;
import org.jvnet.hudson.plugins.backup.utils.compress.CompressionMethodEnum;

/**
 * User: vsellier
 * Date: May 4, 2009
 * Time: 1:08:14 AM
 */
public class FileNamePatternEnumTest {
    @Test
    public void testDate() {
        String template = "blahblah_@date@.@blah@";

        assertTrue(FileNamePatternEnum.DATE.isPresentIn(template));
        assertFalse(FileNamePatternEnum.DATE.isPresentIn("blahblah_date.@extention@"));

        String fileName = FileNamePatternEnum.DATE.replace(template, new BackupConfig());
        assertNotNull(fileName);
        assertFalse(FileNamePatternEnum.DATE.isPresentIn(fileName));
    }

    @Test
    public void testExtension() {
        String template = "blahblah_@date@.@extension@";

        assertTrue(FileNamePatternEnum.EXTENSION.isPresentIn(template));
        assertFalse(FileNamePatternEnum.EXTENSION.isPresentIn("blahblah_date.extention"));

        BackupConfig configuration = new BackupConfig();
        configuration.setArchiveType(CompressionMethodEnum.ZIP);
        String fileName = FileNamePatternEnum.EXTENSION.replace(template, configuration);
        assertNotNull(fileName);
        assertFalse(FileNamePatternEnum.EXTENSION.isPresentIn(fileName));
        assertTrue(fileName.contains("." + CompressionMethodEnum.ZIP.getCode()));

        configuration.setArchiveType(CompressionMethodEnum.TARBZ2);
        fileName = FileNamePatternEnum.EXTENSION.replace(template, configuration);
        assertNotNull(fileName);
        assertFalse(FileNamePatternEnum.EXTENSION.isPresentIn(fileName));
        assertTrue(fileName.contains("." + CompressionMethodEnum.TARBZ2.getCode()));

    }
}
