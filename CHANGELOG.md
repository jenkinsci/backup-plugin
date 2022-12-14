# Version history

## Version 1.6.1 (08/04/2011)

-   Fixed
    [JENKINS-5968](https://issues.jenkins-ci.org/browse/JENKINS-5968):
    org.codehaus.plexus.archiver.ArchiverException: Failed to read
    filesystem attributes

## Version 1.6 (12/10/2010)

-   Added a new **Configuration files (.xml) only** option to backup
    only configuration files.
-   Added a new **No shutdown** option to trigger backup without
    shutting down Hudson.
-   Added some icons in the **Backup manager** screen.
-   Fixed
    [JENKINS-7634](http://issues.jenkins-ci.org/browse/JENKINS-7634):
    Hudson backup deletes home directory and hangs.

## Version 1.5 (07/01/2010)

-   Implemented
    [JENKINS-6892](http://issues.jenkins-ci.org/browse/JENKINS-6892):
    Allow configuration of custom exclusions
-   Implemented
    [JENKINS-5305](http://issues.jenkins-ci.org/browse/JENKINS-5305): It
    is no more possible to use `tar.gz` and `tar.bz2` compression
    formats from Windows anymore.
-   Fixed
    [JENKINS-5929](http://issues.jenkins-ci.org/browse/JENKINS-5929):
    Some images were not displayed.

## Version 1.4.1 (02/21/2010)

-   Help messages fixed

## Version 1.4 (02/11/2010)

-   It is now possible to define which files/directories have to saved
    from jobs' workspaces using a set of Ant includes/excludes patterns
    (refer to the [@includes of Ant
    fileset](http://ant.apache.org/manual/CoreTypes/fileset.html) for
    the exact format)

## Version 1.3

-   Backup content is now configurable
-   Only one backup in the same time is possible

## Version 1.2

-   Maintenance release to remove a redirection problem on settings form
    submission when hudson is not the ROOT application (on
    <http://myserver/hudson> urls)

## Version 1.1

-   Backup should run in the higher security privilege
    ([report](http://www.nabble.com/Minor-permission-error-with-Backup-plugin---unable-to-enter-shutdown-mode---security-enabled-td23664157.html))
-   Tar formats supported
-   Configuration section
-   File to restore selection

## Version 1.0 (04/10/2009)

-   Initial realease including manual file selection and ZIP format
