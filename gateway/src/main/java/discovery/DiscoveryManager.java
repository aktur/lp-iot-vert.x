package discovery;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;

import java.util.Optional;

public class DiscoveryManager {
  /*
    Initialize the ServiceDiscovery
    Set the backend configuration
    In the last milestone, check if the Redis Db is connected
   */
  public ServiceDiscovery initializeServiceDiscovery(Vertx vertx) {
    var redisHost = Optional.ofNullable(System.getenv("REDIS_HOST")).orElse("localhost");
    var redisPort = Integer.parseInt(Optional.ofNullable(System.getenv("REDIS_PORT")).orElse("6379"));
    var redisAuth = Optional.ofNullable(System.getenv("REDIS_PASSWORD")).orElse("");

    var redisConnectionString = ...
    /*
      Initialize the ServiceDiscovery
      Set the backend configuration
      return the ServiceDiscovery instance
   */

    return ServiceDiscovery.create ...
  }
}
