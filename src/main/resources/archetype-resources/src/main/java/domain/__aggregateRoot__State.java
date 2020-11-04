package ${package}.domain;

import ${package}.domain.event.${aggregateRoot}Finished;
import ${package}.domain.event.${aggregateRoot}Started;
import io.serialized.client.aggregate.Event;

public class ${aggregateRoot}State {

  private boolean started;
  private boolean finished;

  public ${aggregateRoot}State handle${aggregateRoot}Started(Event<${aggregateRoot}Started> event) {
    this.started = true;
    return this;
  }

  public ${aggregateRoot}State handle${aggregateRoot}Finished(Event<${aggregateRoot}Finished> event) {
    this.finished = true;
    return this;
  }

  public boolean isStarted() {
    return started;
  }

  public boolean isFinished() {
    return finished;
  }

}
