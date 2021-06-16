#!/bin/bash

multipass launch --name things \
  --cpus 2 \
  --mem 2G \
  --disk 6G \
  --cloud-init ./vm.cloud-init.yaml

multipass mount app things:app

IP=$(multipass info things | grep IPv4 | awk '{print $2}')
echo "${IP} things.home.smart" > hosts.config

