#!/bin/bash

multipass launch --verbose --name mqtt \
  --cpus 1 \
  --mem 512M \
  --disk 3G \
  --cloud-init ./vm.cloud-init.yaml

IP=$(multipass info mqtt | grep IPv4 | awk '{print $2}')
echo "${IP} mqtt.home.smart" > hosts.config

