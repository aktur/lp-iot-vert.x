package http;

public class RegistrationData {
  private final String category;
  private final String id;
  private final String position;
  private final String ip;
  private final String port;

  public String getCategory() {
    return category;
  }

  public String getId() {
    return id;
  }

  public String getPosition() {
    return position;
  }

  public String getIp() {
    return ip;
  }

  public String getPort() {
    return port;
  }

  public RegistrationData(String id, String category, String position, String ip, String port) {
    this.category = category;
    this.id = id;
    this.position = position;
    this.ip = ip;
    this.port = port;
  }
}
