package devices;

import communications.Mqtt;
import io.vertx.mqtt.MqttClient;
import sensors.Sensor;

import java.util.LinkedList;
import java.util.List;

public class MqttDevice implements Device, Mqtt {

  // Implement all the method of Device and Mqtt interfaces

  public MqttDevice(String id) {
    this.id = id;
    this.sensors = new LinkedList<Sensor>();
  }

}
