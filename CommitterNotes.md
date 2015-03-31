# Commit log #

The commit log is a very important tool in our IP and Issue management chain. All commit logs should be in the form of:

```
Your commit log message descriptive text...

COMMAND-LINE
ISSUE-FIELD-UPDATE*
COMMENT-TEXT...
```


## Log Message ##

All log messages should be concise and clear. Bear in mind that reviewers want to be able to see at a glance what the patch is intended to do.

If your commit closes and issue then you should include "(fixes issue ####}", for example:

```
Ensure we check for foo when we do bar (fixes issue 123)
```

### Credits ###

If the patch has been supplied by another contributor then you are taking responsibility for that commit, in other words you must have fully reviewed the patch. You must also credit them by name and reference the issue from your commit message. For example:

```
Ensure we check for foo when we do bar. Thanks to Jane Doe. (fixes issue 123)
```

## Requesting a review ##

We operate a commit then review policy for committers. All commits result in a diff email been sent to the list which should be reviewed by other committers.

If you want to explicitly request a code review you can do so by simply include "new review" in the "COMMAND\_LINE" of your commit log, for example:

```
Ensure we check for foo when we do bar. (fixes issue 123)

new review
```

This will automatically create a new issue of type 'review'.

## Updating issues ##

You can (and should) update issues from commit messages using "update issue ###". For example:

```
Ensure we check for foo when we do bar. (fixes issue 123)

new review
update issue 999
Applied patch to check foo in issue 123, once review is complete we can proceed with this issue.
```

# Further Reading #

  * [Integrating the issue tracker with version control](http://code.google.com/p/support/wiki/IssueTracker#Integration_with_version_control)