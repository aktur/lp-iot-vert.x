package models;

import io.vertx.mqtt.MqttEndpoint;

public class MqttSubscription {
  private final String topic;
  private final MqttEndpoint mqttEndpoint;

  public String getTopic() {
    return topic;
  }

  public MqttEndpoint getMqttEndpoint() {
    return mqttEndpoint;
  }

  public MqttSubscription(String topic, MqttEndpoint mqttEndpoint) {
    this.topic = topic;
    this.mqttEndpoint = mqttEndpoint;
  }
}
