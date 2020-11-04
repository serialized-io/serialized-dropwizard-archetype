#set( $aggregateRootLc = $aggregateRoot.toLowerCase() )
#set( $aggregateRootLcPlural = $aggregateRootLc + "s" )
package ${package};

import ${package}.domain.${aggregateRoot}State;
import ${package}.domain.event.${aggregateRoot}Finished;
import ${package}.domain.event.${aggregateRoot}Started;
import io.dropwizard.Configuration;
import io.serialized.client.SerializedClientConfig;
import io.serialized.client.aggregate.AggregateClient;
import io.serialized.client.projection.ProjectionClient;
import io.serialized.client.reaction.ReactionClient;

import javax.validation.constraints.NotBlank;

import static io.serialized.client.aggregate.AggregateClient.aggregateClient;

public class ${applicationName}Config extends Configuration {

  @NotBlank
  public String serializedAccessKey;

  @NotBlank
  public String serializedSecretAccessKey;

  public AggregateClient<${aggregateRoot}State> ${aggregateRootLc}AggregateClient(SerializedClientConfig config) {
    return aggregateClient("${aggregateRootLc}", ${aggregateRoot}State.class, config)
        .registerHandler(${aggregateRoot}Started.class, ${aggregateRoot}State::handle${aggregateRoot}Started)
        .registerHandler(${aggregateRoot}Finished.class, ${aggregateRoot}State::handle${aggregateRoot}Finished)
        .build();
  }

  public ProjectionClient projectionClient(SerializedClientConfig config) {
    return new ProjectionClient.Builder(config).build();
  }

  public ReactionClient reactionClient(SerializedClientConfig config) {
    return ReactionClient.reactionClient(config).build();
  }

  public SerializedClientConfig serializedClientConfig() {
    return SerializedClientConfig.serializedConfig()
        .accessKey(serializedAccessKey)
        .secretAccessKey(serializedSecretAccessKey)
        .build();
  }

}
