#!/bin/bash

multipass --verbose exec mqtt -- bash << EOF
mosquitto_sub -h localhost -t house/#
EOF
