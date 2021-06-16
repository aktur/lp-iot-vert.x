#!/bin/bash

multipass --verbose exec things -- sudo -- bash << EOF
systemctl status mongodb
EOF
