#!/bin/bash
HTTP_PORT="8081" \
DEVICE_TYPE="http" \
DEVICE_HOSTNAME="localhost" \
DEVICE_LOCATION="bedroom" \
DEVICE_ID="AX3345" \
GATEWAY_TOKEN="smart.home" \
java -jar target/smartdevice-1.0.0-SNAPSHOT-fat.jar ;

