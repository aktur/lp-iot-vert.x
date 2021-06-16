package communications;

import devices.Device;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;

public interface Http {

  boolean isConnectedToGateway();
  void setConnectedToGateway(boolean value);

  default String getProtocol() {
    return "http";
  }

  /*
    create an http request to register the device on the gateway
    do a POST request on "/register" with a header "smart-token"
    Use io.vertx.ext.web.client.WebClient to do the request
   */
  default HttpRequest<Buffer> createRegisterToGatewayRequest(Vertx vertx, String domainName, int port, boolean ssl, String token) {
    return null;
  }
  // create a router
  default Router createRouter(Vertx vertx) {
    return null;
  }

  // create an HTTP server
  default HttpServer createHttpServer(Vertx vertx, Router router) {
    return null;
  }

  int getPort();
  Device setPort(int value);


  // get host name
  default String getHostName() {
    return Optional.ofNullable(System.getenv("DEVICE_HOSTNAME")).orElse("devices.home.smart");
  }


}
