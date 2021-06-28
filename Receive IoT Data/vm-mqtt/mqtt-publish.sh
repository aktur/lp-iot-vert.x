#!/bin/bash

multipass --verbose exec mqtt -- bash << EOF
mosquitto_pub -h localhost -m "hello world" -t house
EOF
