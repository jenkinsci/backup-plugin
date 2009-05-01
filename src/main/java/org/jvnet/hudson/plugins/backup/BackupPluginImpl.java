package org.jvnet.hudson.plugins.backup;

import hudson.Plugin;
import hudson.model.ManagementLink;

import java.util.logging.Logger;
import java.io.IOException;

import org.jvnet.hudson.plugins.backup.utils.BackupPersistence;

/**
 * Entry point of the Backup plugin
 *
 * @author vsellier
 */
public class BackupPluginImpl extends Plugin {
    private final static Logger LOGGER = Logger.getLogger(BackupLink.class
            .getName());

    private BackupConfig configuration;

    private static BackupPluginImpl instance;

    public BackupPluginImpl() {
        instance = this;
    }

    public static BackupPluginImpl getInstance() {
        return instance;
    }

    public void start() {
        ManagementLink.LIST.add(BackupLink.getInstance());

        try {
            configuration = loadConfiguration();
        } catch (IOException e) {
            LOGGER.severe("Error loading configuration file : " + e.getMessage());
        }
    }

    public BackupConfig getConfiguration() {
        return configuration;
    }

    public void setConfiguration(BackupConfig configuration) {
        this.configuration = configuration;
        try {
            saveConfiguration(configuration);
        } catch(IOException e) {
            LOGGER.severe("Error saving configuration file : " + e.getMessage());
        }
    }

    private BackupConfig loadConfiguration() throws IOException {
        LOGGER.info("Loading configuration...");
        BackupPersistence persistence = new BackupPersistence();

        BackupConfig config = persistence.loadConfig();

        return config;
    }

    private void saveConfiguration(BackupConfig configuration) throws IOException {
        LOGGER.info("Saving configuration...");
        BackupPersistence persistence = new BackupPersistence();
        persistence.saveConfig(configuration);
    }
}
