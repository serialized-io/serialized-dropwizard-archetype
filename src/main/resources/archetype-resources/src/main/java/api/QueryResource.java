#set( $aggregateRootLc = $aggregateRoot.toLowerCase() )
#set( $aggregateRootLcPlural = $aggregateRootLc + "s" )
package ${package}.api;

import io.serialized.client.projection.ProjectionClient;
import io.serialized.client.projection.ProjectionResponse;
import io.serialized.client.projection.query.ProjectionQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static io.serialized.client.projection.query.ProjectionQueries.single;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("queries")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public class QueryResource {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final ProjectionClient projectionClient;

  public QueryResource(ProjectionClient projectionClient) {
    this.projectionClient = projectionClient;
  }

  @GET
  @Path("${aggregateRootLcPlural}/{${aggregateRootLc}Id}")
  public Response get${aggregateRoot}(@PathParam("${aggregateRootLc}Id") String ${aggregateRootLc}Id) {

    logger.info("Fetching ${aggregateRootLc} [{}]", ${aggregateRootLc}Id);

    ProjectionQuery query = single("${aggregateRootLcPlural}").id(${aggregateRootLc}Id).build(${aggregateRoot}Projection.class);
    ProjectionResponse<${aggregateRoot}Projection> response = projectionClient.query(query);
    ${aggregateRoot}Projection projection = response.data();
    return Response.ok(projection).build();
  }

}
