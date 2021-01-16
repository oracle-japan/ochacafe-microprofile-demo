#!/bin/bash

CONTAINER_NAME=oracledb

CWD=$(dirname $0)
SOURCE_DIR=$(readlink -f $CWD)
echo $SOURCE_DIR

docker run -d --env-file $SOURCE_DIR/oracledb.env  \
  -p 1521:1521 -p 5500:5500 \
  -it --name $CONTAINER_NAME --shm-size="4g" \
  container-registry.oracle.com/database/standard
