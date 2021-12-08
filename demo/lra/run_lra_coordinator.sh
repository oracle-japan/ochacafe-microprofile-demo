#!/bin/bash

docker run --rm --name lra-coordinator -d \
  -p 8070:8070 \
  --network="host" \
  helidon/lra-coordinator

