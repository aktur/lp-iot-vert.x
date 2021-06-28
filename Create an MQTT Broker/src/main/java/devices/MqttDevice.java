package devices;

import communications.Mqtt;
import io.vertx.mqtt.MqttClient;
import sensors.Sensor;

import java.util.LinkedList;
import java.util.List;

public class MqttDevice implements Device, Mqtt {

  private String id = "something";
  private String position = "nowhere";
  private LinkedList<Sensor> sensors = null;
  private String category = "thingy";
  private MqttClient mqttClient;

  @Override
  public LinkedList<Sensor> getSensors() {
    return sensors;
  }

  @Override
  public MqttDevice setSensors(List<Sensor> sensors) {
    this.sensors.addAll(sensors);
    return this;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public String getPosition() {
    return position;
  }

  @Override
  public MqttDevice setPosition(String value) {
    position = value;
    return this;
  }

  @Override
  public String getCategory() {
    return category;
  }

  @Override
  public MqttDevice setCategory(String value) {
    category = value;
    return this;
  }

  public MqttDevice(String id, String position) {
    this.position = position;
    this.id = id;
    this.sensors = new LinkedList<Sensor>();
  }

  public MqttDevice(String id) {
    this.id = id;
    this.sensors = new LinkedList<Sensor>();
  }

  @Override
  public MqttClient getMqttClient() {
    return mqttClient;
  }

  @Override
  public void setMqttClient(MqttClient client) {
    this.mqttClient = client;
  }
}
