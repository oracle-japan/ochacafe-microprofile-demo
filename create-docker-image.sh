#!/bin/bash

docker build -t helidon-demo-mp .

if [ "$REMOTE_REPO_PREFIX" = "" ]; then
  echo "REMOTE_REPO_PREFIX not set."
else
  docker tag helidon-demo-mp ${REMOTE_REPO_PREFIX}helidon-demo-mp
  docker push ${REMOTE_REPO_PREFIX}helidon-demo-mp
fi

