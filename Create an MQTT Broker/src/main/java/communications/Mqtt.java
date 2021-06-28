package communications;

import io.vertx.circuitbreaker.CircuitBreaker;
import io.vertx.circuitbreaker.CircuitBreakerOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.mqtt.MqttClient;
import io.vertx.mqtt.MqttClientOptions;
import io.vertx.mqtt.messages.MqttConnAckMessage;

import java.util.Optional;

public interface Mqtt {

  default String getProtocol() {
    return "mqtt";
  }

  MqttClient getMqttClient();
  void setMqttClient(MqttClient client);

  // Get an instance of the MQTT client
  default MqttClient createMqttClient(Vertx vertx) {
    // Give an id to the device
    var mqttClientId = Optional.ofNullable(System.getenv("MQTT_CLIENT_ID")).orElse("mqttDevice");

    // Get and return an instance of the MQTT client
    // add handlers to handle the connection issues
    return null;
  };

  // try to connect to the MQTT broker
  // use a circuit breaker to connect to handle connection issues
  default Future<MqttConnAckMessage> startAndConnectMqttClient(Vertx vertx) {
    // Define port and host of the MQTT Broker
    var mqttPort = Integer.parseInt(Optional.ofNullable(System.getenv("MQTT_PORT")).orElse("1883"));
    var mqttHost = Optional.ofNullable(System.getenv("MQTT_HOST")).orElse("mqtt.home.smart");

    // Get and return the circuit breaker
    // the circuit breaker will create the MQTT client
    // and then try to connect
    // when connected, it will set the mqttClient field
    return null;
  };

  default CircuitBreaker getBreaker(Vertx vertx) {
    // Create and return a circuit breaker with:
    // - max of failure = 3
    // - max of retries = 20
    // - timeout = 5000 ms
    // - reset timeout = 10 000 ms
    // - add a delay between every retry
    return null;
  }

}
