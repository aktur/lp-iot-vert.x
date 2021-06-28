package handlers;

import data.MongoStore;
import data.Store;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.mqtt.MqttEndpoint;
import io.vertx.mqtt.messages.MqttPublishMessage;
import java.time.LocalDate;
import java.time.LocalTime;

public class PublishHandler {
  public static Handler<MqttPublishMessage> handler(MqttEndpoint mqttEndpoint) {
    return mqttPublishMessage -> {
      // ---------------------------------------
      //  Transform the Payload to a String
      // ---------------------------------------
      var message = mqttPublishMessage.payload().toString(java.nio.charset.Charset.defaultCharset());

      System.out.println("From: " + mqttEndpoint.clientIdentifier());
      System.out.println("New message on topic: " + mqttPublishMessage.topicName() + " : " + message);

      // ---------------------------------------
      //  Save the message to MongoDb
      // ---------------------------------------
      try {
        var json = new JsonObject()
          .put("topic",mqttPublishMessage.topicName())
          .put("device", new JsonObject(message))
          .put("date", LocalDate.now().toString())
          .put("hour", LocalTime.now().toString());
        System.out.println(json.encodePrettily());

        MongoStore.getMongoClient().save("devices", json)
        .onFailure(error -> {
          System.out.println(error.getMessage());
        })
        .onSuccess(mongoMessage-> {
          System.out.println(mongoMessage);
        });

      } catch (Exception exception) {
        System.out.println(exception.getMessage());
      }

      // ----------------------------------------------
      //  Dispatch messages to all subscribed clients
      // ----------------------------------------------
      Store.getMqttSubscriptions().forEach((id, mqttSubscription) -> {
        if(mqttSubscription.getTopic().equals(mqttPublishMessage.topicName()) && mqttSubscription.getMqttEndpoint().isConnected()) {
          mqttSubscription.getMqttEndpoint().publish(mqttPublishMessage.topicName(), Buffer.buffer(message),mqttPublishMessage.qosLevel(),false, false);
        }
      });

      // ---------------------------------------
      //  Acknowledge the message
      // ---------------------------------------
      switch (mqttPublishMessage.qosLevel()) {
        case AT_LEAST_ONCE:
          mqttEndpoint.publishAcknowledge(mqttPublishMessage.messageId());
          break;
        case EXACTLY_ONCE:
          mqttEndpoint.publishReceived(mqttPublishMessage.messageId());
          break;
        case FAILURE:
          // TODO
          break;
      }

    };
  }
}
