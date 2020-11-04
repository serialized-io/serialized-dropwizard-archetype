#set( $aggregateRootLc = $aggregateRoot.toLowerCase() )
package ${package}.domain.event;

import io.serialized.client.aggregate.Event;

import java.util.UUID;

import static io.serialized.client.aggregate.Event.newEvent;

public class ${aggregateRoot}Finished {

  private UUID ${aggregateRootLc}Id;
  private long finishedAt;

  public static Event<${aggregateRoot}Finished> newFinishedEvent(UUID ${aggregateRootLc}Id, long finishedAt) {
    ${aggregateRoot}Finished event = new ${aggregateRoot}Finished();
    event.${aggregateRootLc}Id = ${aggregateRootLc}Id;
    event.finishedAt = finishedAt;
    return newEvent(event).build();
  }

  public UUID get${aggregateRoot}Id() {
    return ${aggregateRootLc}Id;
  }

  public long getFinishedAt() {
    return finishedAt;
  }

}
