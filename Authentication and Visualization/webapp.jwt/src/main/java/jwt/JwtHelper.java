package jwt;

import io.vertx.reactivex.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.JWTOptions;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.reactivex.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.reactivex.ext.web.handler.JWTAuthHandler;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JwtHelper {
  private JWTAuth tokenProvider;
  private JWTAuthHandler handler;

  public JWTAuthHandler getHandler() {
    return handler;
  }

  // Define jwt options
  private static JWTAuthOptions generateConfig() {
    String publicKeyContent = null;
    try {
      publicKeyContent = String.join("\n", Files.readAllLines(Paths.get("./public_key.pem"), StandardCharsets.UTF_8));
    } catch (IOException e) {
      e.printStackTrace();
    }
    String privateKeyContent = null;
    try {
      privateKeyContent = String.join("\n", Files.readAllLines(Paths.get("./private_key.pem"), StandardCharsets.UTF_8));
    } catch (IOException e) {
      e.printStackTrace();
    }

    return new JWTAuthOptions()
      .addPubSecKey(new PubSecKeyOptions().setAlgorithm("RS256").setBuffer(publicKeyContent))
      .addPubSecKey(new PubSecKeyOptions().setAlgorithm("RS256").setBuffer(privateKeyContent));
  }

  public static JwtHelper initialize(Vertx vertx)  {
    var helper = new JwtHelper();
    // create token provider
    helper.tokenProvider = JWTAuth.create(vertx, generateConfig());
    // define a JWT handler= it will be used for routes that require JWT authentication,
    // as it decodes the Authorization header to extract JWT data
    helper.handler = JWTAuthHandler.create(helper.tokenProvider);
    return helper;
  }

  public String generateToken(String username, JsonObject data) {
    var jwtOptions = new JWTOptions()
      .setAlgorithm("RS256")
      .setExpiresInMinutes(60)
      .setIssuer("this-is-the-issuer")
      .setSubject(username);
    return this.tokenProvider.generateToken(data, jwtOptions);
  }
}


