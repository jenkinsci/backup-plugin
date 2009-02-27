package org.jvnet.hudson.plugins.backup.utils.compress;

/**
 * Common exception for archivers  
 * 
 * @author vsellier
 *
 */
public class ArchiverException extends Exception {

	private static final long serialVersionUID = -6357239656937805184L;

	public ArchiverException() {
	}

	public ArchiverException(String message) {
		super(message);
	}

	public ArchiverException(Throwable cause) {
		super(cause);
	}

	public ArchiverException(String message, Throwable cause) {
		super(message, cause);
	}

}
