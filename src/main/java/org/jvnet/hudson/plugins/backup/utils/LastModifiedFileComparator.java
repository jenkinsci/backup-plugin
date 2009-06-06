package org.jvnet.hudson.plugins.backup.utils;

import java.util.Comparator;
import java.io.File;

/**
 * Compare two {@see java.io.File} by their modification date.
 * The most recent is prior.
 * 
 */
public class LastModifiedFileComparator implements Comparator {
    public int compare(Object o1, Object o2) {
        if (! (o1 instanceof File && o2 instanceof File)) {
            throw new IllegalArgumentException("Parameters not File types");
        }
        if (o1 == null || o2 == null) {
            throw new IllegalArgumentException("One parameter is null");
        }
        return (int) (((File) o2).lastModified() - ((File) o1).lastModified());
    }
}
