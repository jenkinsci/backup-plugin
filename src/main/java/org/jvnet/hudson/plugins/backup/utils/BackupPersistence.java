package org.jvnet.hudson.plugins.backup.utils;

import hudson.XmlFile;
import hudson.model.Hudson;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.logging.Logger;

import org.jvnet.hudson.plugins.backup.BackupConfig;

public class BackupPersistence {
    protected final static Logger LOGGER = Logger.getLogger(BackupPersistence.class
            .getName());

    private final static String CONFIG_FILE = "backup.xml";

    public BackupPersistence() {
    
    }

    public void saveConfig(BackupConfig config) throws IOException {
        XmlFile xmlFile = getConfigFile();
        xmlFile.write(config);
    }

    public BackupConfig loadConfig() throws IOException {
        XmlFile xmlFile = getConfigFile();
        BackupConfig config;
        try {
            config = (BackupConfig) xmlFile.read();
        } catch(FileNotFoundException e) {
            LOGGER.info("Config file not found.");
            config = new BackupConfig();
        }
        return config;
    }

    private XmlFile getConfigFile() {
        XmlFile xmlFile = new XmlFile(new File(Hudson.getInstance().getRootDir(), CONFIG_FILE));

        return xmlFile;
    }

}