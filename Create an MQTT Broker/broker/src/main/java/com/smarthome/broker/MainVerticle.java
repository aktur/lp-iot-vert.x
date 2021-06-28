package com.smarthome.broker;

import data.MongoStore;
import handlers.EndPointHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.mqtt.MqttServer;
import io.vertx.mqtt.MqttServerOptions;

import java.util.Optional;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void stop(Promise<Void> stopPromise) throws Exception {
    System.out.println("Broker is stopped");
    stopPromise.complete();
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    /*
      Define parameters of the application
      ------------------------------------
    */
    // MongoDb parameters
    var mongoPort = Integer.parseInt(Optional.ofNullable(System.getenv("MONGO_PORT")).orElse("27017"));
    var mongoHost = Optional.ofNullable(System.getenv("MONGO_HOST")).orElse("localhost");
    var mongoBaseName = Optional.ofNullable(System.getenv("MONGO_BASE_NAME")).orElse("smarthome_db");

    // Initialize the connection to the MongoDb database
    MongoStore.initialize(vertx, "mongodb://"+mongoHost+":"+mongoPort, mongoBaseName);

    // MQTT parameters
    var mqttPort = Integer.parseInt(Optional.ofNullable(System.getenv("MQTT_PORT")).orElse("1883"));
    var mqttOptions = new MqttServerOptions().setPort(mqttPort);

    // Create the MQTT server
    var mqttServer = MqttServer.create(vertx, mqttOptions);

    /*
      Create handlers for the broker
      ------------------------------------
    */
    mqttServer.endpointHandler(EndPointHandler.handler);

    /*
      Start the MQTT broker
      ------------------------------------
    */
    mqttServer.listen()
      .onFailure(error -> {
        System.out.println("MQTT " + error.getMessage());
      })
      .onSuccess(ok -> {
        System.out.println("MQTT broker started, listening on " + mqttPort);
      });
  }

}
