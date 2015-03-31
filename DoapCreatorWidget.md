# Introduction #

In version 0.2.4. the first W3C widget 'doapcreator' is introduced. The sources can be found in the web project, folder `src/main/widgets/doapcreator`. This page explains how to use this widget in combination with Simal.

# Details #

The doapcreator widget is a W3C widget to be used with [Apache Wookie (Incubating)](http://incubator.apache.org/wookie) or any other W3C Widget server. The widget is built as part of the regular Maven2 build cycle and the resulting wgt archive can then be found in the `target/widgets` folder.

The widget needs to be deployed in a separately running Wookie server. Some configuration is necessary to make sure the widget can be used in Simal.

# Configuration #

The configuration that is needed in Simal and Wookie is very much in alpha phase currently. There are a few configuration parameters that need to be set correctly to make sure that the running Simal application and the running Wookie server can communicate.

For Simal to find Wookie, the property `simal.wookie.url` is used. This can be set at runtime, eg. by starting the application as follows:

```
mvn jetty:run -Dsimal.wookie.url=http://localhost:8888/wookie
```

When the doapcreator submits a project to Simal, it uses the url which is currently hardcoded in the JavaScript file [doapform.js](https://simal.googlecode.com/svn/trunk/uk.ac.osswatch.simal.web/src/main/widgets/doapcreator/scripts/doapform.js):

```
var loc = 'http://localhost:8080/simal-rest/addProject';
```

This means that if your Simal instance is not running on this url, you will have to edit this JavaScript file. To do this from the released wgt you can simply unzip the wgt archive, edit the file and rezip the archive.

# Usage #

The doapcreator widget is accessible via the main Simal menu via the 'Add Project' link. If the Wookie server is not accessible or the doapcreator widget is not installed on the configured Wookie server the link will lead to the regular Doap Form Page.