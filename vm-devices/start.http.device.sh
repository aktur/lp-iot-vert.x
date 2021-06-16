#!/bin/bash

multipass --verbose exec devices -- bash << EOF
cd app

HTTP_PORT="8098" \
DEVICE_TYPE="http" \
DEVICE_LOCATION="garden" \
DEVICE_ID="BVOP34" \
GATEWAY_TOKEN="smart.home" \
GATEWAY_DOMAIN="gateway.home.smart" \
GATEWAY_PORT="9090" \
java -jar smartdevice-1.0.0-SNAPSHOT-fat.jar ;
EOF

