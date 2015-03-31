# Introduction #

Currently, Simal supports the following imports:

  * Ohloh
  * Ping the Semantic Web
  * PIMS

# Ohloh #

[Ohloh](http://www.ohloh.net) is a project registry that contains over 400,000 projects. It has a publicly accessible API that is used by Simal. In order to make this work you will need an [API key](http://www.ohloh.net/accounts/me/api_keys/new) and to obtain one you will have to [register](https://www.ohloh.net/accounts/new) on the Ohloh website.

In Simal, the Ohloh API key needs to be set as a property named `ohloh.api.key`. It can be added to the properties file or passed as a runtime system property, eg. ` -Dohloh.api.key=X0dsHUJ716532hLgvqGw`.

The form to add a project from Ohloh is on the Tools page.

# Ping the Semantic Web #

[Ping the Semantic Web](http://pingthesemanticweb.com/) (PTSW) is a service where you can register RDF documents. Simal has a built-in use of PTSW. In order to get this working in your own environment you will need to register your IP address on the [PTSW website](http://pingthesemanticweb.com/signup.php).

After registration you can download the PTSW archive of all RDF documents. There is unfortunately no way of only downloading DOAP projects, but if you run the Python script below on the big archive XML it will extract the relevant RDF documents:

```
rdfdocument = ''
outputFile = open('doap_pings.xml','w')
outputFile.write('<pingthesemanticwebUpdate version="1.4" updated="">\n')
for line in open('ptsw_all_pings.xml', 'r'):
  rdfdocument += line
  if line.find('/>') != -1:
    if rdfdocument.find('http://usefulinc.com/ns/doap#') != -1:
      outputFile.write(rdfdocument)
    rdfdocument = ''
outputFile.write('</pingthesemanticwebUpdate>')
outputFile.close()
```

Currently (July 2010) there are over 19,000 projects in the PTSW. After these have been imported you can set the PTSW importer on the Tools page (under "Update from PTSW") which will lead to an hourly update from PTSW. However, if the update has been turned off for more than 5 days there will be an error message stating: `Last ping is older than 5 days. Please synch back with Ping the Semantic Web using the core dump available at http://pingthesemanticweb.com/account.php`. There is no requirement to actually import the core dump in Simal, so you can simply download and ignore the core dump and subsequently have the task run to only update/import the latest pings from PTSW. This is specifically useful because the total number of projects is too high for Simal's likening.

# PIMS #

On the Tools page you can also import updates from the PIMS database, provided these are available. Those are three separate Excel files that need to be imported separately.