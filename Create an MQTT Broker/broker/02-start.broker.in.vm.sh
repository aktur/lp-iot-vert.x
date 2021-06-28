#!/bin/bash

multipass --verbose exec broker -- bash << EOF
cd app

MONGO_PORT="27017" \
MONGO_HOST="localhost" \
MONGO_BASE_NAME="smarthome_db" \
java -jar broker-1.0.0-SNAPSHOT-fat.jar ;
EOF
