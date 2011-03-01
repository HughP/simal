To work with widgets you need to use Apache Wookie (Incubating).

The ant build script is provided for convenience, it is mostly a copy of the widget build script
Apache Wookie. It assumes that Wookie is installed from source in the same root directory as the 
Simal web module. If your version of Wookie is in a different location you will need to configure
the properties at the start of the build.xml file (best to do this with a properties file and 
supply a patch).

The most commonly used command from this build script is:

ant deploy-widget

This will ask you the name of the widget to deploy and will deploy it to a running (local) instance
of Wookie. 