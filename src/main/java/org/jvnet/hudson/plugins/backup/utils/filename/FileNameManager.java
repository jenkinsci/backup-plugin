package org.jvnet.hudson.plugins.backup.utils.filename;

import org.jvnet.hudson.plugins.backup.BackupConfig;

/**
 * Convert a file name template to a real file name.
 * <p/>
 * User: vsellier
 * Date: May 3, 2009
 * Time: 12:02:03 PM
 */
public class FileNameManager {

    public String getFileName(final String template, BackupConfig configuration) {
        String fileName = template;
        for (FileNamePatternEnum pattern : FileNamePatternEnum.values()) {
            if (pattern.isPresentIn(fileName)) {
                fileName = pattern.replace(fileName, configuration);
            }
        }
        return fileName;
    }

}
