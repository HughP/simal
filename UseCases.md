## Introduction ##

This page describes use cases for Simal.

## UC1 - Submit project to Simal instance using a web form ##

High-level scenario:
  1. User goes to a web page where there is a form to enter data about a software project
  1. User fills in the form and hits the submit button
  1. If not all checks are ok a message will be displayed so the user can correct those
  1. If all checks are ok an RDF/XML document will be created and submitted to a running Simal instance
  1. The user gets to view the RDF/XML contents to keep for their records
  1. If the submission to the running Simal instance is successful, ie. results in the project being added to the registry, the user is presented with a URL where the project details can be seen on the running Simal instance
  1. If the submission is unsuccessful, the user is presented with an error message stating in as much detail as possible what went wrong

Technical details:
  1. The web form is a Wookie widget which can be embedded in one of Simal pages as well as in any other web page.
  1. There is a running Wookie server that serves this Wookie widget
  1. There may be different versions of the form for different kinds of projects. Eg. JISC projects require some data that is not applicable to other kinds of projects.
  1. Client-side JavaScript checks are performed after form submission eg. to check if all required fields are filled in, if URLs and email addresses are valid, etc.
  1. The RDF/XML document is created client-side using JavaScript and POST-ed to the running Simal instance's REST API.
  1. Simal processes the RDF/XML. If the operation is successful, ie. a new project is created, a stable URL to the detail page of the added project is returned. If the operation is unsuccessful, an error message is returned stating the nature of the error.
  1. The result from Simal is displayed to the user
  1. The same RDF/XML document that is sent to Simal is displayed to the user.