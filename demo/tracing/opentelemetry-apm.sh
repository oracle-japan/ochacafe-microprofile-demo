#!/bin/bash

OCS_APM_ENDPOINT=
OCS_APM_KEY=
JAVA_AGENT_JAR=

export JAVA_TOOL_OPTIONS="-javaagent:$JAVA_AGENT_JAR"

export OTEL_AGENT_PRESENT=true

export OTEL_SERVICE_NAME="helidon-mp-demo"

export OTEL_TRACES_EXPORTER=otlp
export OTEL_METRICS_EXPORTER=none
export OTEL_LOGS_EXPORTER=none

export OTEL_EXPORTER_OTLP_TRACES_PROTOCOL=http/protobuf
export OTEL_EXPORTER_OTLP_TRACES_ENDPOINT="$OCS_APM_ENDPOINT/20200101/opentelemetry/private/v1/traces"
export OTEL_EXPORTER_OTLP_TRACES_HEADERS="Authorization=dataKey $OCS_APM_KEY"

echo $OTEL_EXPORTER_OTLP_TRACES_ENDPOINT
echo $OTEL_EXPORTER_OTLP_HEADERS

java -jar $(dirname $0)/../../target/helidon-mp-demo.jar 

