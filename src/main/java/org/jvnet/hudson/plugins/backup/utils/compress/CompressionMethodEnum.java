package org.jvnet.hudson.plugins.backup.utils.compress;


/**
 * List the different compression methods supported by backup plugin
 */
public enum CompressionMethodEnum {
    ZIP("ZIP", ZipArchiver.class, ZipUnArchiver.class),
    TARGZIP("tgz", TarGzipArchiver.class, TarGzipUnArchiver.class),
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
    
}
