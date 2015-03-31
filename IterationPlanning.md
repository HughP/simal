This page provides a convenient way of keeping track of the focus of each iteration.



If you have a specific need that is not currently a part of our planned development cycles please feel free to provide a patch at any time, we will do our best to review it and, where appropriate, include it in the next iteration. If you are a committer you can, of course, add whatever you want into our iteration planning process. The ContributionProcess document has more details.

# Iteration Planning #

We use iterations of two weeks in a cycle of 4 iterations. At the end of each iteration a new build will be deployed on http://registry.oss-watch.ac.uk. We call this a 'soft release'. So a soft release is performed every two weeks.

At the end of every third iteration a candidate release is created, this is tested and released during the fourth iteration.

The first week of every fourth iteration will be used to test the candidate binary release and fix critical bugs on the release branch. The second week of every fourth iteration will then be mainly used for creating the final binary release itself, which will be signed by as many developers as possible.

During the fourth iteration it is possible to commit changes to the trunk, but they will not be included in the release at the end of the iteration.

In the figure below, the whole cycle of four iterations is displayed:

![http://simal.googlecode.com/svn/wiki/ReleaseManagement.attach/releaseProcessTimeline.gif](http://simal.googlecode.com/svn/wiki/ReleaseManagement.attach/releaseProcessTimeline.gif)

# Schedule #

| **#** | **Iteration Focus** | **Start Date** | **End Date** | **Release Type** |
|:------|:--------------------|:---------------|:-------------|:-----------------|
|3 | Make project details editable  |2011-02-07|2011-02-20|Candidate Release|
|4 | Binary Release |2011-02-21|2011-03-06| **Binary Release** |
|5 | Patching and signing Release |2011-03-07|2011-03-20| **Binary Release** |


# Useful Links #

  * Issues [slotted](http://code.google.com/p/simal/issues/list?can=2&q=label:slotted) for current iteration
  * Issues being [worked on](http://code.google.com/p/simal/issues/list?can=2&q=status:Started)
  * Issues marked as [fixed](http://code.google.com/p/simal/issues/list?can=7&q=&colspec=ID+Type+Status+Priority+Milestone+Summary&x=priority&y=milestone&cells=tiles) (if verified these will be included in the next binary release)
  * ReleaseManagement process