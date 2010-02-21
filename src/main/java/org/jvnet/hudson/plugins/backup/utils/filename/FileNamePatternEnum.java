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

import java.text.SimpleDateFormat;
import java.util.Date;

import org.jvnet.hudson.plugins.backup.BackupConfig;

/**
 * User: vsellier
 * Date: May 3, 2009
 * Time: 6:56:31 PM
 */
public enum FileNamePatternEnum {
    DATE("date") {
        // TODO put this into {@link BackupConfig}
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmm");

        public String replace(String template, BackupConfig configuration) {
            String date = formatter.format(new Date());
            return template.replace(getFullPattern(pattern), date);
        }},
    EXTENSION("extension") {
        public String replace(String template, BackupConfig configuration) {
            return template.replace(getFullPattern(pattern), configuration.getArchiveType().getCode());

        }};

    private final static String SEPARATOR = "@";

    String pattern;

    private FileNamePatternEnum(String pattern) {
        this.pattern = pattern;
    }

    public boolean isPresentIn(String template) {
        return template.contains(SEPARATOR + pattern + SEPARATOR);
    }

    abstract public String replace(String template, BackupConfig configuration);

    /**
     * Return the pattern enclosed with the separator
     */
    private static String getFullPattern(String pattern) {
        return SEPARATOR + pattern + SEPARATOR;
    }
};

