A patch is a record of changes made to the source of a project. Patches (also known as diffs) are how contributions are made to open source projects. For a full discussion of patches see the OSS Watch brifing note on [Software Patches](http://www.oss-watch.ac.uk/resources/softwarepatch.xml).

This document outlines how to create a patch and submit it to the Simal project. For a more complete discussion of the process see the OSS Watch document on [creating patches](http://wiki.oss-watch.ac.uk/MakeASimplePatch).

For the purposes of this document lets assume that you have made a change to the Simal Web application such as that described in the how to on "adding a new widget" (**TODO** write that document). This means you have made some changes to existing files and you have created some new files.

Since our code evolves at a reasonable pace it therefore makes sense to submit your patches often and early. Don't worry if they are not feature complete, it's best to submit you patch as soon as it does something useful.

# The easy way: edit online #
If the change you want to make is small and contained within one file, you can use Google Code's online editor:
  1. [Navigate to](http://code.google.com/p/simal/source/browse/) the page
  1. Click "Edit File"
  1. Depending on your level of access, you'll either modify the file directly or submit a patch to the developers.

You can also edit the wiki this way. [Navigate to the "wiki" directory](http://code.google.com/p/simal/source/browse/#svn%2Fwiki), then proceed as above.

# A Note command line and GUI tools #

This document will describe how to create a patch using command line tools on Windows. There are a number of graphical tools available for SVN (the version control system we use), it is therefore not possible for us to describe how to use them all. However, understanding how to do things with the command line should enable you to work with any graphical tool.

# Prerequisites #

  * Your new feature has been documented clearly
  * Your new feature is adequately tested
  * All tests pass

# Preparation #

## Setting the working directory ##

Ensure you are in the correct working directory e.g.:

```
cd %SIMAL_HOME%\uk.ac.osswatch.simal.web
```

## Updating Local Files ##

Now you need to make sure your local files are up to date with the latest in SVN.

```
svn update
```

After some time working you will recieve a message of the form:

```
At revision xxxxxx.
```

Where "xxxxxx" is the revision number. If there is not ouptut prior to this line then all is well. If there is output prior to this line you must look to see if any of the files are in conflict, this is indicated with a "C" at the start of the line, for example:

```
C foo
```

If you have any files in conflict you need to resolve these conflicts immediately. How to do this is outside the scope of this document, see the [Subversion book](http://svnbook.red-bean.com/en/1.1/ch03s05.html#svn-ch-3-sect-5.4) for more help.

**NOTE** if you use Linux/OSX/Unix/Cygwin you can run the command `svn update | grep "C\ "` to quickly check for conflicts.

## Identify Appropriate Changes ##

Next you need to ensure that there are no local changes that are not a part of the intended contribution. Run the command:

```
svn status
```

This will tell you the status of all your local files that have changed. The output is a six character symbol of the status followed by a space and the name of the file in question. At this stage we are only really interested in the first column. In particular we are interested in the following:

```
'A' Item is scheduled for Addition. That is it will be a part of the patch unless you avoid including it.
'D' Item is scheduled for Deletion. That is it will be deleted by the patch unless you avoid doing so.
'M' Item has been modified. That is changes will be in the patch unless you avoid including them.
'R' Item has been replaced in your working copy. This means the file was scheduled for deletion, and then a new file with the same name was scheduled for addition in its place.
'C' The contents (as opposed to the properties) of the item conflict with updates received from the repository. You shouldn't see any of these as we looked at them in the previous stage.
'?' Item is not under version control. This may be an item that is part of your work and needs adding. See below.
'!' Item is missing (e.g. you moved or deleted it without using svn). This also indicates that a directory is incomplete (a checkout or update was interrupted). This should never occur if you followed the previous step.
```

**TODO** describe what to do with '>' files

**NOTE** if you use Linux/OSX/Unix/Cygwin you can run the command `svn update | grep "?\ "` to quickly check for files that may need to be added.

For information about the other symbols in the status see the [Subversion Book](http://svnbook.red-bean.com/en/1.4/svn-book.html#svn.ref.svn.c.status).

## Create the patch ##
Now tell Subversion to summarise all these changes in one file:

```
svn diff > myPatch.diff
```

## Send it in ##
If your patch resolves an issue in the Issues register, attach it as a file there. Otherwise, create a new issue of type 'Patch' and attach it there. Describe in it, as fully as possible, the intended purpose of this patch.

Note that by submitting a patch to this issue tracker you agree to assign copyright of your work to the University of Oxford. If this is not acceptable to you please contact the developer list at simal-contributors@googlegroups.com before submitting your patch as you may need to sign a Contributor Licence Agreement in order to grant a licence to the University.

# Further Reading #

The Subversion book has a chapter that expands on the [basic work cycle](http://svnbook.red-bean.com/en/1.1/ch03s05.html) with version control.