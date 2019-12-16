#!/bin/sh
java -cp target/helidon-demo-mp.jar oracle.demo.ft.FaultToleranceTester --endpoint http://localhost:8080/ft/circuit-breaker $1

