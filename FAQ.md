# Web module #

## Working with data in general ##

### How do I export data for use in my own application? ###

There are two main ways of exporting data from Simal. One is to use the [REST API](SimalRESTAPI.md), the other is to use the [Python script](https://simal.googlecode.com/svn/trunk/uk.ac.osswatch.simal.web/src/main/python/backupAllProjects.py) (which will in turn use the REST API).

## Working with people ##

### How can I find people I may be interested in? ###

Perhaps the easiest way to find people is to use the people browser.

**FIXME Note: The faceted browser described below is currently not in use.**

To illustrate this let us assume we want to discover people who are engaged with "access management" projects. To do this using the people browser we need to:

Click on "People Browser" in the main menu. We are initially presented with all the people in the registry.

Click "Access Management" in the category facet selector (on the right).

The results displayed now include only those people who work on projects related to access management. Note that the project facet browser has also been filtered to only those projects that are active in the access management field.


# REST Module #

## Programmmatic Use ##

### How do I create a REST URL programmatically? ###

First you need to create a RESTCommand object. Then use the getPath() method to return the path of the URL, or the getURL() method to get the full URL.

### How do I create a REST command programmatically? ###

Use the RESTCommand.create**helper methods. If there is no helper method for the command you wish to create then use RESTCommand.createCommand(url), where url is the path of the URL you would normally use.**

Note that there are a number of constants in the RESTCommand class that can be used to construct the URL in the latter case.

# Core Module #

## Command Line ##

### How can I operate on the repository from the command line? ###

The core module brings with it a Command Line Interface. In order to use it you must first retrieve a copy of the core module. Once you have that you can run the CLI using a command such as (this command will display the help message):

```
        java -cp
        target/simal-core-0.2-SNAPSHOT-jar-with-dependencies.jar
        uk.ac.osswatch.simal.Simal -h
```

### How can I add projects to the repository using the command line? ###

To add a single RDF/XML DOAP file to the repository use the following command:

```
        java -cp
        target/simal-core-0.2-SNAPSHOT-jar-with-dependencies.jar
        uk.ac.osswatch.simal.Simal addxml FILENAME_OR_URL
```

Where FILENAME\_OR\_URL is the local filename ir the remote URL for a DOAP file.

To add all the RDF/XML DOAP files in a directory to the repository use the command:

```
        java -cp
        target/simal-core-0.2-SNAPSHOT-jar-with-dependencies.jar
        uk.ac.osswatch.simal.Simal addxmldir DIRECTORY
```

Where DIRECTORY is replaced by the directory containing the DOAP files.

### How can I set the location of the persistent store for the repository? ###

From the command line you can use the --dir DIRECTORY switch to set the directory of the persistent store. Replace DIRECTORY with the path to your data directory.

You can also set the following property values in your local.simal.properties files.

```
simal.repository.dir=/path/to/database/directory
simal.repository.filename=databaseName
```

Note the command line switches take precedence over the settings in the local properties file.