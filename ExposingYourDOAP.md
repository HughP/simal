# Introduction #

Description of a Project (DOAP) is a RDF schema for describing projects. It was designed for software development projects, but need not be limited to them. This document describes various strategies for exposing DOAP descriptions of your project so that Simal (can other DOAP consumers) can use it.

# Background #

A good introduction to DOAP and its purpose can be found in the [OSS Watch DOAP briefing note](http://www.oss-watch.ac.uk/resources/doap.xml).

Other resources you may find useful are listed at the end of this document.

# How to create your projects DOAP #

There are various ways of creating a single DOAP file. Some are simple, some are complex. As with anything in life, the more complex he process the more control you have, but the steeper the learning curve. This section describes some of the options available to you.

## Manual Creation ##

This is the most complex, but the most flexible way of creating DOAP. If you are familiar with RDF you can simply create the DOAP data by hand and host it like any other data (in your version control system for example). That's all we are going to say about that for now as most people can't or won't do this.

## Netbeans Plugin ##

[DOAP Bean](http://blogs.sun.com/bblfish/entry/doap_bean_available) is a Netbeans plugin for editing and managing DOAP.

## Maven ##

If you project use the Apache Maven build tool there is a [Maven DOAP plugin](http://maven.apache.org/plugins/maven-doap-plugin/) that will generate you DOAP from your pom.xml file.

## Marking up your webpage (RDFa) ##

RDFa provides a neat way for projects to markup their existing web pages with RDF data. The idea is that you maintain a page that contains all the essential information about your project and this page is marked up with RDFa. The following is an example taken from the [W3C RDFa use cases](http://www.w3.org/2006/07/SWD/wiki/RDFa_Use_Cases_and_Requirements#head-af13bd44f15b14906b62387aa4d2c6347514e121):

```
<div xmlns:doap="http://usefulinc.com/ns/doap#" 
     xmlns:foaf="http://xmlns.com/foaf/0.1/" 
     xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#">
  <div about="http://tomcat.apache.org/" class="doap:Project">
    The <a rel="doap:homepage" href="http://tomcat.apache.org" property="doap:name">Apache Tomcat</a> is a <span property="doap:shortdesc">a Java Servlet and Java Servet Pages specifications implementation.</span>
  </div>
</div>
```

# How to expose your database as DOAP #

If you maintain a database of projects then you really ought to be thinking of exposing your data via an API. Simply pull the data from your database, convert it to RDF/XML and provide a way of requesting that data via an API.

You could also expose it via your normal web pages (see RDFa above). However, since you are probably generating your pages dynamically and since the processing to create an HTML page is greater than that to create a RDF/XML document you may want to consider performance here. However, if your data set is small annotating your HTML pages with RDFa is a quick and easy way of exposing your existing data. It also has the added benefit of ensuring that you only expose the data you want to expose. That is, if the HTML page is visible there is no reason why the DOAP data within it should not be visible.

# Resources #

DOAP home page http://trac.usefulinc.com/doap

How it is used in Simal at http://code.google.com/p/simal/wiki/Schema

Samples we use in testing Simal
http://code.google.com/p/simal/source/browse/#svn/trunk/uk.ac.osswatch.simal.core/src/main/resources/testData

Most complete sample (i.e. uses tricky combinations of elements)
http://code.google.com/p/simal/source/browse/trunk/uk.ac.osswatch.simal.core/src/main/resources/testData/testDOAP.xml

You can test DOAP outputs by pasting them into the "raw doap" field at
at http://registry.oss-watch.ac.uk/?wicket:bookmarkablePage=:uk.ac.osswatch.simal.wicket.doap.DoapFormPage