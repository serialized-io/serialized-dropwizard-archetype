#set( $aggregateRootLc = $aggregateRoot.toLowerCase() )
package ${package}.api;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public class Finish${aggregateRoot}Command {

  @NotNull
  public UUID ${aggregateRootLc}Id;

}
