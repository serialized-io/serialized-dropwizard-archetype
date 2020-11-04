#set( $aggregateRootLc = $aggregateRoot.toLowerCase() )
package ${package}.domain;

import ${package}.domain.event.${aggregateRoot}Finished;
import ${package}.domain.event.${aggregateRoot}Started;
import io.serialized.client.aggregate.Event;
import io.serialized.client.aggregate.StateBuilder;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static ${package}.domain.event.${aggregateRoot}Started.newStartedEvent;
import static ${package}.domain.event.${aggregateRoot}Finished.newFinishedEvent;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ${aggregateRoot}Test {

  private final StateBuilder<${aggregateRoot}State> ${aggregateRootLc}StateBuilder = StateBuilder.stateBuilder(${aggregateRoot}State.class)
      .withHandler(${aggregateRoot}Started.class, ${aggregateRoot}State::handle${aggregateRoot}Started)
      .withHandler(${aggregateRoot}Finished.class, ${aggregateRoot}State::handle${aggregateRoot}Finished);

  @Test
  public void shouldStart${aggregateRoot}() {
    UUID id = UUID.randomUUID();

    ${aggregateRoot} ${aggregateRootLc} = new ${aggregateRoot}(new ${aggregateRoot}State());
    Event<${aggregateRoot}Started> ${aggregateRootLc}Started = (Event<${aggregateRoot}Started>) ${aggregateRootLc}.start(id);
    assertThat(${aggregateRootLc}Started.data().get${aggregateRoot}Id()).isEqualTo(id);
  }

  @Test
  public void cannotStartIfAlreadyStarted() {
    UUID id = UUID.randomUUID();

    ${aggregateRoot} ${aggregateRootLc} = new ${aggregateRoot}(${aggregateRootLc}StateBuilder.buildState(singletonList(
        newStartedEvent(id, System.currentTimeMillis())
    )));

    Throwable exception = assertThrows(IllegalStateException.class, () -> {
      ${aggregateRootLc}.start(id);
    });

    assertThat(exception.getMessage()).isEqualTo("Already started!");
  }

  @Test
  public void shouldFinish${aggregateRoot}() {
    UUID id = UUID.randomUUID();

    ${aggregateRoot} ${aggregateRootLc} = new ${aggregateRoot}(${aggregateRootLc}StateBuilder.buildState(singletonList(
        newStartedEvent(id, System.currentTimeMillis())
    )));

    Event<${aggregateRoot}Finished> ${aggregateRootLc}Finished = (Event<${aggregateRoot}Finished>) ${aggregateRootLc}.finish(id);
    assertThat(${aggregateRootLc}Finished.data().get${aggregateRoot}Id()).isEqualTo(id);
  }

  @Test
  public void cannotFinishIfNotStarted() {
    UUID id = UUID.randomUUID();

    ${aggregateRoot} ${aggregateRootLc} = new ${aggregateRoot}(new ${aggregateRoot}State());

    Throwable exception = assertThrows(IllegalStateException.class, () -> {
      ${aggregateRootLc}.finish(id);
    });

    assertThat(exception.getMessage()).isEqualTo("Not started!");
  }

  @Test
  public void cannotFinishIfAlreadyFinished() {
    UUID id = UUID.randomUUID();

    ${aggregateRoot} ${aggregateRootLc} = new ${aggregateRoot}(${aggregateRootLc}StateBuilder.buildState(asList(
        newStartedEvent(id, System.currentTimeMillis()),
        newFinishedEvent(id, System.currentTimeMillis())
    )));

    Throwable exception = assertThrows(IllegalStateException.class, () -> {
      ${aggregateRootLc}.finish(id);
    });

    assertThat(exception.getMessage()).isEqualTo("Already finished!");
  }

}
