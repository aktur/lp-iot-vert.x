#!/bin/bash

TOKEN=$(curl -s -X POST -H 'Accept: application/json' -H 'Content-Type: application/json' --data '{"username":"root","password":"admin","rememberMe":false}' http://localhost:8080/authenticate | jq -r '.token')
echo "$TOKEN"

curl -H 'Accept: application/json' -H "Authorization: Bearer ${TOKEN}" http://localhost:8080/say-hello

curl -H 'Accept: application/json' -H "Authorization: Bearer ${TOKEN}" http://localhost:8080/say-hello
