#!/bin/bash

if [ "$REMOTE_REPO_PREFIX" = "" ]; then
  echo "REMOTE_REPO_PREFIX not set."
  exit 1
fi

envsubst < demo/k8s/open-tracing.yaml | kubectl delete  -f -
envsubst < demo/k8s/open-tracing.yaml | kubectl apply  -f -