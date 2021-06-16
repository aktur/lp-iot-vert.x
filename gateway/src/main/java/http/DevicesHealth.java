package http;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.WebClient;
import io.vertx.mqtt.MqttClient;
import io.vertx.servicediscovery.ServiceDiscovery;

import java.util.Optional;

public class DevicesHealth {

  public ServiceDiscovery getDiscovery() {
    return discovery;
  }

  private ServiceDiscovery discovery;
  private WebClient webClient;
  private MqttClient mqttClient;

  public DevicesHealth(ServiceDiscovery discovery, WebClient webClient, MqttClient mqttClient) {
    this.discovery = discovery;
    this.webClient = webClient;
    this.mqttClient = mqttClient;
  }

  // This handler is executed periodically by this line:
  // vertx.setPeriodic(5000, new DevicesHealth(discovery, webClient, mqttClient).handler);
  // in the MainVerticle

  // 1- search all record with a "category" in the discovery backend
  // 2- for each record, create a web client to do a get request to the device
  // 3- if the device is disconnected then unpublish its associated record
  // 4- if the device responds, MQTT publish the data of the device on the mqttTopic
  public Handler<Long> handler = aLong -> {
    var mqttTopic = Optional.ofNullable(System.getenv("MQTT_TOPIC")).orElse("house");

    getDiscovery().getRecords(rec -> !rec.getMetadata().getString("category").isEmpty())
      .onFailure(error -> {
        System.out.println("Discovery error: " + error.getMessage());
      })
      .onSuccess(okRecords -> {

      });
  };
}
