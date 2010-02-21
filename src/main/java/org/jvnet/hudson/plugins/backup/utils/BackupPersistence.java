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

package org.jvnet.hudson.plugins.backup.utils;

import hudson.XmlFile;
import hudson.model.Hudson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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