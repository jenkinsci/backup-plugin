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

package org.jvnet.hudson.plugins.backup.utils.filename;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
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
