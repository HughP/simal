When a patch is submitted to the project it enters our patch review process. This process is designed to ensure that we maintain quality within the Simal code project and, where necessary, give useful feedback to the contributor about their contribution.

For [small patches](ContributionProcess.md) the following process is followed.

# Review #

  1. visually examine the patch
    1. check the legal status of the contributions, do we need a CLA for this contribution?
      1. If a CLA is required and one is not on file then contact the author
    1. check style
    1. check readability
    1. check new features are documented
    1. check status.xml has been appropriately updated
      1. contributor entry (do not reject for this reason, instead note it in the issue)
      1. change record
  1. check diff has been created correctly and can be applied
    1. if there is a problem with the diff report it to the contributor and ask for resubmission
  1. Report status to the contributor via the issue tracker, status is one of:
    1. rejected - give good reasons and ask for an updated patch
    1. passed review - this indicates that someone needs to apply and test the patch

# Testing #

  1. apply the patch locally
  1. check expected functionality change
  1. check all tests pass
  1. if there is problem with any functionality or testing:
    1. report to the contributor via the issue tracker and ask for resubmission
    1. revert local changes
  1. if all tests pass commit the change with full credit to the author in the commit message and a mention of the issue the patch is attached to
  1. update the issue and thank the contributor