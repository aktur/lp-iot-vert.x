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
    var mongoPort =
    var mongoHost =
    var mongoBaseName =

    // Initialize the connection to the MongoDb database
    MongoStore.initialize

    // MQTT parameters
    var mqttPort =
    var mqttOptions =

    // Create the MQTT server
    var mqttServer =

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
