package org.jvnet.hudson.plugins.backup;

/**
 * User: vsellier
 * Date: Apr 20, 2009
 * Time: 11:35:57 PM
 */
public class BackupConfig {
    private final static String DEFAULT_FILE_NAME_TEMPLATE = "backup_yyyyMMdd_HHmm";

    private String targetDirectory;
    private boolean verbose;
    private String fileNameTemplate = DEFAULT_FILE_NAME_TEMPLATE;

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
        return fileNameTemplate;
    }

    public void setFileNameTemplate(String fileNameTemplate) {
        this.fileNameTemplate = fileNameTemplate;
    }
}
