#set( $aggregateRootLc = $aggregateRoot.toLowerCase() )
#set( $aggregateRootLcPlural = $aggregateRootLc + "s" )
package ${package};

import ${package}.api.CommandResource;
import ${package}.api.QueryResource;
import ${package}.domain.${aggregateRoot}State;
import ${package}.domain.event.${aggregateRoot}Finished;
import ${package}.domain.event.${aggregateRoot}Started;
import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.serialized.client.SerializedClientConfig;
import io.serialized.client.aggregate.AggregateClient;
import io.serialized.client.projection.ProjectionClient;
import io.serialized.client.reaction.ReactionClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;
import static io.serialized.client.projection.EventSelector.eventSelector;
import static io.serialized.client.projection.Functions.inc;
import static io.serialized.client.projection.Functions.unset;
import static io.serialized.client.projection.Functions.set;
import static io.serialized.client.projection.ProjectionDefinition.singleProjection;
import static io.serialized.client.projection.RawData.rawData;
import static io.serialized.client.projection.TargetSelector.targetSelector;

public class ${applicationName} extends Application<${applicationName}Config> {

  private static final Logger LOGGER = LoggerFactory.getLogger(${applicationName}.class);

  @Override
  public void initialize(Bootstrap<${applicationName}Config> bootstrap) {
    bootstrap.setConfigurationSourceProvider(new SubstitutingSourceProvider(bootstrap.getConfigurationSourceProvider(),
        new EnvironmentVariableSubstitutor(false))
    );
  }

  @Override
  public void run(${applicationName}Config config, Environment environment) {

    environment.getObjectMapper() // Customize JSON handling
        .configure(FAIL_ON_UNKNOWN_PROPERTIES, false) // Be tolerant
        .configure(INDENT_OUTPUT, true) // Look nice
        .setSerializationInclusion(NON_NULL); // Ignore fields with null value

    SerializedClientConfig serializedClientConfig = config.serializedClientConfig();
    AggregateClient<${aggregateRoot}State> ${aggregateRootLc}AggregateClient = config.${aggregateRootLc}AggregateClient(serializedClientConfig);
    ProjectionClient projectionClient = config.projectionClient(serializedClientConfig);
    ReactionClient reactionClient = config.reactionClient(serializedClientConfig);

    // Register endpoints
    environment.jersey().register(new CommandResource(${aggregateRootLc}AggregateClient));
    environment.jersey().register(new QueryResource(projectionClient));

    environment.lifecycle().addServerLifecycleListener(server -> {

      // Create/update your Serialized Projections & Reactions here!
      projectionClient.createOrUpdate(singleProjection("${aggregateRootLcPlural}") // Name projection
          .feed("${aggregateRootLc}") // Event feed to project
          .addHandler(${aggregateRoot}Started.class.getSimpleName(),
              set()
                  .with(targetSelector("status"))
                  .with(rawData("STARTED"))
                  .build())
          .addHandler(${aggregateRoot}Finished.class.getSimpleName(),
              set()
                  .with(targetSelector("status"))
                  .with(rawData("FINISHED"))
                  .build())
          .build());

      LOGGER.info("Setup complete!");
    });
  }

  public static void main(String[] args) throws Exception {
    try {
      LOGGER.info("Starting application...");
      new ${applicationName}().run(args);
    } catch (Exception e) {
      LOGGER.error("Unable to start application", e);
      throw e;
    }
  }

}
