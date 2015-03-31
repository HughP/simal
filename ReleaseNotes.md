# Release 0.2.4 #

Released on [16/07/2010](http://groups.google.com/group/simal-contributors/browse_thread/thread/2d0aece2852e7bfc#) and based on 0.2.4-rc1.


# Release 0.2.4-rc1 #

Released for test: [17/06/2010](http://groups.google.com/group/simal-contributors/browse_thread/thread/26b469a187ec100d#)

This release contains a separate widget to [add a project](DoapCreatorWidget.md) to Simal from a separate environment hosted by a [Wookie server](http://incubator.apache.org/wookie).
Also, this is the first release in which the URLs are stable where possible making it possible to deep link to a project, person or category page.

# Release 0.2.3-rc2 #

Released for test: [11/03/2010](http://groups.google.com/group/simal-contributors/browse_thread/thread/2026719cf4579d67#)

Email addresses are now obfuscated to prevent bot harvesting ([Issue 257](https://code.google.com/p/simal/issues/detail?id=257))

# Release 0.2.3-rc1 #

Released for test: [18/02/2010](http://groups.google.com/group/simal-contributors/browse_thread/thread/220eae477b088179#)

Jena backend to TDB. See [Changelog](Changelog.md) for more details.

# Release 0.2.2 #

Released for test: [01/12/2009](http://groups.google.com/group/simal-contributors/browse_thread/thread/964cd179db018f19/17a27dce849077f7?lnk=gst&q=0.2.2#17a27dce849077f7)

Some of the most important changes in this release:

  * Functionality added to backup the database from the command-line ([Issue 157](https://code.google.com/p/simal/issues/detail?id=157))
  * Added support for XHTML tags in body text; you now can markup data like the description with HTML tags as long as it's valid XML ([Issue 179](https://code.google.com/p/simal/issues/detail?id=179))
  * PIMS imports now support empty cells in the Excell sheets ([Issue 254](https://code.google.com/p/simal/issues/detail?id=254))
  * Labels for homepages are configurable ([Issue 191](https://code.google.com/p/simal/issues/detail?id=191))
  * Support for blogs using foaf:weblog ([Issue 32](https://code.google.com/p/simal/issues/detail?id=32))
  * Search facility for projects and people ([Issue 153](https://code.google.com/p/simal/issues/detail?id=153))
  * Link project imports via rdf:seeAlso ([Issue 201](https://code.google.com/p/simal/issues/detail?id=201))


See also the [complete issue list](http://code.google.com/p/simal/issues/list?can=1&q=Milestone:0.2%20OR%20Milestone:0.3%20Status:Verified%20OR%20Status:Fixed%20modified-after:2008/9/1&sort=modified&colspec=ID%20Type%20Status%20Priority%20Milestone%20Summary%20Modified) for this release.

# Previous releases #

## Release 0.2-rc1 ##

Released for test: [25/09/2008](http://groups.google.com/group/simal-contributors/browse_thread/thread/7846d35317307437#)


We are pleased to announce the release of Simal v0.2-rc1

Simal is a framework for building project registries. Using Simal you can quickly collate details about projects and participants. This data can then be accessed via a command line tool for scripting, a RESTful API for mashups or a web application for humans.

This release is a complete rewrite of the 0.1 release which was always intended to be a proof of concept. 0.2 now utilises an RDF repository rather than a flat file system. Consequnetly performance is vastly improved in this release.

Key features include:

- Ability to import DOAP from the local filesystem or a remote URL
- AJAX browsers for finding people or projects
- Fully categorised listings
- Export of RDF data models
- RESTful API

# Installing #

Binary releases: [UserQuickStart](UserQuickStart.md)

Source: [GettingStarted](GettingStarted.md)

# Questions and Feedback #

Assistance is available on our mailing lists, which can be found at
http://groups.google.com/group/simal-users (for users) and http://groups.google.com/group/simal-contributors (for those wanting to get involved)

Bug reports and feature requests are also welcome, please submit them to our issue tracker at http://code.google.com/p/simal/issues/list