This document describes how to download and run the various components of Simal. It does not describe the set-up of a development environment, this is a task left for the reader (although we do have a document describing how to [set up Eclipse](DevelopSimal.md)).

If you have any problems you could try looking at our [FAQ](FAQ.md) or asking questions on our [mailing list](http://groups.google.com/group/simal-contributors).

# Latest Sources #

## Pre-Requisites ##

  * [Java JDK](http://java.sun.com/javase/downloads/previous.jsp) 1.5+
  * [Subversion](http://subversion.tigris.org/project_packages.html)
    * You may prefer to use a GUI to work with Subversion, such as [RapidSVN](http://rapidsvn.tigris.org/)
  * [Apache Maven](http://maven.apache.org/download.html)

## Get Source Files ##

```
svn checkout http://simal.googlecode.com/svn/trunk/ $PROJECT_HOME/simal-trunk
```

where $PROJECT\_HOME is your chosen project directory

NOTE: Use https rather than http if you are a committer

## Install the artifacts ##

The installation process is not as streamlined as it should be right now. However, we are working on it and welcome your help.

First you will have to create the artifacts for the three components _core_, _rest_ and _web_ :

```
cd $PROJECT_HOME/simal-trunk/uk.ac.osswatch.simal.core
mvn install

cd $PROJECT_HOME/simal-trunk/uk.ac.osswatch.simal.rest
mvn install

cd $PROJECT_HOME/simal-trunk/uk.ac.osswatch.simal.web
mvn install
```

### Release Audit Tool (RAT) ###

Whenever you build Simal we run a tool called RAT (Release Audit Tool) over the code. This tool does a number of things to check the legal status of the code. If it finds a problem, such as files that do not have a valid licence header, it will fail the build with a message such as `too many unapproved licenses`.

See the CodeAuditing page for information about how to resolve these problems.

## Configuration ##
Next, you can configure the webapp for your server by creating a local properties file. This is not required as there is a default configuration provided that should work out-of-the-box (defined in  `$PROJECT_HOME/uk.ac.osswatch.simal.core/src/main/resources/default.simal.properties`).

If you do wish to use custom settings you will have to create a file called `local.simal.properties`. There are two main approaches for using this file:

  1. You can create the file in any directory you want. In this case you will also have to specify an environment variable `SIMAL_HOME` and have this point to the directory you have chosen for your property file. This setting will then also be used as a default location for the database files.
  1. If you have not specified the environment variable `SIMAL_HOME` Simal will search for the `local.simal.properties` file in your home directory (as specified by the Java system property `user.home`).

Typical properties you may want to add in your local configuration are for example:

```
simal.user.webapp.baseurl=http://localhost:8080
simal.rest.baseurl=http://localhost:8080/simal-rest
```

Other configuration properties are available. See `$PROJECT_HOME/uk.ac.osswatch.simal.core/src/main/resources/default.simal.properties` for details.

## Running ##

Simal is made up of three components and therefore can be accessed in three ways. Each of these is described below in order of usefulness to the average user (or reverse order for the average developer).

Note, in all these cases the documentation is currently held in SVN, we've not got around to publishing it yet.

### Simal Web Application ###

The Simal web module provides a web application that provides a GUI interface to a Simal repository. The easiest way to run the web application is by using the light-weight Jetty servlet container from Maven after installing the core, rest and web modules in your local repository (as described above) :

```
cd $PROJECT_HOME/simal-trunk/uk.ac.osswatch.simal.web
mvn jetty:run
```

If you want to run the server on a different port than the default 8080 you will have to
make changes in two places. You will have to create a custom properties file as defined above, but you will also have to use the property `jetty.port` when starting Jetty from the command line. For example:

```
mvn -Djetty.port=9999 jetty:run
```

The default admin username and password is `simal`, you can change this in your local.simal.properties file.

#### Debug Mode ####

To run the web application in debug mode using JDPA run the command (in a Linux environment):

```
export MAVEN_OPTS='-Xdebug -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n'
```

After setting this property, run "maven jetty:run" and connect your debugger to port 8000. If "suspend=y" is used, it will will block, waiting for a debug connection.


### Simal Rest ###

Simal rest is a module providing a REST API to Simal. In its current form, the REST module is not intended to run separately from the web application.
Therefore, in order to use the REST module, deploy the web application as described above. The [REST documentation](SimalRESTAPI.md) explains the REST API in more detail.

### Simal Core ###

Simal core is the back end systems. It contains the data model, data services and repository. Simal core provides a command line interface.

Simal provides a command line interface (CLI) for working with core, other interfaces are possible, such as the separate web application found in the module uk.ac.osswatch.simal.web. It is outside the scope of this document to discuss these other interfaces. However, a very brief introduction to the CLI is useful at this point since it means you can can start playing immediately.

To use the CLI you must first build a distribution:

```
cd $PROJECT_HOME/simal-trunk/uk.ac.osswatch.simal.core
mvn assembly:assembly
```

Now you can run the CLI with a command such as

```
        java -cp
        target/simal-core-0.1-SNAPSHOT-jar-with-dependencies.jar
        uk.ac.osswatch.simal.Simal -h
```

Replace `0.1-SNAPSHOT` with the current version of Simal.

# Questions? #

Documentation never answers all your questions. We welcome your questions and suggestions via our [mailing list](http://groups.google.com/group/simal-contributors).