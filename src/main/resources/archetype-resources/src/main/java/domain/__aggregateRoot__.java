#set( $aggregateRootLc = $aggregateRoot.toLowerCase() )
package ${package}.domain;

import com.google.common.base.Preconditions;
import io.serialized.client.aggregate.Event;

import java.util.UUID;

import static ${package}.domain.event.${aggregateRoot}Finished.newFinishedEvent;
import static ${package}.domain.event.${aggregateRoot}Started.newStartedEvent;

public class ${aggregateRoot} {

  private final boolean started;
  private final boolean finished;

  public ${aggregateRoot}(${aggregateRoot}State state) {
    this.started = state.isStarted();
    this.finished = state.isFinished();
  }

  public Event<?> start(UUID ${aggregateRootLc}Id) {
    Preconditions.checkState(!started, "Already started!");

    return newStartedEvent(${aggregateRootLc}Id, System.currentTimeMillis());
  }

  public Event<?> finish(UUID ${aggregateRootLc}Id) {
    Preconditions.checkState(started, "Not started!");
    Preconditions.checkState(!finished, "Already finished!");

    return newFinishedEvent(${aggregateRootLc}Id, System.currentTimeMillis());
  }

}
