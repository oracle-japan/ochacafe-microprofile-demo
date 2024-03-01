#!/bin/bash


status() {
  docker ps | grep helidon-mp-demo 
}

start() {
  docker network create helidon-demo-nw
  
  docker run --rm -d --name jaeger \
  -e COLLECTOR_ZIPKIN_HOST_PORT=:9411 \
  -p 6831:6831/udp \
  -p 6832:6832/udp \
  -p 5778:5778 \
  -p 16686:16686 \
  -p 4317:4317 \
  -p 4318:4318 \
  -p 14250:14250 \
  -p 14268:14268 \
  -p 14269:14269 \
  -p 9411:9411 \
  jaegertracing/all-in-one:1.52
  
  docker run --rm -d --name helidon-mp-demo-0 --hostname helidon-mp-demo-0 --network helidon-demo-nw -e mp.config.profile=jaeger -e otel.service.name=helidon-mp-demo-0 -p 8080:8080 helidon-mp-demo
  docker run --rm -d --name helidon-mp-demo-1 --hostname helidon-mp-demo-1 --network helidon-demo-nw -e mp.config.profile=jaeger -e otel.service.name=helidon-mp-demo-1 helidon-mp-demo
  docker run --rm -d --name helidon-mp-demo-2 --hostname helidon-mp-demo-2 --network helidon-demo-nw -e mp.config.profile=jaeger -e otel.service.name=helidon-mp-demo-2 helidon-mp-demo
  docker run --rm -d --name helidon-mp-demo-3 --hostname helidon-mp-demo-3 --network helidon-demo-nw -e mp.config.profile=jaeger -e otel.service.name=helidon-mp-demo-3 helidon-mp-demo
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

