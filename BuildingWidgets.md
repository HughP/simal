# Introduction #

In version 0.2.4 we introduced the ability to embed W3C Widgets, served by Apache Wookie. It is our intention to add new functionality to the Wookie UI using Widgets wherever possible. This means that the same UI components cna be used in any environemnt that supports W3C Widgets. Thanks to Apache Wookie this means pretty much anywhere we can include HTML.

This document describes how to build a new widget.

# Requirements #

Access to an instance of Simal, if you run your own instance you can use that, if not we suggest you use our [public instance](http://registry.oss-watch.ac.uk).

All widget code is in the `uk.ac.osswatch.simal.widget` module, so you'll need to check this out as a minimum.

You will also need an instance of [Apache Wookie](http://incubator.apache.org/wookie) that you are able to deploy widgets to. During development we recommend running a local instance of Wookie. We also recommend installing this in the same directory that your Simal modules are located.

We use [Apache Ant](http://ant.apache.org) as a build system to assist in the development of widgets.

# Creating a widget from the template #

We have provided a widget template to get you started. This provides the bare minimum of functionality you need to create a widget that behaves like all other Simal widgets and come equipped with key scripts for working with the Simal REST API.

To create your template run the following commands at the command line:

```
cd [Path to Web Module]/src/main/widgets
ant seed-widget
```

You will be asked a few questions about the widget you wish to build. Once complete you will find your widget in a subdirectory with the same name as the widget (this will have been provided in answer to one of the questions).

# Deploy your widget #

To deploy your widget run:

```
cd [Path to Web Module]/src/main/widgets
ant deploy-widget
```

You will be asked the name of the widget to deploy, once supplied the appropriate widget will be deployed to a locally running instance. Note that if you are not running Wookie from source located in the same directory as your Simal modules then you will need to configure the build system accordingly. Contact us on the developer list for assistance.

If you don't want to type in the widget name each time then you can supply it in  the command line:

```
ant deploy-widget -Dwidget.shortname=[widget name]
```

# Building the widget package #

To build the widget package you just need to run `ant` and provide the name of the widget to build. Your widget package will be located in `[Path to Web Module]/target/widgets/[widget name].wgt`

As with deploying widgets you can supply the widget name at the command line:

```
ant -Dwidget.shortname=[widget name]
```

# Testing and developing your widget #

The testing and development processes are no different from any other Wookie widget. So at this point we simply refer you to the [Wookie](http://incubator.apache.org/wookie) documentation and community.