package org.jvnet.hudson.plugins.backup;

import org.apache.commons.lang.StringUtils;
import org.jvnet.hudson.plugins.backup.utils.compress.CompressionMethodEnum;

/**
 * User: vsellier
 * Date: Apr 20, 2009
 * Time: 11:35:57 PM
 */
public class BackupConfig {
    private final static String DEFAULT_FILE_NAME_TEMPLATE = "backup_yyyyMMdd_HHmm";
    private final static CompressionMethodEnum DEFAULT_COMPRESSION_METHOD = CompressionMethodEnum.ZIP;

    private String targetDirectory;
    private boolean verbose;
    private String fileNameTemplate = DEFAULT_FILE_NAME_TEMPLATE;
    private CompressionMethodEnum archiveType = CompressionMethodEnum.ZIP;

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public String getTargetDirectory() {

        return targetDirectory;
    }

    public void setTargetDirectory(String targetDirectory) {
        this.targetDirectory = targetDirectory;
    }

    public String getFileNameTemplate() {
        if (StringUtils.isNotEmpty(fileNameTemplate)) {
            return fileNameTemplate;
        } else {
            return DEFAULT_FILE_NAME_TEMPLATE;
        }
    }

    public void setFileNameTemplate(String fileNameTemplate) {
        this.fileNameTemplate = fileNameTemplate;
    }

    public CompressionMethodEnum getArchiveType() {
        if (archiveType != null) {
            return archiveType;
        } else {
            return DEFAULT_COMPRESSION_METHOD;
        }
    }

    public void setArchiveType(CompressionMethodEnum archiveType) {
        this.archiveType = archiveType;
    }
}
