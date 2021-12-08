#!/bin/bash

docker build -f $(dirname $0)/Dockerfile_lra-coordinator-narayana -t helidon/lra-coordinator-narayana .

if [ "$REMOTE_REPO_PREFIX" != "" ]; then
  docker tag helidon/lra-coordinator-narayana ${REMOTE_REPO_PREFIX}helidon/lra-coordinator-narayana
  docker push ${REMOTE_REPO_PREFIX}helidon/lra-coordinator-narayana
fi

