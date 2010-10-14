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

package org.jvnet.hudson.plugins.backup.utils;

import hudson.model.Computer;
import hudson.model.Hudson;
import hudson.security.ACL;
import org.acegisecurity.context.SecurityContextHolder;
import org.apache.commons.io.filefilter.DelegateFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.codehaus.plexus.util.DirectoryScanner;
import org.jvnet.hudson.plugins.backup.BackupConfig;
import org.jvnet.hudson.plugins.backup.BackupException;
import org.jvnet.hudson.plugins.backup.utils.filename.SimpleFileFilter;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Matcher;

/**
 * This is the backup task, run in background and log to a file
 *
 * @author vsellier
 */
public class BackupTask extends BackupPluginTask {
	public final static String JOBS_NAME = "jobs";
	public final static String WORKSPACE_NAME = "workspace";
	public final static String FINGERPRINTS_NAME = "fingerprints";
	public final static String BUILDS_NAME = "builds";
	public final static String ARCHIVE_NAME = "archive";
	public final static String[] DEFAULT_EXCLUSIONS = { "backup.log" };

    /**
     * delay between two verification of no running processes
     */
    private final static Integer DELAY = 5000;
    
    public BackupTask(BackupConfig configuration, String hudsonWorkDir, String backupFileName, String logFilePath) {
        super(configuration, hudsonWorkDir, backupFileName, logFilePath);
    }

    public void run() {
        assert (logFilePath != null);
        assert (configuration.getFileNameTemplate() != null);

        SecurityContextHolder.getContext().setAuthentication(ACL.SYSTEM);
        
        startDate = new Date();
        try {
            logger = new BackupLogger(logFilePath, configuration.isVerbose());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Unable to open log file for writing: {0}", logFilePath);
            return;
        }

        logger.info("Backup started at " + getTimestamp(startDate));

        // Have to include shutdown time in backup time ?
        if(!configuration.isNoShutdown())
            waitNoJobsInQueue();

        // file filter specific to what's inside jobs' worskpace
        IOFileFilter jobsExclusionFileFilter = null;

        // Creating global exclusions
        List<String> exclusions = new ArrayList<String>();
    	exclusions.addAll(Arrays.asList(DEFAULT_EXCLUSIONS));
    	exclusions.addAll(configuration.getCustomExclusions());
    	if (! configuration.getKeepWorkspaces()) {
    		exclusions.add(WORKSPACE_NAME);
    	}
        else {
            jobsExclusionFileFilter = createJobsExclusionFileFilter(
                    hudsonWorkDir,
                    configuration.getJobIncludes(),
                    configuration.getJobExcludes(),
                    configuration.getCaseSensitive());
        }
    	if (! configuration.getKeepFingerprints()) {
    		exclusions.add(FINGERPRINTS_NAME);
    	}
    	if (! configuration.getKeepBuilds()) {
    		exclusions.add(BUILDS_NAME);
    	}
    	if (! configuration.getKeepArchives()) {
    		exclusions.add(ARCHIVE_NAME);
    	}
        
        IOFileFilter filter = createFileFilter(exclusions, jobsExclusionFileFilter);
        if(configuration.isXmlOnly())
            filter = FileFilterUtils.andFileFilter(filter, FileFilterUtils.orFileFilter(FileFilterUtils.suffixFileFilter(".xml"), FileFilterUtils.directoryFileFilter() ));

        try {
            BackupEngine backupEngine = new BackupEngine(logger,
                    hudsonWorkDir, backupFileName, configuration.getArchiveType().getArchiver(), filter);
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

    public static IOFileFilter createFileFilter(List<String> exclusions, IOFileFilter jobsExclusionFileFilter) {
        if(jobsExclusionFileFilter == null) {
            return FileFilterUtils.notFileFilter(new NameFileFilter(exclusions.toArray(new String[]{})));
        }
        else {
            return FileFilterUtils.andFileFilter(
                    FileFilterUtils.notFileFilter(new NameFileFilter(exclusions.toArray(new String[]{}))),
                    jobsExclusionFileFilter);
        }
    }

    /**
     * Returns a file filter filtering files/dirs to NOT include from jobs' workspace
     * (this means the returned file filter is already a negation).
     */
    public static IOFileFilter createJobsExclusionFileFilter(
            String hudsonWorkDir,
            String jobIncludes,
            String jobExcludes,
            boolean caseSensitive) {

        // directory scanning will be done from the HUDSON_HOME/jobs directory
        DirectoryScanner directoryScanner = new DirectoryScanner();
        directoryScanner.setBasedir(new File(hudsonWorkDir, JOBS_NAME));
        directoryScanner.setCaseSensitive(caseSensitive);

        // for each specified inclusion, we need to prefix it with "*/workspace"
        // to match the workspace directory of each job
        if(jobIncludes != null && jobIncludes.length() > 0) {
            // jobIncludes looks like file1,fil2,dir1/**,dir2/*
            // we need to prefix the first element in the list with "*/workspace"
            jobIncludes = jobIncludes.replaceAll(Matcher.quoteReplacement("\\"), "/");
            jobIncludes = "*/" + WORKSPACE_NAME + '/' + jobIncludes;
            // we do the same for all other elements in the list
            jobIncludes = jobIncludes.replaceAll(",", ",*/" + WORKSPACE_NAME + '/');
            directoryScanner.setIncludes(jobIncludes.split(","));
        }
        else {
            directoryScanner.setIncludes(null);
        }

        // the same is done for exclusions
        if(jobExcludes != null && jobExcludes.length() > 0) {
            jobExcludes = jobExcludes.replaceAll(Matcher.quoteReplacement("\\"), "/");
            jobExcludes = "*/" + WORKSPACE_NAME + '/' + jobExcludes;
            jobExcludes = jobExcludes.replaceAll(",", ",*/" + WORKSPACE_NAME + '/');
            directoryScanner.setExcludes(jobExcludes.split(","));
        }
        else {
            directoryScanner.setExcludes(null);
        }

        directoryScanner.scan();

        FileFilter ff = new SimpleFileFilter(
                directoryScanner.getBasedir(),
                directoryScanner.getExcludedDirectories(),
                directoryScanner.getExcludedFiles());

        return FileFilterUtils.notFileFilter(new DelegateFileFilter(ff));
    }

    private void waitNoJobsInQueue() {
        logger.info("Setting hudson in shutdown mode to avoid files corruptions.");
        try {
            Hudson.getInstance().doQuietDown();
        } catch(NoSuchMethodError e) {
            // nothing to do, this "hack" is there for the plugin to work with
            // Hudson 1.312
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
            Hudson.getInstance().doCancelQuietDown();
        } catch(NoSuchMethodError e) {
            // nothing to do, this "hack" is there for the plugin to work with
            // Hudson 1.312
        } catch (Exception e) {
            logger.error("Erreur cancelling hudson shutdown mode.");
            e.printStackTrace(logger.getWriter());
        }
    }

}
