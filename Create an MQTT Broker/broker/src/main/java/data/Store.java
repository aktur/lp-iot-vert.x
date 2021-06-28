package data;

import io.vertx.mqtt.MqttEndpoint;
import io.vertx.mqtt.messages.MqttSubscribeMessage;
import models.MqttSubscription;

import java.util.HashMap;

public class Store {
  // MqttEndpoint represents an MQTT endpoint for point-to-point communication with the remote MQTT client
  private static final HashMap<String, MqttEndpoint> mqttEndpoints = new HashMap<>();

  private static final HashMap<String,MqttSubscription> mqttSubscriptions = new HashMap<>();

  public static HashMap<String, MqttEndpoint> getMqttEndpoints() {
    return mqttEndpoints;
  }

  public static HashMap<String, MqttSubscription> getMqttSubscriptions() {
    return mqttSubscriptions;
  }
}
