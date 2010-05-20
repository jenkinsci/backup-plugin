/*
 * The MIT License
 *
 * Copyright (c) 2009-2010, Vincent Sellier, Manufacture Française des Pneumatiques Michelin, Romain Seguy
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

package org.jvnet.hudson.plugins.backup.utils.compress;

import hudson.model.Hudson;

/**
 * List the different compression methods supported by backup plugin
 */
public enum CompressionMethodEnum {
    ZIP("zip", ZipArchiver.class, ZipUnArchiver.class, true, true),
    TARGZIP("tar.gz", TarGzipArchiver.class, TarGzipUnArchiver.class, false, true),
    TARBZ2("tar.bz2", TarBz2Archiver.class, TarBz2UnArchiver.class, false, true);

    private final String code;
    private final Class<? extends Archiver> archiverClass;
    private final Class<? extends UnArchiver> unArchiverClass;
    // HUDSON-5305
    private final boolean supportedByWindows;
    private final boolean supportedByUnix;

    private CompressionMethodEnum(String code, Class<? extends Archiver> archiverClass, Class<? extends UnArchiver> unArchiverClass, boolean supportedByWindows, boolean supportedByUnix) {
        this.code = code;
        this.archiverClass = archiverClass;
        this.unArchiverClass = unArchiverClass;
        this.supportedByWindows = supportedByWindows;
        this.supportedByUnix = supportedByUnix;
    }

    public String getCode() {
        return code;
    }

    public Archiver getArchiver() {
        try {
            return archiverClass.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("Unable to instanciate compression engine for " + getCode() + " method.", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to instanciate compression engine for " + getCode() + " method.", e);
        }
    }

    /**
     * Returns {@code true} is the compression method is supported by the platform
     * currently running Hudson (that is, Windows or Unix).
     *
     * <p>Fixes HUDSON-5305.</p>
     */
    public boolean isSupportedByPlatform() {
        if(Hudson.isWindows()) {
            return supportedByWindows;
        }
        else {
            return supportedByUnix;
        }
    }
    
    public static Archiver getArchiver(CompressionMethodEnum method) {
    	return method.getArchiver();
    }
    
    public UnArchiver getUnArchiver() {
        try {
            return unArchiverClass.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("Unable to instanciate compression engine for " + getCode() + " method.", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to instanciate compression engine for " + getCode() + " method.", e);
        }
    }
    
    public static UnArchiver getUnArchiver(CompressionMethodEnum method) {
    	return method.getUnArchiver();
    }

    public static CompressionMethodEnum getFromCode(String code) {
        for (CompressionMethodEnum value : CompressionMethodEnum.values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        throw new IllegalArgumentException("Unknown code " + code);
    }

}
