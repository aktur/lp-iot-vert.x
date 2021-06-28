package handlers;

import data.Store;
import io.vertx.core.Handler;
import io.vertx.mqtt.MqttEndpoint;

public class DisconnectHandler {
  public static Handler<Void> handler(MqttEndpoint mqttEndpoint) {
    return  (Void unused)  -> {
      System.out.println(mqttEndpoint.clientIdentifier() + " is disconnecting");
      // ---------------------------------------
      //  Remove the endpoint from the list
      // ---------------------------------------
      Store.getMqttEndpoints().remove(mqttEndpoint.clientIdentifier());
    };
  }
}
