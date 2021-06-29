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

```bash
java -jar target/webapp-1.0.0-SNAPSHOT-fat.jar
```

## Pre-requisites to use JWT

You need to generate a key:

```bash
openssl genrsa -out private.pem 2048
openssl pkcs8 -topk8 -inform PEM -in private.pem -out private_key.pem -nocrypt
openssl rsa -in private.pem -outform PEM -pubout -out public_key.pem
```

