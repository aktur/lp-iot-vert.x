package devices;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import sensors.Sensor;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public interface Device {

  LinkedList<Sensor> getSensors();
  Device setSensors(List<Sensor> sensors);
  String getId();

  String getPosition();
  Device setPosition(String value);

  String getCategory();
  Device setCategory(String value);


  default JsonObject jsonValue() {

    var jsonSensorsArray = getSensors()
      .stream()
      .map(Sensor::jsonValue)
      .collect(Collectors.toList());

    return new JsonObject()
      .put("id", getId())
      .put("location", getPosition())
      .put("category", getCategory())
      .put("sensors", new JsonArray(jsonSensorsArray));
  }
}
