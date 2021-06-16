#!/bin/bash

multipass launch --verbose --name gateway \
  --cpus 1 \
  --mem 512M \
  --disk 3G \
  --cloud-init ./vm.cloud-init.yaml

multipass mount app gateway:app

IP=$(multipass info gateway | grep IPv4 | awk '{print $2}')
echo "${IP} gateway.home.smart" > hosts.config

