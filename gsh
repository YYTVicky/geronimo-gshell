#!/bin/sh

JAVA_OPTS="$JAVA_OPTS -ea" `dirname $0`/gshell-*/bin/gsh -e "$@"
