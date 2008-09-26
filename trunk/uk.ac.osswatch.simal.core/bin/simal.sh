#!/bin/sh

# Copyright 2007 University of Oxford
#
# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.


# set the current working dir as the PROJECT_HOME variable 

cygwin=false;
darwin=false;
case `uname` in
  CYGWIN*) cygwin=true ;;
  Darwin*) darwin=true
           if [ -z "$JAVA_HOME" ] ; then
             JAVA_HOME=/System/Library/Frameworks/JavaVM.framework/Home   
           fi
           ;;
esac

if [ "$cygwin" = "true" ] ; then
  PROJECT_HOME=`cygpath -w "$PWD"`
else
  PROJECT_HOME=`pwd`
fi

if [ -z "$SIMAL_HOME" ] ; then
  # use the location of this script to infer $SIMAL_HOME

  thisprg="$0"

  SIMAL_HOME=`dirname "$thisprg"`/..

  # Make it fully qualified

  SIMAL_HOME=`cd "$SIMAL_HOME" && pwd`
fi

# ----- Set Up The Runtime Classpath ------------------------------------------

if [ "$cygwin" = "true" ] ; then
  S=';'
  SIMAL_DOT_HOME=`cygpath -w "$SIMAL_HOME"`
  SIMAL_SHELL_HOME=`cygpath -u "$SIMAL_HOME"`
else
  S=':'
  SIMAL_DOT_HOME=$SIMAL_HOME
  SIMAL_SHELL_HOME=$SIMAL_HOME
fi

CP="$CLASSPATH"
export CP
unset CLASSPATH

for i in $SIMAL_SHELL_HOME/target/*-with-dependencies.jar; do
  if [ "$cygwin" = "true" ] ; then
    LIB=`cygpath -w $i`
  else
    LIB=$i
  fi

  CLASSPATH=$CLASSPATH$S$LIB
done

export CLASSPATH

echo "Simal.  Run 'simal --help' for command line documentation"
echo
echo "Using CLASSPATH " $CLASSPATH
export SIMAL_HOME
java uk.ac.osswatch.simal.Simal "$@"
RESULT=$?

# ---- Restore Classpath
unset CLASSPATH
CLASSPATH=$CP
export CLASSPATH

exit $RESULT
