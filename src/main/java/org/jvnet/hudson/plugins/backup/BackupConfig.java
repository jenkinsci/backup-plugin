package org.jvnet.hudson.plugins.backup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jvnet.hudson.plugins.backup.utils.compress.CompressionMethodEnum;

/**
 * User: vsellier Date: Apr 20, 2009 Time: 11:35:57 PM
 */
public class BackupConfig {
	private final static String DEFAULT_FILE_NAME_TEMPLATE = "backup_@date@.@extension@";
	private final static CompressionMethodEnum DEFAULT_COMPRESSION_METHOD = CompressionMethodEnum.ZIP;
	// TODO remove workspace from exclusions when configuration will be possible
	private final static String[] DEFAULT_EXCLUSIONS = { "workspace",
			"backup.log" };

	private String targetDirectory;
	private boolean verbose;
	private String fileNameTemplate = DEFAULT_FILE_NAME_TEMPLATE;
	private CompressionMethodEnum archiveType = CompressionMethodEnum.TARGZIP;
	/**
	 * files or directory names not included into the backup
	 */
	private List<String> exclusions = new ArrayList<String>();

	public List<String> getExclusions() {
		return exclusions;
	}

	public void setExclusions(List<String> exclusions) {
		List<String> defaultExclusions = Arrays.asList(DEFAULT_EXCLUSIONS);
		defaultExclusions.removeAll(exclusions);
		
		this.exclusions.clear();
		this.exclusions.addAll(exclusions);
		this.exclusions.addAll(defaultExclusions);	
    }

	public void addExclusion(String exclusion) {
		this.exclusions.add(exclusion);
	}

	public boolean isVerbose() {
		return verbose;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	public String getTargetDirectory() {

		return targetDirectory;
	}

	public void setTargetDirectory(String targetDirectory) {
		this.targetDirectory = targetDirectory;
	}

	public String getFileNameTemplate() {
		if (StringUtils.isNotEmpty(fileNameTemplate)) {
			return fileNameTemplate;
		} else {
			return DEFAULT_FILE_NAME_TEMPLATE;
		}
	}

	public void setFileNameTemplate(String fileNameTemplate) {
		this.fileNameTemplate = fileNameTemplate;
	}

	public CompressionMethodEnum getArchiveType() {
		if (archiveType != null) {
			return archiveType;
		} else {
			return DEFAULT_COMPRESSION_METHOD;
		}
	}

	public void setArchiveType(CompressionMethodEnum archiveType) {
		this.archiveType = archiveType;
	}
}
