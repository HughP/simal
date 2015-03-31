This document describes how to build a soft release and a binary release. For a timetable of releases see the IterationPlanning page.

# Releases, iterations and timelines #

We use iterations of two weeks in a cycle of 4 iterations. At the end of each iteration a new build will be deployed on http://registry.oss-watch.ac.uk. We call this a 'soft release'. So a soft release is performed every two weeks.

At the end of every third iteration a release branch will be created and a candidate binary release is created from that branch. This means that every third iteration must assign sufficient velocity points to the release process to ensure this process is fully implemented and all pre-requisites that are described below are met. We cannot cut corners on this as this is where our final quality control checks are performed.

The first week of every fourth iteration will be used to test the candidate binary release and fix critical bugs on the release branch. The second week of every fourth iteration will then be mainly used for creating the final binary release itself, which will be signed by as many developers as possible. At the end of the fourth iteration the binary release will be made available on the Google Code website and the public registry website will be updated. During the fourth iteration it is possible to commit changes to the trunk, but they will not be included in the release at the end of the iteration.

In the figure below, the whole cycle of four iterations is displayed:

![http://simal.googlecode.com/svn/wiki/ReleaseManagement.attach/releaseProcessTimeline.gif](http://simal.googlecode.com/svn/wiki/ReleaseManagement.attach/releaseProcessTimeline.gif)

This document describes how to build a soft release and a binary release.

# Keeping the community informed #

The following are the minimal communications that have to take place to keep to the community informed:

| **When** | **What** | **To whom** |
|:---------|:---------|:------------|
| Two weeks before creating release branch | Intention to create release branch in 14 days | simal-contributors@googlegroups.com |
| Three days before creating release branch | Intention to create release branch in 3 days | simal-contributors@googlegroups.com |
| Day before creating release release branch | Intention to create release branch in 1 day | simal-contributors@googlegroups.com |
| After release branch created | Release branch created | simal-contributors@googlegroups.com |
| Candidate binary release uploaded | Binary release candidate available for testing | simal-contributors@googlegroups.com <br /> simal-users@googlegroups.com |
| Week after candidate binary release availability | Vote for final release to be built | simal-contributors@googlegroups.com |
| After voting period | Announce voting results; build final release | simal-contributors@googlegroups.com  <br /> simal-users@googlegroups.com |
| Final binary release uploaded | Announce availability of final release | simal-contributors@googlegroups.com <br /> simal-users@googlegroups.com |

# Soft release (1st, 2nd and 4th iteration) #

A soft release is created at the end of every iteration. However, the third iteration is a special case because it contains the candidate binary release. See the next section for details. For every other iteration we follow the process defined here. The following conditions must be met before creating the soft release:

  * Check that there are **no critical issues** from the code audit tools (see CodeAuditing)
  * All [unverified issues](http://code.google.com/p/simal/issues/list?can=7&q=&colspec=ID+Type+Status+Priority+Milestone+Summary&x=priority&y=milestone&cells=tiles) have been verified by someone other than the owner of that issue

If these condition are met, all that is done to perform the soft release is an update of the public registry website (http://registry.oss-watch.ac.uk). See UpdatingDemoServer for details on how to do that.

# Candidate binary release (3rd iteration) #
During the third iteration the focus is on refactoring, testing and bug fixing in order to ensure the candidate release is ready and all pre-requisites are met when the code freeze starts at the end of the third iteration.

## Pre-Requisites ##

Before a candidate binary release can be made a number of requirements must be met:

  * Check all CodeAuditing reports are satisfactory (satisfactory means there are **no warnings** from the code audit tools)
  * All [unverified issues](http://code.google.com/p/simal/issues/list?can=7&q=&colspec=ID+Type+Status+Priority+Milestone+Summary&x=priority&y=milestone&cells=tiles) have been verified by someone other than the owner of that issue
  * All source code has been formatted according to the project's coding standards
  * All documentation has been updated to reflect the new features in this release

## Determine release version number ##

The version numbers of Simal releases are of the form `x.y.z` or `<major>.<minor>.<micro>`. The `<major>` version number is only increased when a significan amount of new functionality is added. In this case the new release version number will be `(x+1).0.0`. If this is the case, the scope of the release must be agreed upon by the community using the voting system on the mailing list.
The `<minor>` version number is only increased when some functionality is added to the new release, in which case the new release version number will be `x.(y+1).0`. When only bugfixes are part of the new release there will be a so-called point release. The version number of the new release in this case will be `x.y.(z+1)`. Currently, all components (core, rest and webapp) get the same version number.

## Create a release branch in SVN ##

We must build a release candidate for testing. To do that, we first create a branch for the new release. Everything for release x.y.z., including the final release for this version, will then be done from this branch. You can create a branch with a command similar to this one:

```
svn copy https://simal.googlecode.com/svn/trunk https://simal.googlecode.com/svn/branches/x.y.z -m "Create branch for the x.y.z release"
```

Send a mail to the developer list after the branch is created stating that the release branche is created so that the current status of the trunk will be the scope of the release.

## Change src version numbers in the trunk ##

We must now set up SVN trunk for development of the next release. To avoid confusion this should be done immediately after the release branch is created. We set up the trunk for the next release by correctly setting the version number for all modules.
Usually this will mean incrementing the minor version number and setting the micro version number to '0'.

  * in `pom.xml` change `<version>x.y.z</version>` to `<version>x.(y+1).0-SNAPSHOT</version>`
  * in `src/main/resources/default.simal.properties` change `simal.version=x.(y+1).0-SNAPSHOT`
  * in `status.xml` add a new `<release>` element

In addition we need to change the dependency entries for other Simal modules in both the rest and webapp modules.

## Change version numbers to reflect release build ##

All subsequent changes need to be done from the branch. Therefore, the next step is to ensure your local copy is one from the release branch. You could for instance checkout the release branch to a fresh workspace. Next, we need to correctly set the version number. Usually this will mean changing the postfix from "-SNAPSHOT" to "-rc1" in the pom.xml of all the modules and possibly changing the version number (usually when a point release will be made):

  * in `pom.xml` change `<version>x.y.z[-POSTFIX]</version>`
  * in `src/main/resources/default.simal.properties` change `simal.version=x.y.z[-POSTFIX]`
  * in `status.xml` change the version number and release date to the release to be built.

In addition we need to change the dependency entries for other Simal modules in both the rest and webapp modules.

At this early stage we will release all Simal modules simultaneously and therefore we need to change the `pom.xml` as described in each module (there is currently no `default.simal.properties` file in modules other than core).

Commit these changes to SVN.

## Build the Binaries ##

For each of the modules build the binaries as follows:

```
cd uk.ac.osswatch.ac.uk.simal.core
mvn clean install
mvn assembly:assembly

cd ../uk.ac.osswatch.ac.uk.simal.rest
mvn clean install
mvn package

cd ../uk.ac.osswatch.ac.uk.simal.web
mvn clean package
```

Note that the process of building the binaries will run all automated tests against the code.

## Upload to Google Code ##

Upload the binaries of the candidate binary release to the downloads section of Google Code, making them a featured download.

## Ensure documentation is published ##

Rebuild and [publish the docs](PublishingSiteDocs.md).
**Note:** This step is currently suspended until decisions have been made on the new CMS. See [discussion on the mailing list](http://groups.google.com/group/simal-contributors/browse_thread/thread/7bc0d763fcdc56c1).

## Ensure the demo server is running the latest release ##

[Update the version of Simal](UpdatingDemoServer.md) on the demo server

## Update the Release Notes ##

The ReleaseNotes are used to announce a new release. They are held in the wiki and are used in email and blog announcements.

## Lift Code Freeze and Announce the new Release ##

The code freeze on trunk can now be lifted as the release is tagged in Subversion.

The release should be announced on the user and contributor lists using the ReleaseNotes.

# Steps towards the final binary release (4th iteration) #

During every fourth iteration, the candidate binary will be tested and patched if necessary from the release branch. Changes committed to the trunk during this iteration will not be included in the binary release.

## Test ##

Each of the binaries should be tested by as many people, on as many platforms as possible. For each reported test failure a decision must be made whether the issue will need to be patched in a patch release or if it can wait until the next release.

More details on how to test are included in:

  * TestingCoreReleases
  * TestingRestReleases
  * TestingWebReleases

## Patch the release when necessary ##

When one or more critical errors are discovered a patch release needs to be created from the release branch. Patch the critical errors on the branch and create a release from that branch following the procedure in this document, incrementing the rc number. Merge the changes from the branch to the trunk to assure the trunk includes these patches.

## Vote ##

When no (more) critical errors are discovered on the last release candidate during the testing phase we can now proceed with getting approval for the release.

A vote on the release is called on the contributor mailing list.

Assuming that the vote passes we can proceed. If it does not pass the reason(s) for the negative votes must be addressed. The result of the voting is announced on the contributor and user mailing lists.

## Create and sign final binary release ##

The final binary release is created on the release branch. The only difference between the final release and the last release candidate is the version number.

The process on signing the release is described in SigningReleases.

## Ensure the demo server is running the latest release ##

[Update the version of Simal](UpdatingDemoServer.md) on the demo server

## Upload to Google Code ##

Upload the binaries of the binary release to the downloads section of Google Code, making them a featured download. Remove the release candidate.

If this is a point release previous point releases should be deleted if possible (they use up the storage quota and can be rebuilt from SVN if people need to do so).

Deprecate the previous release and remove the featured downloads lable from it (but do not delete the release).

## Announce availability ##
Announce the availability of the release on the user and developer mailing lists.