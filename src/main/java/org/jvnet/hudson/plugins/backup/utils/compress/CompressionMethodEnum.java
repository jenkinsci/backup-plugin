package org.jvnet.hudson.plugins.backup.utils.compress;


/**
 * List the different compression methods supported by backup plugin
 */
public enum CompressionMethodEnum {
    ZIP("ZIP", ZipArchiver.class, Object.class);

    private final String code;
    private final Class<? extends Archiver> archiverClass;
    private final Class unarchiverClass;

    private CompressionMethodEnum(String code, Class<? extends Archiver> archiverClass, Class<?> unarchiverClass) {
        this.code = code;
        this.archiverClass = archiverClass;
        this.unarchiverClass = unarchiverClass;
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
}
