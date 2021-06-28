#!/bin/bash

multipass --verbose exec devices -- bash << EOF
java --version
EOF
