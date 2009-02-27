package org.jvnet.hudson.plugins.backup.utils;

import java.io.FileFilter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.io.filefilter.NotFileFilter;
import org.jvnet.hudson.plugins.backup.BackupException;

/**
 * This is the backup task, run in background and log to a file
 * 
 * @author vsellier
 * 
 */
public class BackupTask extends BackupPluginTask {

    private List<String> exclusions = new ArrayList<String>();

    public BackupTask() {
		exclusions.addAll(getDefaultsExclusions());
	}

	public void run() {
		assert (logFileName != null);
		assert (fileName != null);

		startDate = new Date();
		try {
			logger = new BackupLogger(logFileName, verbose);
		} catch (IOException e) {
			LOGGER.severe("Unable to open log file for writing : "
					+ logFileName);
			return;
		}

		logger.info("Backup started at " + getTimestamp(startDate));

		FileFilter filter = createFileFilter(exclusions);

		try {
			BackupEngine backupEngine = new BackupEngine(logger,
					configurationDirectory, fileName, filter);
			backupEngine.doBackup();
		} catch (BackupException e) {
			e.printStackTrace(logger.getWriter());
		}

		endDate = new Date();
		logger.info("Backup end at " + getTimestamp(endDate));
		BigDecimal delay = new BigDecimal(endDate.getTime()
				- startDate.getTime());
		delay = delay.setScale(2, BigDecimal.ROUND_HALF_UP);
		delay = delay.divide(new BigDecimal("1000"));

		logger.info("[" + delay.toPlainString() + "s]");
		finished = true;
		logger.close();
	}

    public List<String> getDefaultsExclusions() {
		List<String> defaultExclusion = new ArrayList<String>();

		defaultExclusion.add("workspace");

		return defaultExclusion;
	}

    private FileFilter createFileFilter(List<String> exclusions) {
		// creating the filter
		IOFileFilter filter = new NameFileFilter(exclusions
				.toArray(new String[] {}));
		// revert it because they are exclusions
		filter = new NotFileFilter(filter);

		return filter;
	}
}
