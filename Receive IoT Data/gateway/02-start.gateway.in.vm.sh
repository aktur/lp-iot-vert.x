#!/bin/bash

multipass --verbose exec gateway -- bash << EOF
cd app

GATEWAY_TOKEN="smart.home" \
GATEWAY_SSL="false" \
REDIS_HOST="localhost" \
MQTT_HOST="things.home.smart" \
java -jar gateway-1.0.0-SNAPSHOT-fat.jar ;
EOF
