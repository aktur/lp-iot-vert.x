package com.smarthome.smartdevice;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

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
      httpPort, deviceLocation, deviceId, gatewayHttPort, domainNameOrIP, ssl
     */

    /*
      Initialize the device (new HttpDevice(deviceId))
     */
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

    // add the route

    /*
      Start the http server of the device
     */

  }

}
