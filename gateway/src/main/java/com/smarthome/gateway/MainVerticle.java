package com.smarthome.gateway;

import discovery.DiscoveryManager;
import http.DevicesHealth;
import http.Registration;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.PemKeyCertOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.servicediscovery.rest.ServiceDiscoveryRestEndpoint;
import mqtt.MqttManager;

import java.util.Optional;

public class MainVerticle extends AbstractVerticle {

  MqttManager mqttManager;

  @Override
  public void stop(Promise<Void> stopPromise) throws Exception {
    System.out.println("Gateway stopped");
    mqttManager.getMqttClient().disconnect();
    stopPromise.complete();
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    /*
      Define parameters of the application
      ------------------------------------
      - Redis:
        redisHost, redisPort, redisAuth, redisConnectionString
      - Http Server:
        gatewayHttPort, authenticationToken, gatewayCertificate (path to certificate), gatewayKey (path to key), httpServerOptions
      - MQTT Client

     */
    var gatewayHttPort = Integer.parseInt(Optional.ofNullable(System.getenv("GATEWAY_HTTP_PORT")).orElse("9090"));
    var ssl = Boolean.parseBoolean(Optional.ofNullable(System.getenv("GATEWAY_SSL")).orElse("false"));

    var gatewayCertificate = Optional.ofNullable(System.getenv("GATEWAY_CERTIFICATE")).orElse("");
    var gatewayKey = Optional.ofNullable(System.getenv("GATEWAY_KEY")).orElse("");

    var httpServerOptions = new HttpServerOptions()
      .setSsl(ssl).
        setKeyCertOptions(
          new PemKeyCertOptions()
            .addCertPath(gatewayCertificate)
            .addKeyPath(gatewayKey)
        );

    /*
      Define and connect the MQTT client
     */
    mqttManager = new MqttManager();
    mqttManager.startAndConnectMqttClient(vertx)
      .onFailure(error -> {
        System.out.println("🤬 enable to connect to broker " + error.getMessage());
      })
      .onSuccess(ok -> {
        var discovery = new DiscoveryManager().initializeServiceDiscovery(vertx);

        /*
          Create the router
          Create a BodyHandler (The BodyHandler allows you to retrieve request bodies, limit body sizes and handle file uploads.)
          See: https://vertx.io/docs/vertx-web/java/#_request_body_handling

          Creates the REST endpoint using the default root (/discovery).
          Test it: curl http://localhost:9090/discovery

          Create the registration route
         */
        var router = Router.router(vertx);
        router.route().handler(BodyHandler.create());

        ServiceDiscoveryRestEndpoint.create(router, discovery);

        /*
        Define the registration route: "/register":
        This route is called by the devices to register themselves
        Use a post method
        Tips: to get the POST data, use the `getBodyAsJson` method of the routing context (routingContext.getBodyAsJson())

        Check if the authentication token is ok
        Then check that the data of the Json payload are ok
          Search if the record exists
            if not, publish the new record
            if yes, update the existing record with the new data and set the status of the device to UP (okRecord.setStatus(Status.UP))

         The route must send an acknowledge response like:
         routingContext.json(new JsonObject().put("registration updated","ok"));
         or routingContext.json(new JsonObject().put("registration","ko"));
         or routingContext.json(new JsonObject().put("registration","ko").put("cause", "bad-token"));

       */
        var registration = new Registration(discovery);

        router.post("/register").handler(registration.handler);
        /*
        Create a timer that calls a handler every 5 seconds
        Then every 5 seconds, do:
          - get the list of the registered devices
          - for each device to an http request to the devices
            - if you cannot connect to a device, un publish the device
            - if the connection is successful
              - use the MQTT client to publish the JSON data of the object

         You can test this part by running the skeleton project of the devices simulator
       */
        var webClient = WebClient.create(vertx);
        var mqttClient = mqttManager.getMqttClient();
        vertx.setPeriodic(5000, new DevicesHealth(discovery, webClient, mqttClient).handler);

        /*
          Create and start the http server
         */
        vertx.createHttpServer(httpServerOptions).requestHandler(router)
          .listen(gatewayHttPort)
          .onFailure(error -> startPromise.fail(error.getCause()))
          .onSuccess(httpServerOk -> {
            startPromise.complete();
            System.out.println("Gateway: HTTP server started on port " + gatewayHttPort);
          });

      });

  }

}
