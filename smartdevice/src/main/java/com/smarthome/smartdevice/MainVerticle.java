package com.smarthome.smartdevice;

import devices.HttpDevice;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import sensors.eCO2Sensor;
import sensors.HumiditySensor;
import sensors.TemperatureSensor;

import java.util.List;
import java.util.Optional;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void stop(Promise<Void> stopPromise) throws Exception {
    System.out.println("Device stopped");
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
    var deviceLocation = Optional.ofNullable(System.getenv("DEVICE_LOCATION")).orElse("somewhere");
    var deviceId = Optional.ofNullable(System.getenv("DEVICE_ID")).orElse("something");

    if(deviceType.equals("http")) { // HTTP Device

      var httpPort = Integer.parseInt(Optional.ofNullable(System.getenv("HTTP_PORT")).orElse("8080"));

      var gatewayHttPort = Integer.parseInt(Optional.ofNullable(System.getenv("GATEWAY_HTTP_PORT")).orElse("9090"));
      var domainNameOrIP = Optional.ofNullable(System.getenv("GATEWAY_DOMAIN")).orElse("0.0.0.0");
      var ssl = Boolean.parseBoolean(Optional.ofNullable(System.getenv("GATEWAY_SSL")).orElse("false"));

      var authenticationToken = Optional.ofNullable(System.getenv("GATEWAY_TOKEN")).orElse("secret");

    /*
      Initialize the device (new HttpDevice(deviceId))
     */
      var httpDevice = new HttpDevice(deviceId)
        .setCategory("Temperature Humidity Environment Sensor")
        .setPosition(deviceLocation)
        .setPort(httpPort)
        .setSensors(List.of(
          new TemperatureSensor(),
          new HumiditySensor(),
          new eCO2Sensor()
        ));

    /*
      Create the request for the gateway
      Send the request to the gateway
      If the registration fails, call the `setConnectedToGateway(false)` method of the device
      If the registration succeeds, call the `setConnectedToGateway(true)` method of the device
     */
      var requestToGateway = httpDevice.createRegisterToGatewayRequest(vertx, domainNameOrIP, gatewayHttPort, ssl, authenticationToken);

      var registration = new JsonObject()
        .put("category",httpDevice.getCategory())
        .put("id", httpDevice.getHostName())
        .put("position", httpDevice.getPosition())
        .put("host", httpDevice.getHostName())
        .put("port", httpDevice.getPort());

      requestToGateway.sendJsonObject(registration)
        .onFailure(error -> {
          System.out.println("Connection to the Gateway failed: " + error.getMessage());
          httpDevice.setConnectedToGateway(false);
        })
        .onSuccess(response -> {
          if (response.statusCode() != 200) {
            System.out.println("Registration failed: " + response.statusCode());
            httpDevice.setConnectedToGateway(false);
            // try again
          } else {
            httpDevice.setConnectedToGateway(true);
            System.out.println("Registration succeeded: " + response.statusCode());
            System.out.println(response.bodyAsJsonObject().encodePrettily());
          }
        });

    /*
      Define a router
      Add a route that returns the value of the Device
     */
      var router = httpDevice.createRouter(vertx);

      router.get("/").handler(routingContext -> {
        routingContext.json(httpDevice.jsonValue());
      });

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
