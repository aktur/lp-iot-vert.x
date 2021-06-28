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

  default InetAddress getInetAddress() {
    try {
      return InetAddress.getLocalHost();
    } catch (UnknownHostException e) {
      e.printStackTrace();
      return null;
    }
  }

  default String getIP() {
    var inetAddress = getInetAddress();
    return inetAddress != null ? inetAddress.getHostAddress() : null;
  }

  default String getHostName() {
    var inetAddress = getInetAddress();
    return inetAddress != null ? inetAddress.getHostName() : null;
  }

  int getPort();
  Device setPort(int value);
}
