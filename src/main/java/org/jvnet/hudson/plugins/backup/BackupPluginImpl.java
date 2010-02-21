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

package org.jvnet.hudson.plugins.backup;

import hudson.Plugin;

import java.io.IOException;
import java.util.logging.Logger;

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

    @Override
    public void start() {
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
            LOGGER.severe("Error saving configuration file: " + e.getMessage());
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
