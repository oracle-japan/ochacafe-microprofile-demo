#!/bin/bash

CWD=$(dirname $0)
SOURCE_DIR=$(readlink -f $CWD)
echo $SOURCE_DIR

PDB=PDB1
PASSWORD=OCHaCafe6834
CONTAINER_NAME=oracledb

docker start $CONTAINER_NAME

sleep 10

CONTAINER_ORACLE_HOME=$(docker exec $CONTAINER_NAME bash -c 'echo -n "$ORACLE_HOME"')

echo "alter database open;" | \
  docker exec -i --user oracle $CONTAINER_NAME \
  $CONTAINER_ORACLE_HOME/bin/sqlplus SYS@$PDB/$PASSWORD AS SYSDBA
