package sensors;

public class TemperatureSensor implements Sensor {

  @Override
  public String getName() {
    return "temperature";
  }

  @Override
  public String getUnit() {
    return "Celsius";
  }

  @Override
  public double getLevel(int t) {
    var minTemperature = -3.0;
    var maxTemperature= 25.0;
    return simulate(t,minTemperature, maxTemperature);
  }
}
