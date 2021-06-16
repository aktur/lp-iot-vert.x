#!/bin/bash
curl --header "Content-Type: application/json" \
     --request POST \
     --data '{"category":"zzz","id":"xxx","position":"somewhere","ip":"127.0.0.1","port":8081}' \
     http://localhost:9090/register
