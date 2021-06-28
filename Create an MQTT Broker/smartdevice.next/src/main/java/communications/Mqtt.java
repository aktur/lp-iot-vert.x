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

  default MqttClient createMqttClient(Vertx vertx) {
    // Give an id to the device
    var mqttClientId = Optional.ofNullable(System.getenv("MQTT_CLIENT_ID")).orElse("mqttDevice");

    // Get and return an instance of the MQTT client
    // add handlers to handle the connection issues
    return MqttClient.create(vertx, new MqttClientOptions().setClientId(mqttClientId)
    ).exceptionHandler(throwable -> {
      // Usually, this handler allows to catch Netty errors
      // there is a bug with this part (it does not prevent the program to work)
      // I asked the Vert.x team - stay tuned
      System.out.println(throwable.getMessage());
    }).closeHandler(voidValue -> {
      // this handler is executed when the client loose the connection
      System.out.println("The connection with the broker is lost");
      // try to reconnect
      startAndConnectMqttClient(vertx);
    });
  };

  // try to connect to the MQTT broker
  // use a circuit breaker to connect to handle connection issues
  default Future<MqttConnAckMessage> startAndConnectMqttClient(Vertx vertx) {
    // Define port and host of the MQTT Broker
    var mqttPort = Integer.parseInt(Optional.ofNullable(System.getenv("MQTT_PORT")).orElse("1883"));
    var mqttHost = Optional.ofNullable(System.getenv("MQTT_HOST")).orElse("mqtt.home.smart");

    // Get and return the circuit breaker
    return getBreaker(vertx).execute(promise -> {

      // create the MQTT client
      var mqttClient = createMqttClient(vertx);

      // connect to the MQTT broker
      mqttClient.connect(mqttPort, mqttHost)
        .onFailure(error -> {
          System.out.println("MQTT " + error.getMessage());
          promise.fail("[" + error.getMessage() + "]");
        })
        .onSuccess(ok -> {
          System.out.println("The connection looks good");
          // make the broker available for the other classes
          setMqttClient(mqttClient);
          promise.complete();
        });

    });
  };

  default CircuitBreaker getBreaker(Vertx vertx) {
    // Create and return a circuit breaker
    return CircuitBreaker.create("device-circuit-breaker", vertx,
      new CircuitBreakerOptions()
        .setMaxFailures(3) // number of failure before opening the circuit
        .setMaxRetries(20)
        .setTimeout(5_000) // consider a failure if the operation does not succeed in time
        .setResetTimeout(10_000) // time spent in open state before attempting to re-try
    ).retryPolicy(retryCount -> retryCount * 100L); // add delay between every retry
  }

}
