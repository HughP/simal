The demo server at http://registry.oss-watch.ac.uk can be updated by anyone with the necessary priveledges (e.g. a sudo user) on the demo machine with the following steps.

# Update and Compile the Modules #

```
cd /home/simal
./update.sh
```

This will update all Simal modules from SVN, run all tests and deploy artifacts to the local Maven repository.

# Clean The Demo Data - OPTIONAL #

If you want to reset the demo data follow these steps:

On the Tools page of the demo server select "Remove all data from the repository" - be very **careful** with this, it will delete all data in the server. Only use it if you know what you are doing.

NOTE: you will need to stop the server as described in the next section before proceeding.

Now deploy the approved data using:

```
./deployData.sh
```

This will deploy all the data files we have that are known to work cleanly in the Simal repository. We need to do this because the update command we ran earlier deleted the current demo data. This is done to ensure hte demo server isn't filled up with spam entries, we periodically reset it in this way.

# Stop the currently running process #

```
ps aux | grep maven
```

Note the process ID for the currently running instance of Simal.

```
kill [SIMAL_PROCESS_ID]
```

# Run the Server #

Now we run the server again with:

```
./run.sh
```

That's it, you can log out now.

# Script Sources #

The scripts used on the demo server are provided below. Using these scripts you can set up your own demo server for use locally. In theory you will only need to change the paths at the start of each script, let us know if this is not the case in practice.

## update.sh ##

```
#!/bin/bash

# This script updates Simal from SVN head and insatlls the artifacts into
# the MAVEN repository.

# FIXME: if any of the builds fail the script tries to continue and subsequent
# builds will probably fail. We need to failfast with a useful error message.

export JAVA_HOME=/usr/lib/jvm/java-6-sun/
export MAVEN_HOME=/usr/local/apache-maven/apache-maven-2.0.8/
export PATH=$PATH:$MAVEN_HOME/bin

#
# Update Simal Core
#

cd /home/simal/trunk/uk.ac.osswatch.simal.core
rm -Rf ./simalDOAPFilestore/simal-uploads/
svn up
mvn clean
mvn -Dmaven.test.skip=true install
if [ $? -ne 0 ]; then
    echo ===================================
    echo Error: Installing Simal core failed
    echo ===================================
    exit 1
fi

#
# Update Simal REST
#

cd /home/simal/trunk/uk.ac.osswatch.simal.rest
rm -Rf ./simalDOAPFilestor/simal-uploads/*
svn up
mvn -Dmaven.test.skip=true clean install
if [ $? -ne 0 ]; then
    echo ===================================
    echo Error: Installing Simal rest failed
    echo ===================================
    exit 1
fi

#
# Update Simal Web
#

cd /home/simal/trunk/uk.ac.osswatch.simal.web
rm -Rf ./simalDOAPFilestore/simal-uploads/
svn up
mvn -Dmaven.test.skip=true clean install
if [ $? -ne 0 ]; then
    echo ==================================
    echo Error: Installing Simal Web failed
    echo ==================================
    exit 1
fi

#
# setup local properties file
#

echo simal.rest.baseurl=http\://simal.16degrees.com.au\:8000/simal-rest > /home
/simal/trunk/uk.ac.osswatch.simal.web/local.simal.properties
```

## deploydata.sh ##

```
#!/bin/bash

# This script updates the data held locally from various remote sources
# and deploys them in the local Simal repository

export JAVA_HOME=/usr/lib/jvm/java-6-sun/
export MAVEN_HOME=/usr/local/apache-maven/apache-maven-2.0.8/
export PATH=$PATH:$MAVEN_HOME/bin

cd /home/simal/trunk/rdf/oss-watch
svn up

cd /home/simal/trunk/uk.ac.osswatch.simal.core
mvn -Dmaven.test.skip=true assembly:assembly

# IMPORT Simal data

echo "===================================================================="
echo "Importing Simal RDF"
echo "===================================================================="

java -cp target/simal-core-0.3-dev-jar-with-dependencies.jar uk.ac.osswatch.simal.Simal -properties /home/simal/trunk/uk.ac.osswatch.simal.web/local.simal.properties -d /home/simal/trunk/uk.ac.osswatch.simal.web addxml /home/simal/trunk/uk.ac.osswatch.simal.core/src/main/resources/simal.rdf

if [ $? -ne 0 ]; then
    echo ===========================
    echo Error: Deploying Simal data
    echo ===========================
    exit 1
fi

# IMPORT manually created DOAP files

echo "===================================================================="
echo "Import manually edited data"
echo "===================================================================="

java -cp target/simal-core-0.3-dev-jar-with-dependencies.jar uk.ac.osswatch.simal.Simal -properties /home/simal/trunk/uk.ac.osswatch.simal.web/local.simal.properties -d /home/simal/trunk/uk.ac.osswatch.simal.web addxmldir /home/simal/rdf/oss-watch

if [ $? -ne 0 ]; then
    echo ======================================
    echo Error: Deploying Manually created data
    echo ======================================
    exit 1
fi

# IMPORT JISC "what we do" scraped files

echo "===================================================================="
echo "Import scraped data"
echo "===================================================================="

java -cp target/simal-core-0.3-dev-jar-with-dependencies.jar uk.ac.osswatch.simal.Simal -properties /home/simal/trunk/uk.ac.osswatch.simal.web/local.simal.properties -d /home/simal/trunk/uk.ac.osswatch.simal.web addxmldir /home/simal/trunk/simal/src/documentation/content/rdf/www.jisc.ac.uk

if [ $? -ne 0 ]; then
    echo ==============================
    echo Error: Deploying Scrapted data
    echo ==============================
    exit 1
fi

# IMPORT SKUA related data

echo "===================================================================="
echo "import remote data"
echo "===================================================================="

java -cp target/simal-core-0.3-dev-jar-with-dependencies.jar uk.ac.osswatch.simal.Simal -properties /home/simal/trunk/uk.ac.osswatch.simal.web/local.simal.properties -d /home/simal/trunk/uk.ac.osswatch.simal.web addxml http://www.myskua.org/skua-doap.rdf

if [ $? -ne 0 ]; then
    echo ===========================
    echo Error: Deploying SKUA data
    echo ===========================
    exit 1
fi


java -cp target/simal-core-0.3-dev-jar-with-dependencies.jar uk.ac.osswatch.simal.Simal -properties /home/simal/trunk/uk.ac.osswatch.simal.web/local.simal.properties -d /home/simal/trunk/uk.ac.osswatch.simal.web addxml http://nxg.me.uk/norman/rdf

if [ $? -ne 0 ]; then
    echo =================================
    echo Error: Deploying Norman Gray data
    echo =================================
    exit 1
fi
```

## run.sh ##

```
#!/bin/bash

export JAVA_HOME=/usr/lib/jvm/java-6-sun/
export MAVEN_HOME=/usr/local/apache-maven/apache-maven-2.0.8/
export PATH=$PATH:$MAVEN_HOME/bin

cd /home/simal/trunk/uk.ac.osswatch.simal.web
mvn clean -Djetty.port=80 jetty:run &
```