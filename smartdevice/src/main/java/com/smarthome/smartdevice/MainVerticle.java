package com.smarthome.smartdevice;

import devices.HttpDevice;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

import java.util.Optional;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void stop(Promise<Void> stopPromise) throws Exception {
    stopPromise.complete();
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    /*
      Define parameters of the application
      ------------------------------------
      deviceType, httpPort, deviceLocation, deviceId, gatewayHttPort, domainNameOrIP, ssl
     */
    var deviceType = Optional.ofNullable(System.getenv("DEVICE_TYPE")).orElse("http");
    var deviceId = Optional.ofNullable(System.getenv("DEVICE_ID")).orElse("something");

    if(deviceType.equals("http")) { // HTTP Device
      var httpPort = Integer.parseInt(Optional.ofNullable(System.getenv("HTTP_PORT")).orElse("8080"));
    /*
      Initialize the device (new HttpDevice(deviceId))
     */
      var httpDevice = new HttpDevice(deviceId);
      // add the configuration
      // add the sensors

    /*
      Create the request for the gateway
      Send the request to the gateway
      If the registration fails, call the `setConnectedToGateway(false)` method of the device
      If the registration succeeds, call the `setConnectedToGateway(true)` method of the device
     */



    /*
      Define a router
      Add a route that returns the value of the Device
     */
      var router = httpDevice.createRouter(vertx);

      // add the route

    /*
      Start the http server of the device
     */
      var httpserver = httpDevice.createHttpServer(vertx, router)
        .listen(httpPort)
        .onFailure(error -> startPromise.fail(error.getCause()))
        .onSuccess(ok -> {
          startPromise.complete();
          System.out.println("Device: HTTP server started on port " + httpPort);
        });
    } else { // MQTT Device
      // To Be done in the next project
    }



  }

}
