# Broker

## Build

To build and package the gateway application, use the below command:
```bash
./mvnw clean package
```

To tun the application without packaging it, use the command below:
```bash
./mvnw clean compile exec:java
```

## Start the Broker

First build the application (`./mvnw clean package`) then run the below command:

```bash
java -jar broker-1.0.0-SNAPSHOT-fat.jar ;
```
