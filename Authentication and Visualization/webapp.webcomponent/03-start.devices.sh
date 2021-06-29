#!/bin/bash

multipass start devices

multipass --verbose exec devices -- bash << EOF
cd app

DEVICE_TYPE="mqtt" \
DEVICE_LOCATION="attic" \
MQTT_CLIENT_ID="mqtt_001" \
DEVICE_ID="001" \
MQTT_PORT="1883" \
MQTT_HOST="things.home.smart" \
MQTT_TOPIC="house" \
java -jar smartdevice-1.0.0-SNAPSHOT-fat.jar &

HTTP_PORT="8082" \
DEVICE_TYPE="http" \
DEVICE_LOCATION="garden" \
DEVICE_ID="BVOP34" \
DEVICE_HOSTNAME="devices.home.smart" \
GATEWAY_TOKEN="smart.home" \
GATEWAY_DOMAIN="gateway.home.smart" \
GATEWAY_PORT=9090 \
java -jar smartdevice-1.0.0-SNAPSHOT-fat.jar &
EOF
