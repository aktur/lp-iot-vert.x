package data;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

public class MongoStore {
  private static MongoClient mongoClient;

  public static void initialize(Vertx vertx, String connectionString, String dataBaseName) {
    // Create a MongoClient instance
    mongoClient = ...
  }

  public static MongoClient getMongoClient() {
    return mongoClient;
  }

}
