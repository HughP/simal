# Introduction #

Simal is an open source tool for building project registries. Simal uses the Description of a Project (DOAP) schema to collect, store and present data about projects. Simal is available under the [Apache software License V2](http://www.oss-watch.ac.uk/resources/apache2.xml).

This document describes how to download and install Simal on your local machine. We are concerned here with the latest binary release of Simal, if you want to work with the latest development release you'll need the developers [quickstart](GettingStarted.md). If you just want to see what Simal can do then you probably want to view our [demo server](http://registry.oss-watch.ac.uk).


---

**_Warning_ : Simal is very young at this time. It works, but is far from feature complete. As such, we welcome input into the requirements and design of the initial system via our [mailing list](http://groups.google.com/group/simal-contributors)**.

---


# Pre-Requisites #

If you want to run Simal's REST API or the Web application you will need a servlet engine such as Jetty or Apache Tomcat.

You can also run Simal from the command line. In that case, all you need is [Java JRE 1.5](http://java.sun.com/javase/downloads/previous.jsp) or higher.

# Downloading #
All our releases are available from the [download section](http://code.google.com/p/simal/downloads/list). Visit that page and download the latest war file. Optionally (but recommended!), download the checksum and the PGP signature to verify the download's integrity.

## Simal web application ##

To run the web application simply drop the simal-webapp-VERSION.war into your chosen servlet container and point your browser at http://localhost:PORT/simal-webapp-VERSION


---

**_Note:_ If you are not running on localhost you will need to configure the application accordingly.**

---


## Simal REST endpoint ##

The web application bundles the REST module with it. To use the REST endpoint first deploy the web application; subsequently, the REST endpoint is available at http://localhost:PORT/simal-webapp-VERSION/simal-rest

## W3C widget to add project to widget instance ##

Optionally, you can download the W3C widget to add a project to a running Simal instance.  You will have to deploy this widget in a W3C widget container such as [Wookie](http://incubator.apache.org/wookie). The default application settings are such that it will assume the Wookie container to be running on localhost port 8888. After deployment the widget will be accessible through the 'Add project' link on the main webpage of Simal.

# Getting help #

Our documentation is minimal at present. If you can't find the information you need on this site please search our user mailing list archives . If you are still lost then join us on our [user mailing list](http://groups.google.com/group/simal-users) where we will be glad to answer your questions.