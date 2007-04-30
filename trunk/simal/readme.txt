Building from Source
====================

There are two parts to Simal, an Apache Forrest generated Catalogue site and an 
Apache Wicket web application. For now the web application can be ignored by 
the average user since it does not do anything particularly useful. If you 
want to work on it then join our mailing lists.

To work with the catalogue you will need to checkout an build
Apache Forrest 0.8-dev (that is Forrest's current SVN head). The Forrest 
project has some excellent documentation for this (it's easy, honest).

Running the Catalogue Application
=================================

Once you have Forrest installed change to your Simal directory and run the 
command forrest run. Now point your browser at http://localhost:8089

That's it!

Note
----
We use a different port to the default Forrest port.

You are now running the catalogue in dynamic mode. You can edit content 
files (stored in src/documentation/content/xdcos) and then refresh your
browser to see the changes immediately. It's worth noting that this version 
of the catalogue also includes all the project documentation. So you can help 
us improve the documentation by running the catalogue and editing the files 
whilst you experiment. Use the Contributor tab above to be taken to the 
contributor section where you will find more information on how to help 
Simal improve.