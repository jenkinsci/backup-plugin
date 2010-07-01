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

	private String targetDirectory;
	private boolean verbose;
	private String fileNameTemplate = DEFAULT_FILE_NAME_TEMPLATE;
	private CompressionMethodEnum archiveType = CompressionMethodEnum.TARGZIP;
	private boolean keepWorkspaces;
	private boolean keepFingerprints;
	private boolean keepBuilds;
	private boolean keepArchives;

        private String jobIncludes;
        private String jobExcludes;
        private boolean caseSensitive;
	
	/**
	 * files or directory names not included into the backup
	 */
	private List<String> customExclusions = new ArrayList<String>();

	public List<String> getCustomExclusions() {
		return customExclusions;
	}

	public void setCustomExclusions(List<String> exclusions) {
		this.customExclusions.clear();
		this.customExclusions.addAll(exclusions);
	}

	public void addExclusion(String exclusion) {
		this.customExclusions.add(exclusion);
	}
	
	public String getCustomExclusionsString() {
		return StringUtils.join(customExclusions, ',');
	}
	
	public void setCustomExclusionsString(String customExclusionsString) {
		String[] exclusionsArray = StringUtils.split(customExclusionsString, ", ");
		List<String> exclusionsList = Arrays.asList(exclusionsArray);
		setCustomExclusions(exclusionsList);
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
	
	public void setKeepWorkspaces(boolean keeping) {
		this.keepWorkspaces = keeping;
	}

	public boolean getKeepWorkspaces() {
		return keepWorkspaces;
	}

	public void setKeepFingerprints(boolean keepFingerprints) {
		this.keepFingerprints = keepFingerprints;
	}

	public boolean getKeepFingerprints() {
		return keepFingerprints;
	}

	public void setKeepBuilds(boolean keepBuilds) {
		this.keepBuilds = keepBuilds;
	}

	public boolean getKeepBuilds() {
		return keepBuilds;
	}

	public void setKeepArchives(boolean keepArchives) {
		this.keepArchives = keepArchives;
	}

	public boolean getKeepArchives() {
		return keepArchives;
	}

        public String getJobIncludes() {
                return jobIncludes;
        }

        public void setJobIncludes(String jobIncludes) {
                this.jobIncludes = jobIncludes;
        }

        public String getJobExcludes() {
                return jobExcludes;
        }

        public void setJobExcludes(String jobExcludes) {
                this.jobExcludes = jobExcludes;
        }

        public boolean getCaseSensitive() {
                return caseSensitive;
        }

        public void setCaseSensitive(boolean caseSensitive) {
                this.caseSensitive = caseSensitive;
        }

}
