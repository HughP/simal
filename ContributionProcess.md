Simal is an open development project. This means that we accept contrbitutions from anyone. However, this does not mean that we will automatically include any contribution in our releases. This document provides a checklist to allow people to maximise the chances of their contribution being accepted by the project.

The document is split into two sections, "Small Contributions" and "Major Contributions". For small contributions (e.g. spelling corrections or minor bug fixes) we have a lightweight process. However, for major contributions (e.g. new documents or features) we have a much more rigorous process.

Small contributions are those unlikely to contain any intellectual properties, for example, a spelling correction, documentation clarification or small bug fix. That is, there are no significant legal concerns with accepting the contribution without validating the legal right of the author to make the contribution.

As author you need not concern yourself with whether your contribution is a small or major contribution. The difference is in our handling of copyrights in the code. As contributor you can follow the contributor checklist below. If necessary we will contact you with respect to copyright licencing for major contributions (described in more detail below).

# Contributor Checklist #

  1. ensure all tests in all modules pass
    * run mvn test in each of the modules higher in the Simal stack, that is, if you change something in core you need to run all tests, but changing something in  the web module only requires you to run tests in that module)
  1. create a [patch for submission](CreatePatch.md)
  1. find the appropriate [issue in the tracker](http://code.google.com/p/simal/issues/list) (or create a new one if necessary)
  1. attach the patch to the issue with a comment explaining its purpose

**NOTE** by submitting the patch to the issue tracker you indicate that you transfer all copyright in the contribution to the University of Oxford. If you do not wish to transfer copyright then you must use the Major Contributions process below.

Once you have submitted the patch it will enter into our PatchReviewProcess

# Major Contributions #

A major contribution is a a significant change such as new feature, major bug fix or substantial documentation contribution. It is likely to contain some intellectual property and therefore will require that a licence to use the copyright contained in the contribution is granted to the University of Oxford.

To do this we require that all major contributions are accompanied by a statement granting us a licence to use the copyright. Note this statement does not transfer copyright to the University of Oxford, it merely grants us the right to use your copyrighted contribution. You can therefore continue to excercise the copyrights in your contribution in any way you desire.

OSS Watch has a useful document describing the purpose of [Contribtor Licence Agreements](http://www.oss-watch.ac.uk/resources/cla.xml).

At the time of writing we have not finalised our CLA, for the current draft please see IndividualContributorLIcenceAgrement in our wiki.

Should you want to make a major contribution please follow the checklist above but indicate that you do not wish to transfer copyright and request that we draw up a CLA for you instead.