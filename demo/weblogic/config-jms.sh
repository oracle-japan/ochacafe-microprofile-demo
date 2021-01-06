#!/bin/bash

CONTAINER_NAME=wls1411
CWD=$(dirname $0)
SOURCE_DIR=$(readlink -f $CWD)
echo $SOURCE_DIR

wget -O $SOURCE_DIR/weblogic-deploy.zip https://github.com/oracle/weblogic-deploy-tooling/releases/download/release-1.9.8/weblogic-deploy.zip
docker exec -w /u01/oracle/properties $CONTAINER_NAME unzip weblogic-deploy.zip
rm $SOURCE_DIR/weblogic-deploy.zip

# update domain
echo ">>> configuring jms resources ..."
docker exec -w /u01/oracle/properties $CONTAINER_NAME weblogic-deploy/bin/updateDomain.sh -oracle_home /u01/oracle -domain_type WLS -domain_home /u01/oracle/user_projects/domains/base_domain -model_file /u01/oracle/properties/config-jms.yaml
docker exec $CONTAINER_NAME rm -rf /u01/oracle/properties/weblogic-deploy

# restart server
echo ">>> restarting server ..."
docker restart $CONTAINER_NAME
