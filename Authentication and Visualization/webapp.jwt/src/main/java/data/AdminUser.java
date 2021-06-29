package data;

public class AdminUser {
  private String name;
  private String password;
  private Boolean authenticated;

  public String getName() {
    return name;
  }

  public String getPassword() {
    return password;
  }

  public Boolean isAuthenticated() {
    return authenticated;
  }

  public void setAuthenticated(Boolean authenticated) {
    this.authenticated = authenticated;
  }

  public AdminUser(String name, String password) {
    this.name = name;
    this.password = password;
    this.authenticated = false;
  }
}
