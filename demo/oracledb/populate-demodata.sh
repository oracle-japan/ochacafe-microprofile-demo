#!/bin/bash

CWD=$(dirname $0)
SOURCE_DIR=$(readlink -f $CWD)
echo $SOURCE_DIR

PDB=PDB1
PASSWORD=OCHaCafe6834
CONTAINER_NAME=oracledb

docker exec -i --user oracle $CONTAINER_NAME \
  sqlplus SYS/$PASSWORD@PDB1 AS SYSDBA \
  < $SOURCE_DIR/populate-demodata.sql
