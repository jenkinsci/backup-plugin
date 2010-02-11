/*
 * The MIT License
 *
 * Copyright (c) 2010, Manufacture Française des Pneumatiques Michelin, Romain Seguy
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

package org.jvnet.hudson.plugins.backup.utils.filename;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Very simple {@link FileFilter} which only accepts files/directories specified
 * as parameters of the constructor.
 * 
 * @author Romain Seguy
 */
public class SimpleFileFilter implements FileFilter {

    private List<File> directories = new ArrayList<File>();
    private List<File> files = new ArrayList<File>();

    /**
     * @param basedir the base directory which contains the specified {@code directories} or {@code files}
     * @param directories a set of sub-directories of {@code basedir} to be accepted by this filter
     * @param files a set of files contained in {@code basedir} to be accepted by this filter
     */
    public SimpleFileFilter(File basedir, String[] directories, String[] files) {
        if(directories != null) {
            for(int i = 0; i < directories.length; i++) {
                this.directories.add(new File(basedir, directories[i]));
            }
        }

        if(files != null) {
            for(int i = 0; i < files.length; i++) {
                this.files.add(new File(basedir, files[i]));
            }
        }
    }

    public boolean accept(File pathname) {
        if(directories.contains(pathname)) {
            return true;
        }
        if(files.contains(pathname)) {
            return true;
        }
        return false;
    }

}
