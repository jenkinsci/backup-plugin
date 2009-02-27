package org.jvnet.hudson.plugins.backup;

/**
 * Common exception for backup plugin
 * 
 * @author vsellier
 */
public class BackupException extends Exception {

	private static final long serialVersionUID = 8930285419536357470L;

	public BackupException() {
	}

	public BackupException(String message) {
		super(message);
	}

	public BackupException(Throwable cause) {
		super(cause);
	}

	public BackupException(String message, Throwable cause) {
		super(message, cause);
	}

}
