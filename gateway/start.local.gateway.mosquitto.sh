#!/bin/bash
GATEWAY_TOKEN="smart.home" \
GATEWAY_SSL="false" \
REDIS_HOST="gateway.home.smart" \
MQTT_HOST="mqtt.home.smart" \
java -jar target/gateway-1.0.0-SNAPSHOT-fat.jar

