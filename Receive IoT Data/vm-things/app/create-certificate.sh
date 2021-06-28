#!/bin/bash

mkcert home.smart "*.home.smart"
cp home.smart+1-key.pem things.home.smart.key
cp home.smart+1.pem things.home.smart.crt


