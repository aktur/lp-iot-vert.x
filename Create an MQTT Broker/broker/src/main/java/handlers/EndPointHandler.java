package handlers;

import data.Store;
import io.vertx.core.Handler;
import io.vertx.mqtt.MqttEndpoint;

public class EndPointHandler {

  public static Handler<MqttEndpoint> handler = mqttEndpoint -> {
    System.out.println("MQTT Client " + mqttEndpoint.clientIdentifier() + " request to connect");

    /* add mqtt endpoint to the endpoints list
       MqttEndpoint represents an MQTT endpoint for point-to-point
       communication with the remote MQTT client
    */

    // ---------------------------------------
    //  Accept the endpoint
    // ---------------------------------------



    // ---------------------------------------
    //  Add handlers to the new MQTT endpoint
    // ---------------------------------------
    mqttEndpoint.subscribeHandler(SubscribeHandler.handler(mqttEndpoint));

    mqttEndpoint.publishHandler(PublishHandler.handler(mqttEndpoint));

    mqttEndpoint.disconnectHandler(DisconnectHandler.handler(mqttEndpoint));

  };

}
