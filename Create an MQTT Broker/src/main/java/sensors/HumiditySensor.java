package sensors;

public class HumiditySensor implements Sensor {
  @Override
  public String getName() {
    return "humidity";
  }

  @Override
  public String getUnit() {
    return "%";
  }

  @Override
  public double getLevel(int t) {
    var minHumidity = 1.0;
    var maxHumidity= 100.0;
    return simulate(t,minHumidity, maxHumidity);
  }
}
