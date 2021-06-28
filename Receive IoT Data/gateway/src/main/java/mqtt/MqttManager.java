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
      breaker = CircuitBreaker.create("gateway-circuit-breaker", vertx,
        new CircuitBreakerOptions()
          .setMaxFailures(3) // number of failure before opening the circuit
          .setMaxRetries(20)
          .setTimeout(5_000) // consider a failure if the operation does not succeed in time
          .setResetTimeout(10_000) // time spent in open state before attempting to re-try
      ).retryPolicy(retryCount -> retryCount * 100L);
    }
    return breaker;
  }

  // create and connect the MQTT client "in" a Circuit Breaker
  public Future<MqttConnAckMessage> startAndConnectMqttClient(Vertx vertx) {

    var mqttClientId = Optional.ofNullable(System.getenv("MQTT_CLIENT_ID")).orElse("gateway");

    var mqttPort = Integer.parseInt(Optional.ofNullable(System.getenv("MQTT_PORT")).orElse("1883"));
    var mqttHost = Optional.ofNullable(System.getenv("MQTT_HOST")).orElse("mqtt.home.smart");

    return getBreaker(vertx).execute(promise -> {

      mqttClient = MqttClient.create(vertx, new MqttClientOptions()
        .setClientId(mqttClientId)
      ).exceptionHandler(throwable -> {
        // Netty ?
        System.out.println(throwable.getMessage());
      }).closeHandler(voidValue -> {
        // Connection with broker is lost
        System.out.println("Connection with broker is lost");
        // try to connect again
        startAndConnectMqttClient(vertx);
      });

      // some code executing with the breaker
      // the code reports failures or success on the given promise.
      // if this promise is marked as failed, the breaker increased the
      // number of failures

      mqttClient.connect(mqttPort, mqttHost)
        .onFailure(error -> {
          System.out.println("MQTT " + error.getMessage());
          promise.fail("[" + error.getMessage() + "]");
        })
        .onSuccess(ok -> {
          System.out.println("Connection to the broker is ok");
          promise.complete();
        });

    });

  }
}

