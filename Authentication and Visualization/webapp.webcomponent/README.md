# WebApp

## Build

To package your application:
```bash
./mvnw clean package
```

To run your application:
```bash
./mvnw clean compile exec:java
```

## Start the web application with the jar file

### Broker

Before starting the web application, you need to
- start a MongoDb database
- start the broker

```bash
MONGO_PORT="27017" \
MONGO_HOST="localhost" \
MONGO_BASE_NAME="smarthome_db" \
java -jar broker-1.0.0-SNAPSHOT-fat.jar
```
> set the environment variables to fit your context (local or in a virtual machine)

If you use the virtual machine (with Multipass or Vagrant, the MongoDb database is started automatically)

### Gateway

- start a Redis database
- start the gateway

```bash
GATEWAY_TOKEN="smart.home" \
GATEWAY_SSL="false" \
REDIS_HOST="localhost" \
MQTT_HOST="things.home.smart" \
MQTT_TOPIC="house" \
java -jar gateway-1.0.0-SNAPSHOT-fat.jar
```
> set the environment variables to fit your context (local or in a virtual machine)

If you use the virtual machine (with Multipass or Vagrant, the Redis database is started automatically)

### Devices

Then, you need to start at least one HTTP device simulator ans one MQTT device simulator:

```bash
DEVICE_TYPE="mqtt" \
DEVICE_LOCATION="attic" \
MQTT_CLIENT_ID="mqtt_001" \
DEVICE_ID="001" \
MQTT_PORT="1883" \
MQTT_HOST="things.home.smart" \
MQTT_TOPIC="house" \
java -jar smartdevice-1.0.0-SNAPSHOT-fat.jar &

HTTP_PORT="8082" \
DEVICE_TYPE="http" \
DEVICE_LOCATION="garden" \
DEVICE_ID="BVOP34" \
DEVICE_HOSTNAME="devices.home.smart" \
GATEWAY_TOKEN="smart.home" \
GATEWAY_DOMAIN="gateway.home.smart" \
GATEWAY_PORT=9090 \
java -jar smartdevice-1.0.0-SNAPSHOT-fat.jar &
```
> set the environment variables to fit your context (local or in a virtual machine)


Then you can start the web application locally or in the same virtual machine as the broker

```bash
MONGO_PORT="27017" \
MONGO_HOST="things.home.smart" \
MONGO_BASE_NAME="smarthome_db" \
java -jar target/webapp-1.0.0-SNAPSHOT-fat.jar
```

> set the environment variables to fit your context

## Pre-requisites to use JWT

You need to generate a key:

```bash
openssl genrsa -out private.pem 2048
openssl pkcs8 -topk8 -inform PEM -in private.pem -out private_key.pem -nocrypt
openssl rsa -in private.pem -outform PEM -pubout -out public_key.pem
```
