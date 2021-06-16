#!/bin/bash

multipass launch --verbose --name devices \
  --cpus 1 \
  --mem 512M \
  --disk 3G \
  --cloud-init ./vm.cloud-init.yaml

multipass mount app devices:app

IP=$(multipass info devices | grep IPv4 | awk '{print $2}')
echo "${IP} devices.home.smart" > hosts.config

