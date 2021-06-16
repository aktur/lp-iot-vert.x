package sensors;
// eCO2：400-60000 ppm
public class eCO2Sensor implements Sensor {
  @Override
  public String getName() {
    return "eCO2";
  }

  @Override
  public String getUnit() {
    return "ppm";
  }

  @Override
  public double getLevel(int t) {
    var minQuality = 400.0;
    var maxQuality= 60000.0;
    return simulate(t,minQuality, maxQuality);


  }
}
