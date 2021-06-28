#!/bin/bash

multipass --verbose exec things -- bash << EOF
java --version
EOF
