#!/bin/bash

MONGO_PORT="27017" \
MONGO_HOST="things.home.smart" \
MONGO_BASE_NAME="smarthome_db" \
java -jar target/webapp-1.0.0-SNAPSHOT-fat.jar
