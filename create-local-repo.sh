#!/bin/bash

WL_HOME=${HOME}/opt/wls1411
WL_T3CLIENT_JAR=${WL_HOME}/wlserver/server/lib/wlthint3client.jar

#WL_CLIENT_JAR=${HOME}/opt/wls12214/wlserver/server/lib/wlclient.jar
#WL_JMSCLIENT_JAR=${HOME}/opt/wls12214/wlserver/server/lib/wljmsclient.jar

mkdir -p m2repo

mvn deploy:deploy-file \
 -Dfile=$WL_T3CLIENT_JAR \
 -Durl=file:./m2repo \
 -DgroupId=oracle.weblogic \
 -DartifactId=wlthint3client \
 -Dversion=14.1.1.0.0 \
 -Dpackaging=jar \
 -DgeneratePom=true

#mvn deploy:deploy-file \
# -Dfile=$WL_CLIENT_JAR \
# -Durl=file:./m2repo \
# -DgroupId=oracle.weblogic \
# -DartifactId=wlclient \
# -Dversion=12.2.1.4.0 \
# -Dpackaging=jar \
# -DgeneratePom=true

#mvn deploy:deploy-file \
# -Dfile=$WL_JMSCLIENT_JAR \
# -Durl=file:./m2repo \
# -DgroupId=oracle.weblogic \
# -DartifactId=wljmsclient \
# -Dversion=12.2.1.4.0 \
# -Dpackaging=jar \
# -DgeneratePom=true

