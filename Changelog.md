# 0.2.5-SNAPSHOT #

## Core module ##

## REST module ##

  * Added name space prefixes to RDF/XML of `/project` call
  * Added support for 'get all categories' on REST call `/allCategories`

## Web Module ##

Added Python script to backup all projects based on their RDF/XML representation.

# 0.2.4 #

## Web module ##

### Add project W3C widget ###

Separate W3C widget named 'doapcreator' included that can generate a DOAP RDF/XML file from a web form and post it to Simal's REST API. See DoapCreatorWidget for more info.

# 0.2.3-SNAPSHOT #

## Core ##

### Jena backend ###

  * Jena is updated to version 2.6.2. to be compatible with Joseki and to allow use of other Jena backend types
  * Added support to SDB and TDB backends. See [Jena documentation](http://jena.sourceforge.net/documentation.html) for details. The default backend is TDB.
  * Backend used at runtime can be configured using the property ` simal.db.type `
  * All properties (as defined in the property files) can now be overruled by using a system property (eg. at startup: `-Dsimal.db.type=TDB `)

### SPARQL Endpoint ###

  * Added JOSEKI SPARQL server (see http://www.joseki.org/)
  * This endpoint is accessible as described in http://joseki.org/protocol.html. The endpoint is on `/joseki/sparql` and only supports the `SPARQL/Query Protocol`, not the `SPARQL/Update Protocol`.

### Add Multiple Projects from Ohloh.net ###

The Command Line interface can now accept a filename instead of a project ID, e.g.

```
importOhloh -f FILENAME
```

This file contains one Ohloh project ID per line (or a comment if the line starts with '#'). All projects listed in this file will be imported into the database. For example:

```
################################
# Projects to import from Ohloh
################################

simal

# Apache projects
apache
forrest
```