#!/bin/bash

multipass --verbose exec things -- bash << EOF
cd app
java -jar broker-1.0.0-SNAPSHOT-fat.jar ;
EOF
