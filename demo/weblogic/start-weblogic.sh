#!/bin/bash

CONTAINER_NAME=wls1411

CWD=$(dirname $0)
SOURCE_DIR=$(readlink -f $CWD)
echo $SOURCE_DIR
docker run -d -p 7001:7001 -p 9002:9002 --name $CONTAINER_NAME \
  -v $SOURCE_DIR:/u01/oracle/properties \
  container-registry.oracle.com/middleware/weblogic:14.1.1.0-dev-11
