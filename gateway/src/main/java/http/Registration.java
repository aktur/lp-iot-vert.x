package http;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.Status;
import io.vertx.servicediscovery.types.HttpEndpoint;

import java.util.Optional;

public class Registration {
  private final ServiceDiscovery discovery;
  private final String authenticationToken = Optional.ofNullable(System.getenv("GATEWAY_TOKEN")).orElse("secret");

  public ServiceDiscovery getDiscovery() {
    return discovery;
  }

  public Registration(ServiceDiscovery discovery) {
    this.discovery = discovery;
  }

  // Check if the authentication token in the "smart-token" header is the good token
  public boolean checkAuthenticationToken(RoutingContext routingContext) {
    var optionalToken = Optional.ofNullable(routingContext.request().getHeader("smart-token")) ;
    var token = optionalToken.isEmpty() ? "" : optionalToken.get();
    return token.equals(authenticationToken);
  }

  // Check if the registration payload sent by the device contains the appropriate data
  public void checkRegistrationDataFormat(RoutingContext routingContext, Handler<String> error, Handler<RegistrationData> success) {
    var payload = routingContext.getBodyAsJson();
    var category = Optional.ofNullable(payload.getString("category"));
    var id = Optional.ofNullable(payload.getString("id"));
    var position = Optional.ofNullable(payload.getString("position"));
    var host = Optional.ofNullable(payload.getString("host"));
    var port = Optional.ofNullable(payload.getString("port"));

    // check the data posted by the device
    if (category.isPresent() && id.isPresent() && position.isPresent() && host.isPresent() && port.isPresent()) {
      success.handle(new RegistrationData(id.get(), category.get(), position.get(), host.get(), port.get()));
    } else {
      error.handle("bad registration data format");
    }
  }

  public Future<Record> publish(Record record) {
    return getDiscovery().publish(record);
  }

  public Future<Record> update(Record record) {
    return getDiscovery().update(record);
  }

  // this is the handler triggered by the registration route
  public Handler<RoutingContext> handler = routingContext -> {
    if(checkAuthenticationToken(routingContext)) {

      // check the data posted by the device
      checkRegistrationDataFormat(routingContext,
        errorMessage -> {
          // this is not a device / information missing
          routingContext.json(new JsonObject().put("registration","ko").put("cause", errorMessage));
        },
        registrationData -> {
          // construct the record for registration
          var record = HttpEndpoint.createRecord(
            registrationData.getId(),
            registrationData.getIp(),
            Integer.parseInt(registrationData.getPort()),
            "/"
          );
          // add metadata
          record.setMetadata(
            new JsonObject()
              .put("category", registrationData.getCategory())
              .put("position", registrationData.getPosition())
          );

          // search if the record exists in the backend discovery
          getDiscovery().getRecord(rec -> rec.getName().equals(registrationData.getId())) // the Status.DOWN are filtered
            .onFailure(error -> {
              System.out.println("Error when fetching the records " + error.getMessage());
            })
            .onSuccess(okRecord -> {
              if(okRecord==null) {
                // the lookup succeeded, but no matching service (the record doesn't exist)
                // create the record
                publish(record)
                  .onFailure(error -> System.out.println("Error when publishing " + error.getMessage()))
                  .onSuccess(ok -> routingContext.json(new JsonObject().put("registration","ok")));

              } else {
                // The record exists
                // Update the record
                okRecord.setStatus(Status.UP);
                okRecord.setMetadata(
                  new JsonObject()
                    .put("category", registrationData.getCategory())
                    .put("position", registrationData.getPosition()));
                update(okRecord)
                  .onFailure(error -> System.out.println("Error when updating " + error.getMessage()))
                  .onSuccess(ok -> routingContext.json(new JsonObject().put("registration updated","ok")));
              }
            });
        });

    } else {
      //routingContext.response().setStatusCode(401);
      routingContext.json(new JsonObject().put("registration","ko").put("cause", "bad-token"));
    }
  };
}
