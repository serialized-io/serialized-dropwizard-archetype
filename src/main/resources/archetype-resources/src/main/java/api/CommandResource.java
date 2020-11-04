#set( $aggregateRootLc = $aggregateRoot.toLowerCase() )
#set( $aggregateRootLcPlural = $aggregateRootLc + "s" )
package ${package}.api;

import ${package}.domain.${aggregateRoot};
import ${package}.domain.${aggregateRoot}State;
import io.serialized.client.aggregate.AggregateClient;
import io.serialized.client.aggregate.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

import static io.serialized.client.aggregate.AggregateRequest.saveRequest;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static java.util.Collections.singletonList;

@Path("commands")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public class CommandResource {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final AggregateClient<${aggregateRoot}State> ${aggregateRootLc}AggregateClient;

  public CommandResource(AggregateClient<${aggregateRoot}State> ${aggregateRootLc}AggregateClient) {
    this.${aggregateRootLc}AggregateClient = ${aggregateRootLc}AggregateClient;
  }

  @POST
  @Path("start-${aggregateRootLc}")
  public Response start${aggregateRoot}(@Valid @NotNull Start${aggregateRoot}Command command) {

    logger.info("Starting ${aggregateRootLc} [{}]", command.${aggregateRootLc}Id);

    ${aggregateRoot} ${aggregateRootLc} = new ${aggregateRoot}(new ${aggregateRoot}State());
    Event<?> event = ${aggregateRootLc}.start(command.${aggregateRootLc}Id);

    ${aggregateRootLc}AggregateClient.save(saveRequest()
        .withAggregateId(command.${aggregateRootLc}Id)
        .withEvent(event)
        .build());

    URI location = UriBuilder.fromResource(QueryResource.class).path("${aggregateRootLcPlural}/{${aggregateRootLc}Id}").build(command.${aggregateRootLc}Id);
    return Response.created(location).build();
  }

  @POST
  @Path("finish-${aggregateRootLc}")
  public Response finish${aggregateRoot}(@Valid @NotNull Finish${aggregateRoot}Command command) {

    logger.info("Finishing ${aggregateRootLc} [{}]", command.${aggregateRootLc}Id);

    ${aggregateRootLc}AggregateClient.update(command.${aggregateRootLc}Id, state -> {
      ${aggregateRoot} ${aggregateRootLc} = new ${aggregateRoot}(state);
      return singletonList(${aggregateRootLc}.finish(command.${aggregateRootLc}Id));
    });

    return Response.ok().build();
  }

}
