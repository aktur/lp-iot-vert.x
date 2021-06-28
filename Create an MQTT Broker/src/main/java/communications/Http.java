package communications;

import devices.Device;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;

import java.util.Optional;

public interface Http {

  boolean isConnectedToGateway();
  void setConnectedToGateway(boolean value);

  default String getProtocol() {
    return "http";
  }

  /*
    create an http request to register the device on the gateway
   */
  default HttpRequest<Buffer> createRegisterToGatewayRequest(Vertx vertx, String domainName, int port, boolean ssl, String token) {
    return WebClient.create(vertx).post(port, domainName, "/register")
      .putHeader("smart-token", token)
      .ssl(ssl);
  }

  default Router createRouter(Vertx vertx) {
    return Router.router(vertx);
  }

  default HttpServer createHttpServer(Vertx vertx, Router router) {
    return vertx.createHttpServer().requestHandler(router);
  }

  default String getHostName() {
    return Optional.ofNullable(System.getenv("DEVICE_HOSTNAME")).orElse("devices.home.smart");
  }

  int getPort();
  Device setPort(int value);
}
