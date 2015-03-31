# Introduction #

When a release is built automated tests ensure that the system is behaving as normal. However, before releasing a build it is important to ensure that a human has installed and tested the release. The document describes the **minimum** that you should do in order to test a build of the core module ready for release.

You are strongly encouraged to try variations around the themes outlined in this document. The more thorough you test the build, the less likely there will be a significant bug in the release.

# Details #

**NOTE** It is important that tests are not run inside a directory with a checkout of Simal source code. Please move the jars to a separate location in order.

## Print the help message ##

### Purpose of test ###

To ensure that the command line interface is working.

### Commands ###

```
java -cp simal-core-0.2-jar-with-dependencies.jar uk.ac.osswatch.simal.Simal -h
```

### Expected behaviour ###

The Simal CLI help message is output to the console.

## Print Simal version number ##

### Purpose of test ###

Ensure that Simal is reporting the correct version number.

### Commands ###

```
java -cp simal-core-0.2-jar-with-dependencies.jar uk.ac.osswatch.simal.Simal -v
```

### Expected behaviour ###

Simal will output it's versionnumber and some environment information. Check the version number is as expected.


## Import a local document ##

To ensure a document can be imported from the local filesystem via the command line.

### Commands ###

```
wget http://simal.googlecode.com/svn/trunk/uk.ac.osswatch.simal.core/src/main/resources/simal.rdf 

java -cp simal-core-0.2-jar-with-dependencies.jar uk.ac.osswatch.simal.Simal addxml simal.rdf
```

### Expected Behaviour ###

Confirmation is output that the entity was added.

## Import a remote document ##

To ensure a document can be imported from the local filesystem via the command line.

### Commands ###

```
java -cp simal-core-0.2-jar-with-dependencies.jar uk.ac.osswatch.simal.Simal addxml http://simal.googlecode.com/svn/trunk/uk.ac.osswatch.simal.core/src/main/resources/simal.rdf 
```

### Expected Behaviour ###

Confirmation is output that the entity was added.


## Additional Tests ##

This section lists CLI commands that do not currently have a test strategy defined above. You are encouraged to test these commands and, in the process, write a test strategy for them.

  * writexml
  * importPTSW
  * importOhloh

## Automated Testing ##

When building the distribution a number of unit tests are run. However, these do not adequately test the distribution since they do not test the distribution in isolation of the development and test resources.

The tests outlined above should be written into a test script that can be run against a built command line module.