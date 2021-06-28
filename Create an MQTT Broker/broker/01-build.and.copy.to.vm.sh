#!/bin/bash

./mvnw clean package
cp target/*-fat.jar ../vms/vm-broker/app

