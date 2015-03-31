

# Introduction #

This page describes the Simal REST API. Note that by default the REST API is accessible by anyone. If you wish to secure it your only option, at the time of writing, is to use `.htaccess` see below for more details.

# Details #

## URL Structure ##

All REST URLs are of the same form:

`http://foo.com/simal-rest/COMMAND/NAME_VALUE_PAIRS/FORMAT`

Where COMMAND is the command you wish to execute, NAME\_VALUE\_PAIRS is zero or more parameter names and value pairs and FORMAT is a format code that specifies the format of the response document.

NAME\_VALUE\_PAIRS take the form of name-value. For example, to provide a project identifier you would use something like `project-prj126`. Name value pairs can be provided in any order.

## Format ##

Currently, only `json` and `xml` are supported as valid data FORMATs.

## Project Data ##

### Add a project ###

To add a project using a DOAP POST to:

`http://foo.com/simal-rest/addProject`

The RDF project data should be contained in a parameter called rdf .

### Retrieve a Single Project ###

To retrieve the details of a single project use:

`http://foo.com/simal-rest/project/project-ID/FORMAT`

Where ID is the project identifier and FORMAT is an approved format.

#### Retrieve a featured project ####

By calling the method to retrieve a single project with an ID of "featured" you will retrieve a single featured project.

### All Projects ###

To retrieve all projects from the standard SIMAL repository use:

`http://foo.com/simal-rest/allProjects/FORMAT`

## Person Data ##

## Retrieve a Single Person ##

To retrieve the details of a single person use:

`http://foo.com/simal-rest/person/person-ID/FORMAT`

Where ID is the person identifier and FORMAT is an approved format.

### All Colleagues of a Person ###

A colleague is defined as someone who works on any of the projects our target person works on.

To retrieve all colleagues from the standard SIMAL repository use:

`http://foo.com/simal-rest/allColleagues/person-ID/FORMAT`

Where ID is replaced with the Simal ID of the person we are interested in.

### Retrieve all People ###

To retrieve the details of all people use:

`http://foo.com/simal-rest/allPeople/FORMAT`

## Alternative Data Sources ##

Simal can retrieve data from sources other than the standard Simal repository. Such as the MyExperiment social networking tool for scientific research. In order to use alternative data sources use the "source" parameter as follows:

`http://foo.com/simal-rest/allColleagues/source-myExperiment/PERSON-ID/FORMAT`

# Securing #

At present there are no security features for the REST API, therefore, if you need to lock it down then you need to use `.htaccess`. For example, in httpd.conf add:

```
ProxyPass /simal-rest !
```

To block all direct access to port 8080:

```
iptables -A INPUT -j DROP -p tcp --destination-port 8080 -i eth0
```