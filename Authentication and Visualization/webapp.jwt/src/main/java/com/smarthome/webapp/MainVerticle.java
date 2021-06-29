package com.smarthome.webapp;

import data.AdminUser;
import io.reactivex.Completable;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.handler.BodyHandler;
import io.vertx.reactivex.ext.web.handler.StaticHandler;
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
      // get the username and password from the request

      // check if the user is the Admin user
      // if yes set the authenticated field to true
      //    - generate te token with additional data: {"greetingMessage", "Welcome "+user}
      //    - send the token: {token: token} & status code is 200
      // else status code is 401 and {message: "Bad JWT Token"}

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
    router.get("/say-hello").handler().handler(routingContext -> {
      // get the subject of the token (the user name in our case)
      // get the additional data
      // send data with subject and additional data: {greetingMessage: greetingMessage, subject: subject}
    });

    // protect the root with the jwtHandler
    router.get("/disconnect").handler().handler(routingContext -> {
      // set the authenticated field of the AdminUser to true
      // reply: {"message": "disconnected"}

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
