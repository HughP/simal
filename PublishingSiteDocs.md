# Introduction #

The site docs are produced using [Apache Forrest](http://forrest.apache.org), there are a few different ways in which these docs can be published. Currently the only method here is by using the Forrestbot tool, more will follow.

# Prerequisites #

To publish (or even build) the docs you'll need to install [Apache Forrest](http://forrest.apache.org)

# Publishing with Forrestbot #

## Source Files ##

The source files for the documentation are distributed across multiple locations. This is so that documentation for individual modules can be build and distributed independantly of the documentation for other modules. However, the file structure for documentation is the same in each case and is the default file structure found in [Apache Forrest](http://forrest.apache.org).

The `Simal` module contains the "master" content object, that is, the `Simal` module can be used to build all documentation for all Simal modules in a single site. It is this module that we use to create the ]http://simal.oss-watch.ac.uk|documentation site]. Therefore this module will be the focus of these instructions.

## Configuration Files ##

### publish.xml ###

There is a configuration file called `publish.xml` located in the `simal` module.
This contains the basic instructions to tell Forrestbot what to publish and what delivery method to use. Delivery methods include 'svn' 'scp' 'ftp' and others, we are using the SCP method currently.

### deploy.scp.settings ###

The above `publish.xml` file has a setting which point to `deploy.scp.settings`.

It is `critical` that this file is not added to the svn structure as it contains sensitive information that should remain private - in the same way as any other username/password pair would be. For this reason it is included in the `svn:ignore` list.

The contents of this file should be something like :-

```
<?xml version="1.0"?>
<project>
  <property name="deploy.scp.dest" value="user@linux.ox.ac.uk/:/afs/.ox.ac.uk/vhost/simal.oss-watch.ac.uk/docroot"/>
  <!--
  <property name="deploy.scp.keyfile" value="\path\to\simal\id_dsa"/>
  <property name="deploy.scp.passphrase" value="yourpassphrase"/>
  -->
  <property name="deploy.scp.password" value="yourpassword"/>
</project>
```

Note the above settings show keyfile and passphrase settings commented out, if this is the preferred method of connection then uncomment and fill in the correct details.
If you have not connected to the server before using SSH/SCP then do so manually first via the command line so that you can accept the server certificate first.
The password method will only work if your server accepts password authentication.

## Publishing ##

```
forrest -f publish.xml build
```

Should there be a broken internal link in the site the `build` above command will report a failure. If this is the case you will find details of the broken links in `simal/build/site/broken-links.xml`.

Once you have a succesful build you can deploy with:

```
forrest -f publish.xml deploy
```