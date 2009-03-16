package org.jvnet.hudson.plugins.backup.utils;

import hudson.model.Computer;
import hudson.model.Hudson;

import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.io.filefilter.NotFileFilter;
import org.jvnet.hudson.plugins.backup.BackupException;
import org.kohsuke.stapler.StaplerResponse;

/**
 * This is the backup task, run in background and log to a file
 * 
 * @author vsellier
 * 
 */
public class BackupTask extends BackupPluginTask {
	/**
	 * delay between two verification of no running processes
	 */
	private final static Integer DELAY = 5000;

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

		// Have to include shutdown time in backup time ?
		waitNoJobsInQueue();

		FileFilter filter = createFileFilter(exclusions);

		try {
			BackupEngine backupEngine = new BackupEngine(logger,
					configurationDirectory, fileName, filter);
			backupEngine.doBackup();
		} catch (BackupException e) {
			e.printStackTrace(logger.getWriter());
		} finally {
			cancelNoJobs();

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

	private void waitNoJobsInQueue() {
		logger
				.info("Setting hudson in shutdown mode to avoid files corruptions.");
		try {
			Hudson.getInstance().doQuietDown(getStaplerResponseFake());
		} catch (Exception e) {
			logger.error("Erreur putting hudson in shutdown mode.");
			e.printStackTrace(logger.getWriter());
		}

		logger.info("Waiting all jobs end...");

		Computer computer[] = Hudson.getInstance().getComputers();

		int running = 1;
		// TODO BACKUP-PLUGIN add timeout here
		while (running > 0) {
			running = 0;
			for (int i = 0; i < computer.length; i++) {
				running += computer[i].countBusy();
			}
			logger.info("Number of running jobs detected : " + running);
			
			try {
				Thread.sleep(DELAY);
			} catch (InterruptedException e) {
			}
		}

		logger.info("All jobs finished.");
	}

	private void cancelNoJobs() {
		logger.info("Cancel hudson shutdown mode");
		try {
			Hudson.getInstance().doCancelQuietDown(getStaplerResponseFake());
		} catch (Exception e) {
			logger.error("Erreur cancelling hudson shutdown mode.");
			e.printStackTrace(logger.getWriter());
		}
	}

	private StaplerResponse getStaplerResponseFake() {
		return (StaplerResponse) Proxy.newProxyInstance(Thread.currentThread()
				.getContextClassLoader(),
				new Class[] { StaplerResponse.class }, new InvocationHandler() {
					public Object invoke(Object proxy, Method method,
							Object[] args) throws Throwable {
						return null;
					}
				});
	}
}
