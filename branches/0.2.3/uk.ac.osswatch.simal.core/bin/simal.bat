@echo off
rem Copyright 2007 University of Oxford
rem
rem Licensed to the Apache Software Foundation (ASF) under one or more
rem contributor license agreements.  See the NOTICE file distributed with
rem this work for additional information regarding copyright ownership.
rem The ASF licenses this file to You under the Apache License, Version 2.0
rem (the "License"); you may not use this file except in compliance with
rem the License.  You may obtain a copy of the License at
rem
rem     http://www.apache.org/licenses/LICENSE-2.0
rem
rem Unless required by applicable law or agreed to in writing, software
rem distributed under the License is distributed on an "AS IS" BASIS,
rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
rem See the License for the specific language governing permissions and
rem limitations under the License.

if "%OS%"=="Windows_NT" @setlocal
if "%OS%"=="WINNT" @setlocal

rem ----- use the location of this script to infer $SIMAL_HOME -------
if NOT "%OS%"=="Windows_NT" set DEFAULT_SIMAL_HOME=..
if "%OS%"=="Windows_NT" set DEFAULT_SIMAL_HOME=%~dp0\..
if "%OS%"=="WINNT" set DEFAULT_SIMAL_HOME=%~dp0\..
if "%SIMAL_HOME%"=="" set SIMAL_HOME=%DEFAULT_SIMAL_HOME%

rem ----- set the current working dir as the PROJECT_HOME variable  ----
if NOT "%OS%"=="Windows_NT" call "%SIMAL_HOME%\bin\setpwdvar98.bat"
if "%OS%"=="Windows_NT" call "%SIMAL_HOME%\bin\setpwdvar.bat"
if "%OS%"=="WINNT" call "%SIMAL_HOME%\bin\setpwdvar.bat"
set PROJECT_HOME=%PWD%

rem ----- Save and set CLASSPATH --------------------------------------------
set OLD_CLASSPATH=%CLASSPATH%
set CLASSPATH=
cd /d "%SIMAL_HOME%\target\"
for %%i in ("*with-dependencies.jar") do call "%SIMAL_HOME%\bin\appendcp.bat" "%SIMAL_HOME%\target\%%i"
cd /d %PWD%

echo.
echo Simal.  Run 'simal --help' for command line documentation
echo.

java uk.ac.osswatch.simal.Simal %1 %2 %3 %4 %5 %6 %7 %8 %9

rem ---- Restore old CLASSPATH
set CLASSPATH=%OLD_CLASSPATH%
