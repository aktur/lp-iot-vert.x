package devices;

import communications.Http;
import sensors.Sensor;

import java.util.LinkedList;
import java.util.List;

public class HttpDevice implements Device, Http {
  private boolean connectedToGateway = false;
  private String id = "something";
  private String position = "nowhere";
  private LinkedList<Sensor> sensors = null;
  private String category = "thingy";
  private int port = 0;

  @Override
  public boolean isConnectedToGateway() {
    return connectedToGateway;
  }

  @Override
  public void setConnectedToGateway(boolean value) {
    connectedToGateway = value;
  }


  @Override
  public String getPosition() {
    return position;
  }

  // it's possible to move the device
  @Override
  public HttpDevice setPosition(String value) {
    position = value;
    return this;
  }

  @Override
  public String getCategory() {
    return category;
  }

  @Override
  public HttpDevice setCategory(String value) {
    category = value;
    return this;
  }

  @Override
  public int getPort() {
    return port;
  }

  @Override
  public HttpDevice setPort(int value) {
    port = value;
    return this;
  }

  @Override
  public LinkedList<Sensor> getSensors() {
    return sensors;
  }

  @Override
  public HttpDevice setSensors(List<Sensor> sensors) {
    this.sensors.addAll(sensors);
    return this;
  }

  @Override
  public String getId() {
    return id;
  }

  public HttpDevice(String id, String position) {
    this.position = position;
    this.id = id;
    this.sensors = new LinkedList<Sensor>();
  }

  public HttpDevice(String id) {
    this.id = id;
    this.sensors = new LinkedList<Sensor>();
  }

}
