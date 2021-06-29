#!/bin/bash

cp target/webapp-1.0.0-SNAPSHOT-fat.jar webapp-1.0.0-SNAPSHOT-fat.jar

MONGO_PORT="27017" \
MONGO_HOST="things.home.smart" \
MONGO_BASE_NAME="smarthome_db" \
java -jar webapp-1.0.0-SNAPSHOT-fat.jar \
  -Dvertx.disableFileCPResolving=true

# https://stackoverflow.com/questions/31778971/vertx-web-where-do-i-place-webroot-folder
