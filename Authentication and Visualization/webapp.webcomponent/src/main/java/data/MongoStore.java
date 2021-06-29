package data;

import io.reactivex.Flowable;
import io.vertx.ext.mongo.FindOptions;
import io.vertx.reactivex.core.Vertx;

import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.streams.ReadStream;
import io.vertx.reactivex.ext.mongo.MongoClient;

public class MongoStore {
  private static MongoClient mongoClient;

  public static void initialize(Vertx vertx, String connectionString, String dataBaseName) {
    // Create a MongoClient instance
    mongoClient = MongoClient.createShared(
      vertx,
      new JsonObject()
        .put("db_name",dataBaseName)
        .put("useObjectId", false)
        .put("connection_string", connectionString)
    );
  }

  public static Flowable<JsonObject> getLastDevicesMetricsFlowable(Integer howMany) {

    JsonObject query = new JsonObject();
    var options = new FindOptions();

    options.setSort(new JsonObject().put("_id",-1));
    options.setLimit(howMany);
    // Last n records
    ReadStream<JsonObject> devices = mongoClient.findBatchWithOptions("devices", query, options);

    // Convert the stream to a Flowable
    return devices.toFlowable();
  }


}
