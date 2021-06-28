# Gateway

## Build

To build and package the gateway application, use the below command:
```bash
./mvnw clean package
```

To tun the application without packaging it, use the command below:
```bash
./mvnw clean compile exec:java
```

## Start the gateway

First build the application (`./mvnw clean package`) then run the below command:

```bash
GATEWAY_TOKEN="smart.home" \
GATEWAY_SSL="false" \
REDIS_HOST="gateway.home.smart" \
java -jar target/gateway-1.0.0-SNAPSHOT-fat.jar
```

If you decided to use SSL (optional):
```bash
GATEWAY_TOKEN="smart.home" \
GATEWAY_SSL="false" \
REDIS_HOST="gateway.home.smart" \
java -jar target/gateway-1.0.0-SNAPSHOT-fat.jar
```

## Manually test the device registration

```bash
curl --header "Content-Type: application/json" \
     --header "smart-token: smart.home" \
     --request POST \
     --data '{"category":"something","id":"000","position":"somewhere","host":"localhost","port":8081}' \
     http://localhost:9090/register
```

## Simulate a "running" device

You need to serve trough http a Json payload like this one:

```json
{
  "id":"AX3345",
  "location":"bedroom",
  "sensors":[
    {"temperature":{"unit":"Celsius","value":-3.0}},
    {"humidity":{"unit":"%","value":7.631742512670286}},
    {"eCO2":{"unit":"ppm","value":15299.999999999987}}
  ]
}
```

## Deploy the jar gateway to the virtual machine and start it

Without SSL:
```bash
```bash
# package the gateway application
./mvnw clean package
# copy the jar file to the shared directory
cp target/*-fat.jar vms/vm-gateway/app

# start the gateway inside the virtual machine
multipass --verbose exec gateway -- bash << EOF
cd app

GATEWAY_TOKEN="smart.home" \
GATEWAY_SSL="false" \
REDIS_HOST="localhost" \
java -jar gateway-1.0.0-SNAPSHOT-fat.jar
EOF
```
The url of the gateway will be [https://gateway.home.smart:9090](https://gateway.home.smart:9090)
> `9090` is the default http port

With SSL:
```bash
# package the gateway application
./mvnw clean package
# copy the jar file to the shared directory
cp target/*-fat.jar vms/vm-gateway/app

# start the gateway inside the virtual machine
multipass --verbose exec gateway -- bash << EOF
cd app

GATEWAY_SSL="true" \
GATEWAY_HTTP_PORT=8443 \
GATEWAY_CERTIFICATE="./certificates/gateway.home.smart.crt" \
GATEWAY_KEY="./certificates/gateway.home.smart.key" \
REDIS_HOST="localhost" \
java -jar gateway-1.0.0-SNAPSHOT-fat.jar
EOF
```
The url of the gateway will be [https://gateway.home.smart:8443](https://gateway.home.smart:8443)

## SSL Certificate

This part is optional, but you can use your own certificate and key. If you want to generate self-signed certificates, you can use the **MKCert** project: [https://github.com/FiloSottile/mkcert](https://github.com/FiloSottile/mkcert)

**Example**: If you want to generate a certificate and a key for your local domain name `gateway.home.smart`:

```bash
cd certificates
mkcert home.smart "*.home.smart"
cp home.smart+1-key.pem gateway.home.smart.key
cp home.smart+1.pem gateway.home.smart.crt
EOF
```
