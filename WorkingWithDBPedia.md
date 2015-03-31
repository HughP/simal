# Introduction #

[https:://www.dbpedia.org DBPedia] is a great resource for data. This page contains some notes about how to get info out that could well be useful for us.

The queries on this page are not intended to be exhaustive, just examples of what is available and potentially useful to us.

## Useful Links ##

  * [SPARQL Explorer](http://dbpedia.org/sparql)
  * [SPARQL query builder](http://querybuilder.dbpedia.org/).
  * [DBPedia ontology](http://wiki.dbpedia.org/Ontology)

# Licences #

## Get Free Software Licences ##

```
SELECT DISTINCT ?licenceName WHERE {
  ?license rdf:type <http://dbpedia.org/class/yago/FreeSoftwareLicenses>.
  ?license rdfs:label ?licenceName .
  FILTER (lang(?licenceName) = "en")
}
```

# Projects #

## Get Project Using a Free Software Licence ##

```
SELECT ?projectName, ?Project, ?licenceName, ?Licence WHERE {
  ?Project a <http://dbpedia.org/ontology/Software> .
  ?Project dbpedia2:license ?Licence .
  ?Licence rdf:type <http://dbpedia.org/class/yago/FreeSoftwareLicenses> .
  ?Licence rdfs:label ?licenceName .
  ?Project dbpedia2:name ?projectName .
  FILTER (lang(?projectName) = "en" && lang(?licenceName) = "en")
} LIMIT 100
```

# Universities #

## Get Location of Universities in the UK ##

```
PREFIX dbo: <http://dbpedia.org/ontology/>
PREFIX georss: <http://www.georss.org/georss/>

SELECT DISTINCT ?place ?university WHERE {
     ?university rdf:type dbo:University.
     ?university dbo:country :United_Kingdom.
     ?university georss:point ?place.
}
ORDER BY ?university
```

# Other Ideas #

**Standards (e.g. http://dbpedia.org/page/HTML) - see [ISSUE 25](https://code.google.com/p/simal/issues/detail?id=25)** Software categories