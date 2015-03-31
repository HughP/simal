This document tells you about the various ways you can manage the categories on your own installation.

# Add a Project to a Category #

Once you have a project in the database you can add a category by viewing the project details and clicking "New" under the categories heading. This will provide a drop down list of all categories currently in the system. Simply select the one you want and click add.

# Adding Categories to the Database #

The description above tells you how to add an existing category to an project, this section will tell you how to add a new category.

## Adding Categories With Projects ##

Categories are added to the system automatically whenever a DOAP file is added that includes category definitions. So, to add a category using DOAP simply ensure your file has entries such as:

```
<doap:category rdf:resource="http://simal.oss-watch.ac.uk/category/supplementaryDOAPTest#" />
```

You can add an {{{rdfs:label}} is you so desire, e.g.

```
<doap:category rdf:resource="http://simal.oss-watch.ac.uk/category/simalCategoryTest#" rdfs:label="Category Test">
```

## Adding Categtories Via the CLI ##

To add a category via the command line we are currently limited to submitting a DOAP file, as above. However, it might be useful to allow categories to be added in isolation via the CLI. See [ISSUE369](https://code.google.com/p/simal/issues/detail?id=369).

## Adding Categories Via the User Interface ##

At the time of writing there is no way to add categories from the UI. Although this is a feature that we intend to implement, see [ISSUE368](https://code.google.com/p/simal/issues/detail?id=368).