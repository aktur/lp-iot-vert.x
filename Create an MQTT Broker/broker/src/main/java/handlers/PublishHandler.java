package handlers;

import data.MongoStore;
import data.Store;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.mqtt.MqttEndpoint;
import io.vertx.mqtt.messages.MqttPublishMessage;
import java.time.LocalDate;

public class PublishHandler {
  public static Handler<MqttPublishMessage> handler(MqttEndpoint mqttEndpoint) {
    return mqttPublishMessage -> {
      // ---------------------------------------
      //  Transform the Payload to a String
      // ---------------------------------------
      var message =

      System.out.println("From: " + mqttEndpoint.clientIdentifier());
      System.out.println("New message on topic: " + mqttPublishMessage.topicName() + " : " + message);

      // ---------------------------------------
      //  Save the message to MongoDb
      // ---------------------------------------
      try {


      } catch (Exception exception) {
        System.out.println(exception.getMessage());
      }

      // ----------------------------------------------
      //  Dispatch messages to all subscribed clients
      // ----------------------------------------------
      Store.getMqttSubscriptions().forEach((id, mqttSubscription) -> {

      });

      // ---------------------------------------
      //  Acknowledge the message
      // ---------------------------------------


    };
  }
}
