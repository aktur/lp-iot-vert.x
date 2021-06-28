#!/bin/bash
DEVICE_TYPE="mqtt" \
DEVICE_LOCATION="attic" \
MQTT_CLIENT_ID="WWXX12" \
DEVICE_ID="WWXX12" \
MQTT_PORT="1883" \
MQTT_HOST="localhost" \
MQTT_TOPIC="house" \
java -jar target/smartdevice-1.0.0-SNAPSHOT-fat.jar ;

# GATEWAY_TOKEN="smart.home" \
# MQTT_HOST="mqtt.home.smart" \
# localhost
