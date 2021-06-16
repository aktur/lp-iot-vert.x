#!/bin/bash

multipass --verbose exec gateway -- bash << EOF
java --version
EOF
