# Introduction #

Simal uses a number of tools to check source code for common issues. This document describes the audit tools in use and what they tell us.

Perhaps the most important tool is our [Continuous Integration](http://www.16degrees.com.au/hudson) server generously hosted by [16degrees.com.au](http://www.16degrees.com.au). This server builds the code and runs all tests within 15 minutes of any SVN commit, it will also build all downstream projects. Any failures in the uild or tests will be sent to the developer mailing list.

# Unit and Integration Tests #

All code contributed to Simal must be accompanied by integration and/or unit tests.

To run these tests locally use `mvn test`

All tests are run by the continuous integration server at least once every night and after a change is detected in SVN.

# Cobertura test coverage reports #

[Cobertura](http://cobertura.sourceforge.net/) is a tool for generating reports on unit test coverage. It can help you in assessing how well you code is covered by the unit tests and which parts need some more attention.
It is integrated with a Maven plugin and forms part of the site reports. It can be run on your local machine by executing `mvn site`.
After Maven has successfully finished you can view the reports by opening the page `target/site/index.html` and navigate to `Project Reports > Cobertura Test Coverage`.
The Project Reports section also displays the reports of some other useful tools that are discussed below.

# Find Bugs #

[FindBugs™](http://findbugs.sourceforge.net/) is a program to find bugs in Java programs. It looks for instances of "bug patterns" --- code instances that are likely to be errors.

We use our [Continuous Integration](http://www.16degrees.com.au/hudson) server to track trends in FindBugs warnings.

# Compiler Warnings #

We use our [Continuous Integration](http://www.16degrees.com.au/hudson) server to track trends in compiler warnings.

The CI server is set to fail a build when the number of warnings goes over 15. The health of the project is also dependant on the number of warnings, requiring less than five for a "sunny" outlook and less than 10 for an "overcast" outlook.

# PMD #

[PMD](http://pmd.sourceforge.net/) scans Java source code and looks for potential problems like:

  * Possible bugs - empty try/catch/finally/switch statements
  * Dead code - unused local variables, parameters and private methods
  * Suboptimal code - wasteful String/StringBuffer usage
  * Overcomplicated expressions - unnecessary if statements, for loops that could be while loops
  * Duplicate code - copied/pasted code means copied/pasted bugs

To run PMD on the Simal code locally use `mvn pmd:pmd`

## PMD and Continuous Integration ##

The CI server is set to fail a build when the number of PMD warnings goes over 10. The health of the project is also dependant on the number of warnings, requiring less than 3 for a "sunny" outlook and less than 7 for an "overcast" outlook.

# Licence Audit #

[RAT](http://incubator.apache.org/rat) is used to audit source files for correct licence headers and other legal requiremets.

RAT is run every time we try and do an install of a Simal artifact. If any problems occur the build will be aborted and an error message shown.

The report from RAT is output to `target/rat.txt` this will give more information about any problems found.

RAT can be run manually with the command:

`mvn rat:check`

## Excluding Files from RAT checks ##

In some cases RAT provides a false positive on licence checks. To avoid these you can add exclusions to the RAT configuration in the projects pom.xml. See:

```
<plugin>
  <groupId>org.codehaus.mojo</groupId>
  <artifactId>rat-maven-plugin</artifactId>
  <configuration>
    ...
  </configuration>
</plugin>
```

## Adding Licence Headers ##

RAT can also be used to automatically add licence headers using:

`java -jar rat-VERSION.jar --addLicence  --copyright "Copyright 2008 University of Oxford" --force  /path/to/project`