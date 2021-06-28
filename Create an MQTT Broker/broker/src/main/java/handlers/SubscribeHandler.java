package handlers;

import data.Store;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.Handler;
import io.vertx.mqtt.MqttEndpoint;
import io.vertx.mqtt.messages.MqttSubscribeMessage;
import models.MqttSubscription;

import java.util.ArrayList;

public class SubscribeHandler {

  public static Handler<MqttSubscribeMessage> handler(MqttEndpoint mqttEndpoint) {

    return mqttSubscribeMessage -> {
      var grantedQosLevels = new ArrayList<MqttQoS>();
      // several topic subscriptions are possible
      mqttSubscribeMessage.topicSubscriptions().forEach(mqttTopicSubscription -> {

        System.out.println("Subscription to " +
          mqttTopicSubscription.topicName() + " with QoS " +
          mqttTopicSubscription.qualityOfService()
        );

        grantedQosLevels.add(mqttTopicSubscription.qualityOfService());

        // -------------------------------------------------
        //  Add mqtt subscription to the subscriptions list
        // -------------------------------------------------
        Store.getMqttSubscriptions().put(
          mqttTopicSubscription.topicName() + "@" + mqttEndpoint.clientIdentifier(),
          new MqttSubscription(mqttTopicSubscription.topicName(), mqttEndpoint)
        );
      });

      // ---------------------------------------
      //  Acknowledge the subscriptions request
      // ---------------------------------------
      mqttEndpoint.subscribeAcknowledge(mqttSubscribeMessage.messageId(), grantedQosLevels);

    };

  }

}
