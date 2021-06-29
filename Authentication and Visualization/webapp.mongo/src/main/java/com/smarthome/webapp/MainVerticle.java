package com.smarthome.webapp;

import data.AdminUser;
import data.MongoStore;
import io.reactivex.Completable;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSBridgeOptions;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.reactivex.ext.web.handler.BodyHandler;
import io.vertx.reactivex.ext.web.handler.StaticHandler;
import io.vertx.reactivex.ext.web.handler.sockjs.BridgeEvent;
import io.vertx.reactivex.ext.web.handler.sockjs.SockJSHandler;
import jwt.JwtHelper;

import java.util.Optional;

public class MainVerticle extends AbstractVerticle {

  AdminUser adminUser;

  @Override
  public Completable rxStop() {
    System.out.println("Webapp stopped");
    return super.rxStop();
  }

  @Override
  public Completable rxStart() {

    // =============== MongoDb ===============
    // MongoDb parameters
    var mongoPort = Integer.parseInt(Optional.ofNullable(System.getenv("MONGO_PORT")).orElse("27017"));
    var mongoHost = Optional.ofNullable(System.getenv("MONGO_HOST")).orElse("localhost");
    var mongoBaseName = Optional.ofNullable(System.getenv("MONGO_BASE_NAME")).orElse("smarthome_db");

    // Initialize the connection to the MongoDb database
    MongoStore.initialize(vertx, "mongodb://"+mongoHost+":"+mongoPort, mongoBaseName);

    // =============== JWT Authentication ===============
    var adminName = Optional.ofNullable(System.getenv("ADMIN_NAME")).orElse("root");
    var adminPassword = Optional.ofNullable(System.getenv("ADMIN_PASSWORD")).orElse("admin");
    adminUser = new AdminUser(adminName, adminPassword);

    var jwtHelper = JwtHelper.initialize(vertx);
    var jwtHandler = jwtHelper.getHandler();

    // =============== WebApp ===============
    var httpPort = Integer.parseInt(Optional.ofNullable(System.getenv("HTTP_PORT")).orElse("8080"));
    var staticPath = Optional.ofNullable(System.getenv("STATIC_PATH")).orElse("/*");

    // Define a router
    var router = Router.router(vertx);

    router.route().handler(BodyHandler.create());
    // Serving static resources
    var staticHandler = StaticHandler.create();
    staticHandler.setCachingEnabled(false);
    router.route(staticPath).handler(staticHandler);

    // =============== Authentication Root ===============
    /*
      The JWT handler can be used for routes that require JWT authentication,
      as it decodes the Authorization header to extract JWT data.

      Test it:

      curl --request POST \
           --header "content-type: application/json" \
           --url http://localhost:8080/authenticate \
           --data '{
              "username": "root", "password": "admin"
           }'

    */
    router.post("/authenticate").handler(routingContext -> {

      var payload = routingContext.getBodyAsJson();
      var user = payload.getString("username");
      var pwd = payload.getString("password");

      routingContext.response()
        .putHeader("Content-Type", "application/jwt");

      if(user.equals(adminUser.getName()) && pwd.equals(adminUser.getPassword())) {
        // this adminUser object allows to check the authentication when using the service bus
        adminUser.setAuthenticated(true);

        // generate a token
        var token = jwtHelper.generateToken(user, new JsonObject().put("greetingMessage", "Welcome "+user));
        // answer with token

        routingContext
          .response().setStatusCode(200)
          .send(new JsonObject().put("token", token).encode());
      } else  {
        routingContext
          .response().setStatusCode(401)
          .send(new JsonObject().put("message", "Bad JWT Token").encode());
      }
    });

    // protect the root with the jwtHandler
    // if the token is not recognized the response status is "Unauthorized"
    /*
      Test it
      curl http://localhost:8080/say-hello # Unauthorized%

      TOKEN="your-token"

      curl --header "content-type: application/json" \
           --header "Authorization: Bearer ${TOKEN}" \
           --url http://localhost:8080/say-hello
     */
    router.get("/say-hello").handler(jwtHandler).handler(routingContext -> {

      var documents = new JsonArray();

      // retrieve a stream of the 10 latest documents
      MongoStore.getLastDevicesMetricsFlowable(10)
        .subscribe(doc -> {
          // at every found document display the document and add it to the documents list
          documents.add(doc);
          System.out.println(doc.encodePrettily());
        }, throwable -> {
          System.out.println(throwable.getMessage());
          routingContext.response().setStatusCode(500);
          routingContext.json(new JsonObject().put("error", throwable.getMessage()));

        }, () -> {
          // All documents found
          System.out.println("All documents found");
          routingContext.json(documents);
        });

    });


    // protect the root with the jwtHandler
    router.get("/disconnect").handler(jwtHandler).handler(routingContext -> {
      adminUser.setAuthenticated(false);
      routingContext.json(new JsonObject().put("message", "disconnected"));
    });




    // =============== Start the http server  ===============
    var httpserver = vertx.createHttpServer().requestHandler(router);

    return httpserver.rxListen(httpPort)
      .doOnSuccess(ok-> {
        System.out.println("Web Application: HTTP server started on port " + httpPort);
      }).doOnError(error -> {
        System.out.println(error.getCause().getMessage());
      }).ignoreElement();

  }

}
