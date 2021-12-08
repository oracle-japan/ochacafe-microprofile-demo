#!/bin/bash

docker build -f $(dirname $0)/Dockerfile_lra-coordinator -t helidon/lra-coordinator .

if [ "$REMOTE_REPO_PREFIX" != "" ]; then
  docker tag helidon/lra-coordinator ${REMOTE_REPO_PREFIX}helidon/lra-coordinator
  docker push ${REMOTE_REPO_PREFIX}helidon/lra-coordinator
fi

