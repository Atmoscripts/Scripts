package scripts.atmosphere.blackjack.filters;

import org.tribot.api.types.generic.Filter;
import org.tribot.api2007.types.RSNPC;

public class LurableNpc extends Filter<RSNPC> {
  @Override
  public boolean accept(RSNPC npc) {
    for (final String action : npc.getActions()) {
      if (action.equals("Lure")) {
        return true;
      }
    }
    return false;
  }
}
