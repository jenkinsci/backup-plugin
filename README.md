# Backup plugin

Copyright &copy; 2009-2011, Vincent Sellier, Manufacture Française des Pneumatiques Michelin, Romain Seguy, and other contributors. Licensed under MIT License.

## About this plugin

The Backup plugin is meant to be used from [Jenkins](https://jenkins.io/) to provide a way to backup and restore Jenkins configuration files.

This plugin is searching for a new maintainer!

## Want periodic backups?

This plugin requires that you manually start it to back up your data.

For automated periodic backups, check out the [thinBackup](https://plugins.jenkins.io/thinBackup/) plugin.

## Description

The Backup plugin adds a new **Backup manager** item in the **Manage
Jenkins** page. This item allows (cf. screenshot below):

-   Tweaking backup settings (using the **Setup** link)
-   Backuping Jenkins's configuration (using the **Backup Jenkins
    configuration** link)
-   Restoring Jenkins's configuration from a previous backup (using the
    **Restore Jenkins configuration** link)

## Alternatives to this plugin

See the [Jenkins user documentation](https://www.jenkins.io/doc/book/system-administration/backing-up/) for alternatives.

## Installation

The Backup plugin can be installed from any Jenkins installation connected to the Internet using the **Plugin Manager** screen.

## Source code

The source code of this plugin is on [GitHub](https://github.com/jenkinsci/backup-plugin).
