#!/bin/sh
java -cp target/helidon-mp-demo.jar com.example.ft.FaultToleranceTester --endpoint http://localhost:8080/ft/circuit-breaker $1

