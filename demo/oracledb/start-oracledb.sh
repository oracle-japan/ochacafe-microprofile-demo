#!/bin/bash

CONTAINER_NAME=oracledb
ORACLE_SID=ORCL
ORACLE_PDB=PDB1
ORACLE_PWD=OCHaCafe6834

CWD=$(dirname $0)
SOURCE_DIR=$(readlink -f $CWD)
echo $SOURCE_DIR

#docker run -d --env-file $SOURCE_DIR/oracledb.env  \
#  -p 1521:1521 -p 5500:5500 \
#  -it --name $CONTAINER_NAME --shm-size="4g" \
#  container-registry.oracle.com/database/standard


docker run -d -it --name $CONTAINER_NAME \
  -p 1521:1521 -p 5500:5500 \
  --shm-size="4g" \
  -e ORACLE_SID=$ORACLE_SID \
  -e ORACLE_PDB=$ORACLE_PDB \
  -e ORACLE_PWD=$ORACLE_PWD \
  container-registry.oracle.com/database/enterprise:19.3.0.0
