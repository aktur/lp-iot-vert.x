#!/bin/bash
curl --header "Content-Type: application/json" \
     --header "smart-token: smart.home" \
     --request POST \
     --data '{"category":"something","id":"000","position":"somewhere","host":"localhost","port":8081}' \
     http://localhost:9090/register
