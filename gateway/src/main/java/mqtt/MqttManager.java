package mqtt;

import io.vertx.circuitbreaker.CircuitBreaker;
import io.vertx.circuitbreaker.CircuitBreakerOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.mqtt.MqttClient;
import io.vertx.mqtt.MqttClientOptions;
import io.vertx.mqtt.messages.MqttConnAckMessage;

import java.util.Optional;

public class MqttManager {
  private MqttClient mqttClient;
  private CircuitBreaker breaker;

  public MqttClient getMqttClient() {
    return mqttClient;
  }

  // get a circuit breaker
  private CircuitBreaker getBreaker(Vertx vertx) {
    if(breaker==null) {
      breaker = //  create the circuit breaker
    }
    return breaker;
  }

  // create and connect the MQTT client "in" a Circuit Breaker
  public Future<MqttConnAckMessage> startAndConnectMqttClient(Vertx vertx) {

    var mqttClientId = Optional.ofNullable(System.getenv("MQTT_CLIENT_ID")).orElse("gateway");

    var mqttPort = Integer.parseInt(Optional.ofNullable(System.getenv("MQTT_PORT")).orElse("1883"));
    var mqttHost = Optional.ofNullable(System.getenv("MQTT_HOST")).orElse("mqtt.home.smart");

    return getBreaker(vertx).execute(promise -> {

      mqttClient = // create the mqtt client

      // some code executing with the breaker
      // the code reports failures or success on the given promise.
      // if this promise is marked as failed, the breaker increased the
      // number of failures

      // connect the mqttClient

    });

  }
}

