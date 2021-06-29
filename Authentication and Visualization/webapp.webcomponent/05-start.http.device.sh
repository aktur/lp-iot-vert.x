#!/bin/bash

#multipass start devices

multipass --verbose exec devices -- bash << EOF
cd app

HTTP_PORT="8083" \
DEVICE_TYPE="http" \
DEVICE_LOCATION="bedroom" \
DEVICE_ID="RTV786" \
DEVICE_HOSTNAME="devices.home.smart" \
GATEWAY_TOKEN="smart.home" \
GATEWAY_DOMAIN="gateway.home.smart" \
GATEWAY_PORT=9090 \
java -jar smartdevice-1.0.0-SNAPSHOT-fat.jar &
EOF
