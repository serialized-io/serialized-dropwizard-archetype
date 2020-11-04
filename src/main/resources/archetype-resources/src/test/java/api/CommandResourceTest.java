#set( $aggregateRootLc = $aggregateRoot.toLowerCase() )
package ${package}.api;

import ${package}.${applicationName}Config;
import ${package}.domain.${aggregateRoot}State;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import io.dropwizard.testing.junit5.DropwizardClientExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import io.serialized.client.SerializedClientConfig;
import io.serialized.client.aggregate.AggregateClient;
import io.serialized.client.aggregate.EventBatch;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.ws.rs.core.Response;
import java.util.UUID;

import static ${package}.domain.event.${aggregateRoot}Started.newStartedEvent;
import static ${package}.api.EventTypeMatcher.containsEventType;

import static java.util.Collections.singletonList;
import static javax.ws.rs.client.Entity.json;
import static javax.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(DropwizardExtensionsSupport.class)
public class CommandResourceTest {

  private static final AggregateApiStub.AggregateApiCallback aggregateApiCallback = mock(AggregateApiStub.AggregateApiCallback.class);
  private static final DropwizardClientExtension dropwizard = new DropwizardClientExtension(new AggregateApiStub(aggregateApiCallback));

  private final AggregateClient<${aggregateRoot}State> aggregateClient = new ${applicationName}Config().${aggregateRootLc}AggregateClient(createConfig(dropwizard));
  private final CommandResource commandResource = new CommandResource(aggregateClient);

  private final ResourceExtension resources = ResourceExtension.builder()
      .setTestContainerFactory(new GrizzlyWebTestContainerFactory())
      .addProvider(commandResource)
      .build();

  @BeforeEach
  public void setUp() {
    reset(aggregateApiCallback);
    dropwizard.getObjectMapper().setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
  }

  @Test
  public void testStart() {

    Start${aggregateRoot}Command command = new Start${aggregateRoot}Command();
    command.${aggregateRootLc}Id = UUID.randomUUID();

    when(aggregateApiCallback.eventsStored(eq(command.${aggregateRootLc}Id), any(EventBatch.class))).thenReturn(OK);

    Response response = resources.target("/commands/start-${aggregateRootLc}").request().post(json(command));

    // then
    assertThat(response.getStatus()).isEqualTo(201);
    verify(aggregateApiCallback, times(1)).eventsStored(eq(command.${aggregateRootLc}Id), argThat(containsEventType("${aggregateRoot}Started")));
  }

  @Test
  public void testFinish() {

    Finish${aggregateRoot}Command command = new Finish${aggregateRoot}Command();
    command.${aggregateRootLc}Id = UUID.randomUUID();

    AggregateApiStub.AggregateResponse aggregateResponse = new AggregateApiStub.AggregateResponse(
        command.${aggregateRootLc}Id.toString(), "${aggregateRootLc}", 1, singletonList(
            newStartedEvent(command.${aggregateRootLc}Id, System.currentTimeMillis()
        )
    ));

    when(aggregateApiCallback.aggregateLoaded(eq("${aggregateRootLc}"), eq(command.${aggregateRootLc}Id))).thenReturn(aggregateResponse);
    when(aggregateApiCallback.eventsStored(eq(command.${aggregateRootLc}Id), any(EventBatch.class))).thenReturn(OK);

    Response response = resources.target("/commands/finish-${aggregateRootLc}").request().post(json(command));

    assertThat(response.getStatus()).isEqualTo(200);
    verify(aggregateApiCallback, times(1)).eventsStored(eq(command.${aggregateRootLc}Id), argThat(containsEventType("${aggregateRoot}Finished")));
  }

  public static SerializedClientConfig createConfig(DropwizardClientExtension dropwizard) {
    return new SerializedClientConfig.Builder()
        .rootApiUrl(dropwizard.baseUri() + "/api-stub")
        .accessKey("dummy")
        .secretAccessKey("dummy")
        .build();
  }

}
