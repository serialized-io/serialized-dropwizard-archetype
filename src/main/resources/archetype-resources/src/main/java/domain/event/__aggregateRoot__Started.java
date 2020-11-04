#set( $aggregateRootLc = $aggregateRoot.toLowerCase() )
package ${package}.domain.event;

import io.serialized.client.aggregate.Event;

import java.util.UUID;

import static io.serialized.client.aggregate.Event.newEvent;

public class ${aggregateRoot}Started {

  private UUID ${aggregateRootLc}Id;
  private long startedAt;

  public static Event<${aggregateRoot}Started> newStartedEvent(UUID ${aggregateRootLc}Id, long startedAt) {
    ${aggregateRoot}Started event = new ${aggregateRoot}Started();
    event.${aggregateRootLc}Id = ${aggregateRootLc}Id;
    event.startedAt = startedAt;
    return newEvent(event).build();
  }

  public UUID get${aggregateRoot}Id() {
    return ${aggregateRootLc}Id;
  }

  public long getStartedAt() {
    return startedAt;
  }

}
