# Introduction #

When a release is built automated tests ensure that the system is behaving as normal. However, before releasing a build it is important to ensure that a human has installed and tested the release. The document describes the **minimum** that you should do in order to test a build of the rest module ready for release.

You are strongly encouraged to try variations around the themes outlined in this document. The more thorough you test the build, the less likely there will be a significant bug in the release.

## Preparation ##

You must have installed a Servlet engine on your machine, we suggest [Jetty](http://www.mortbay.org/jetty) or [Apache Tomcat](http://tomcat.apache.org). Download the REST api war and drop it into your servlet container, then start up the container.

You will also need [CURL](http://curl.haxx.se/) to run these tests.

# Details #

## Start the REST server ##

### Purpose of test ###

To ensure the REST server is running.

### Commands ###

```
http://localhost:8080/simal-rest-0.2
```

### Expected behaviour ###

A page is displayed telling you that the simal rest is available on this server.

## Import RDF Data ##

To ensure a document can be imported from the local filesystem via the REST interface.

### Commands ###

```
curl --data "rdf=<RDF/>" http://localhost:8080/simal-rest-0.2/simal-rest/addProject
```

### Expected Behaviour ###

HTTP response 200.

## Get all projects as JSON ##

To ensure we can retrieve projects in JSON format

### Commands ###

```
curl -G http://localhost:8080/simal-rest-0.2/simal-rest/allProjects/json
```

### Expected Behaviour ###

A list of all projects in the repository in JSON format.


## Get all people as JSON ##

To ensure we can retrieve people in JSON format

### Commands ###

```
curl -G http://localhost:8080/simal-rest-0.2/simal-rest/allPeople/json
```

### Expected Behaviour ###

A list of all people in the repository in JSON format.