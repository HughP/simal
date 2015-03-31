# How to develop Simal #

This How-To describes how to set up your development environment
for working with Simal. Since Simal uses many Eclipse plugins in components
of the Simal project it makes most sense for you to work within the Eclipse
environment (although this is not a requirement, it just makes your life
easier). This how-to therefore focuses on configuring Eclipse for working
on Simal.

If you just want to build and run Simal, without doing any development, then see GettingStarted instead.

## Intended Audience ##

People wishing to participate in the development of Simal.

## Purpose ##

To set up an Eclipse development environment that can build Simal.

## Prerequisites ##

Readers should

  * Have at least a basic understanding of RDF, HTML and Java

## 1. Install Eclipse ##

### Eclipse 3.5 + Web Tools Project 3.1 ###

If you do not currently have eclipse installed we recommend that you download and unpack the [Eclipse IDE for Java EE Developers](http://www.eclipse.org/downloads/) package.

If you already have eclipse then you will need to ensure that you also
have the Web Standards Tools (WST) plugin installed (use the WTP
[update site](http://download.eclipse.org/webtools/updates/)).

### Configure JDK ###
The m2eclipse plugin you're about to install requires Eclipse to be run in a JDK.
  1. Download and install a JDK if you don't have one already, from oracle.com.
  1. Add these two lines to the eclipse.ini file, adjusting the path as necessary:

{{{-vm
C:\java\jdk1.6.0\_23\bin\javaw.exe}}}

## 2. Get Eclipse Plugins ##
The easiest way to get the Simal code into Eclipse is as follows.
### Subclipse ###
We use a Subversion repository for our source code, therefore you need
an SVN plugin like Subclipse for working with the repository. Install it as follows:

  1. Help | Install New Software...
  1. Click "Add..." and enter a name, with the following location: http://subclipse.tigris.org/update_1.6.x
  1. In the "Work with:" box, enter: http://subclipse.tigris.org/update_1.6.x
  1. Wait a few moments, then select the Subclipse plugin, and press Finish.

For more information about Eclipse plugins, try [this tutorial](http://www.vogella.de/articles/Eclipse/article.html#plugin_installation).

### m2eclipse ###
Simal is built with Maven. Therefore, you should install the m2eclipse plugin. Follow the above procedure, with this repository: http://m2eclipse.sonatype.org/sites/m2e

This Core update site contains a single component: "Maven Integration for Eclipse (Required)". When you install this component (in the same way as above) you will be installing all of the core Wizards, the POM Editor, Maven Repository integration, and Maven integration.

### Wicket Bench ###

[Wicket Bench](http://www.laughingpanda.org/mediawiki/index.php/Wicket_Bench) is
a plugin that makes working with Wicket files easier. It can be installed
from the [update site](http://www.laughingpanda.org/svn/wicket-bench/trunk/wicket-bench-site)
using the method described above.

TODO describe how to use this.

## 3. Get Simal Sources ##
The first thing you need to do is checkout the Simal source code. To do this
use the [simal.psf](http://simal.googlecode.com/svn/wiki/DevelopSimal.attach/simal.psf) team set.

  * Save the [simal.psf](http://simal.googlecode.com/svn/wiki/DevelopSimal.attach/simal.psf)
file in a convenient location

  * In Eclipse select `File -> Import`

  * In the dialog select `Team -> Team Project Set` and click `Next`

  * Select the `simal.psf` file you saved earlier and click `Finish`

After following these steps the source code for Simal will be checked out from
version control, ready for you to start work.

## 4. Get Maven dependencies ##
After importing the files from SVN you will need to tell Eclipse to use Maven to build the source. To do this right click on each of the `uk.ac.osswatch.simal.*` projects and select `Maven -> Enable Dependency Management`. Maven will now download all dependencies; this can take some time depending on the speed of your network detection, but it is a one-off step.

## 5. Test Setup So Far ##

To verify that everything compiles and builds correctly, let's now run
some tests:

  * Right click on `uk.ac.osswatch.simal.core/src/test/java` and select `Run As -> JUnit Test`
  * Wait for the tests to run. On the "JUnit" tab, you should get a green bar for all tests.
  * Right click on `uk.ac.osswatch.simal.rest/src/test/java` and select `Run As -> JUnit Test`
  * Wait for the tests to run. On the "JUnit" tab, you should get a green bar for all tests.
  * Right click on `uk.ac.osswatch.simal.web/src/test/java` and select `Run As -> JUnit Test`
  * Wait for the tests to run. On the "JUnit" tab, you should get a green bar for all tests.

## 6. Building and Running from within Eclipse ##
### Build ###
At this point you will have three projects in Eclipse, one for the `core`, the `rest` and the `web` project. You can run Maven2 commands for each project from the `Run as` context menu.

  1. Right-click `uk.as.osswatch.simal.core`
  1. Choose `Run As -> Maven install`

This installs the core module in the local repository.

### Deploy ###
To deploy the webapp from within Eclipse you can also use the m2eclipse run configuration.

  1. Go to `Run -> Run Configurations...` from the main menu.
  1. Right-click `Maven Build` and choose `New`
  1. Use "`Browse Workspace...` to select `uk.ac.osswatch.simal.web`
  1. Enter `jetty:run` in `Goals:`.

Now you can debug the webapp from within Eclipse.

  1. Select `Run -> Run` from the main menu.

  1. Go to `Run -> Run Configurations...` from the main menu.
  1. Right-click `Maven Build` and choose `New`
  1. Use "`Browse Workspace...` to select `uk.ac.osswatch.simal.web`
  1. Enter `jetty:run` in `Goals:`.

Now you can debug the webapp from within Eclipse.

  1. Select `Run -> Run` from the main menu.

Note however that there is an issue with persistencing the data when you're running from within. The shutdown hook in Simal that ensures that the project data is persisted on disk is not called when shutting down the JVM in Eclipse. This is a bug/feature of Eclipse. See [Issue 344](http://code.google.com/p/simal/issues/detail?id=344) for more info.

## 7. Start developing ##

Assuming all tests past you are now ready to start developing.
Further assistance is available in the relevant sections of the Simal
documentation and in the JavaDocs.

For information about how to contribute patches to the Simal team, see:
  * CreatePatch
  * ContributionProcess

For more information:
http://code.google.com/p/simal/wiki/GettingStarted

### Updating source ###
Don't forget to regularly (perhaps daily) check for updates in the Subversion repository:

  1. Right-click the `uk.ac.osswatch.simal.core` node
  1. Select Team > Update to HEAD
  1. Repeat for the other modules.

## 8. Optional Steps ##


### Codestyle and code formatting ###

We have defined a custom Simal formatter and code templates for Eclipse.
To install these first download the following files:
  * [simal\_formatter.xml](http://simal.googlecode.com/svn/wiki/DevelopSimal.attach/simal_formatter.xml)
  * [simal\_codetemplates.xml](http://simal.googlecode.com/svn/wiki/DevelopSimal.attach/simal_codetemplates.xml)

To import the Formatter go to ` Window -> Preferences -> Java -> Code Style -> Formatter `.
Select `Import` and choose the simal\_formatter.xml.

To import the code templates go to ` Window -> Preferences -> Java -> Code Style -> Code Templates `.
Select `Import` and choose the simal\_codetemplates.xml.

Note that you do not need to use the Simal style and templates for all our projects, you can configure templates on a per project basis within Eclipse.


### Task Management ###

These plugins are useful in tracking and managing tasks.


### Mylyn Generic Issue Tracker Integration ###
TODO Describe use of the Mylyn generic issue tracker to [integrate](http://www.jroller.com/alexRuiz/entry/using_mylyn_with_google_code) with Google Code




### Code Quality ###
The plugins are used for managing code quality in Simal. They typically generate a number of reports indicating potential problems with code quality. It is good practice to run these reports on a periodic basis and to fix as many issues they highlight as is possible.

Many of these plugins integrated with our Maven builds and our [continuous integration](http://16degrees.com.au/hudson) server. This allows us to keep an eye on the quality trends within our code.


### PMD ###

[PMD](http://pmd.sourceforge.net/) scans Java source code and looks for potential problems like:


  * Possible bugs - empty try/catch/finally/switch statements
  * Dead code - unused local variables, parameters and private methods
  * Suboptimal code - wasteful String/StringBuffer usage
  * Overcomplicated expressions - unnecessary if statements, for loops that could be while loops
  * Duplicate code - copied/pasted code means copied/pasted bugs


Use the [PMD Eclipse update](http://pmd.sourceforge.net/eclipse/) site to install this plugin.


### Find bugs ###

[FindBugs](http://findbugs.sourceforge.net/) uses static analysis to look for bugs in Java code.

Use the [FindBugs Eclipse update](http://findbugs.cs.umd.edu/eclipse/) site to install this plugin.