#!/bin/sh
java -cp target/helidon-mp-demo.jar oracle.demo.ft.FaultToleranceTester --endpoint http://localhost:8080/ft/bulkhead $1

