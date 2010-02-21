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

package org.jvnet.hudson.plugins.backup.utils.compress;

/**
 * List the different compression methods supported by backup plugin
 */
public enum CompressionMethodEnum {
    ZIP("zip", ZipArchiver.class, ZipUnArchiver.class),
    TARGZIP("tar.gz", TarGzipArchiver.class, TarGzipUnArchiver.class),
    TARBZ2("tar.bz2", TarBz2Archiver.class, TarBz2UnArchiver.class);

    private final String code;
    private final Class<? extends Archiver> archiverClass;
    private final Class<? extends UnArchiver> unArchiverClass;

    private CompressionMethodEnum(String code, Class<? extends Archiver> archiverClass, Class<? extends UnArchiver> unArchiverClass) {
        this.code = code;
        this.archiverClass = archiverClass;
        this.unArchiverClass = unArchiverClass;
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
