package org.jvnet.hudson.plugins.backup;

/**
 * User: vsellier
 * Date: Apr 20, 2009
 * Time: 11:35:57 PM
 */
public class BackupConfig {
    private String targetDirectory;
    private boolean verbose;

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
    
}
