#!/bin/bash

multipass --verbose exec gateway -- bash << EOF
cd app

GATEWAY_TOKEN="smart.home" \
GATEWAY_SSL="false" \
REDIS_HOST="redis.home.smart" \
MQTT_HOST="mqtt.home.smart" \
java -jar gateway-1.0.0-SNAPSHOT-fat.jar ;
EOF
