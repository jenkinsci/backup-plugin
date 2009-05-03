package org.jvnet.hudson.plugins.backup.utils.filename;

import org.jvnet.hudson.plugins.backup.BackupConfig;

import java.text.SimpleDateFormat;
import java.util.Date;

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
            return template.replace(getFullPattern(pattern), "." + configuration.getArchiveType().getCode());

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

