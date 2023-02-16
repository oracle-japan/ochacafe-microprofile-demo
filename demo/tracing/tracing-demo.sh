#!/bin/bash


status() {
  docker ps | grep helidon-mp-demo 
}

start() {
  docker network create helidon-demo-nw
  
  docker run --rm -d --name jaeger \
  -e COLLECTOR_ZIPKIN_HTTP_PORT=9411 \
  -p 5775:5775/udp \
  -p 6831:6831/udp \
  -p 6832:6832/udp \
  -p 5778:5778 \
  -p 16686:16686 \
  -p 14268:14268 \
  -p 9411:9411 \
  --hostname jaeger \
  --network helidon-demo-nw \
  jaegertracing/all-in-one:1.13
  
  docker run --rm -d --name helidon-mp-demo-0 --hostname helidon-mp-demo-0 --network helidon-demo-nw -e tracing.service=helidon-mp-demo-0 -p 8080:8080 helidon-mp-demo
  docker run --rm -d --name helidon-mp-demo-1 --hostname helidon-mp-demo-1 --network helidon-demo-nw -e tracing.service=helidon-mp-demo-1 helidon-mp-demo
  docker run --rm -d --name helidon-mp-demo-2 --hostname helidon-mp-demo-2 --network helidon-demo-nw -e tracing.service=helidon-mp-demo-2 helidon-mp-demo
  docker run --rm -d --name helidon-mp-demo-3 --hostname helidon-mp-demo-3 --network helidon-demo-nw -e tracing.service=helidon-mp-demo-3 helidon-mp-demo
}

stop() {
  docker stop helidon-mp-demo-0
  docker stop helidon-mp-demo-1
  docker stop helidon-mp-demo-2
  docker stop helidon-mp-demo-3
  docker stop jaeger
  docker network rm helidon-demo-nw
}

case "$1" in
    'start')
            start
            ;;
    'stop')
            stop
            ;;
    'status')
            status
            ;;
    *)
            echo
            echo "Usage: $0 { start | stop | status }"
            echo
            exit 1
            ;;
esac

exit 0

