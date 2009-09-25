#!/bin/sh

mvn release:prepare release:perform -DremoteTagging=false -DscmCommentPrefix=[backup-plugin]

