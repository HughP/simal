# Introduction #

When a release is built automated tests ensure that the system is behaving as normal. However, before releasing a build it is important to ensure that a human has installed and tested the release. The document describes the **minimum** that you should do in order to test a build of the web module ready for release.

You are strongly encouraged to try variations around the themes outlined in this document. The more thorough you test the build, the less likely there will be a significant bug in the release.

## Preparation ##

Some of these tests are destructive, it is therefore vital that you do not run these tests in a servlet engine with a live instance of Simal - if you do your data will be modified.

You must have installed a Servlet engine on your machine, we suggest [Jetty](http://www.mortbay.org/jetty) or [Apache Tomcat](http://tomcat.apache.org). Download the web module war and drop it into your servlet container, then start up the container.

# Details #

## Start the webapp ##

### Purpose of test ###

To ensure the webapp is running.

### Commands ###

Start your webapp and then:

```
http://localhost:8080/simal-webapp-0.2
```

### Expected behaviour ###

The homepage of the Simal webapp is displayed with no projects in the repostiory (note for there to be no projects present you need to have completed).

## Populate the repository with test data ##

### Purpose of test ###

To ensure that the repository is working. To ensure subsequent tests will pass.

### Commands ###

# Click on "Tools" in the menu
# Click on "Import test data into the repository"

### Expected behaviour ###

After a short delay you will be returned to the tools page which should now show the following repository statistics:

|Number of Projects|6|
|:-----------------|:|
|Number of People|18|
|Number of Categories|54|

## View project browser ##

### Purpose of test ###

To ensure the project browser is working correctly.

### Commands ###

# Click on "Project Browser" in the menu

### Expected behaviour ###

The Ajax exhibit browser is shown with all 6 projects being visible.

## View people browser ##

### Purpose of test ###

To ensure the people browser is working correctly.

### Commands ###

# Click on "People Browser" in the menu

### Expected behaviour ###

The Ajax exhibit browser is shown with all 18 people being visible.


## View category browser ##

### Purpose of test ###

To ensure the category browser is working correctly.

### Commands ###

# Click on "Category Browser" in the menu

### Expected behaviour ###

The Ajax exhibit browser is shown with all 54 categories being available.


## Delete all data ##

### Purpose of test ###

To ensure data removal works. To ensure the repostiory is clean for the next set of tests.

### Commands ###

**WARNING** This will delete all data. Be sure that you are not running these tests in a live environment.

# Click on "Tools" in the menu
# Click on "Remove all data from the repository"

### Expected behaviour ###

After a short delay you will be returned to the tools page which should now show the following repository statistics:

|Number of Projects|0|
|:-----------------|:|
|Number of People|0 |
|Number of Categories|0 |