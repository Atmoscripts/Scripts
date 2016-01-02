package scripts.atmosphere.blackjack.filters;

import org.tribot.api.types.generic.Filter;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSNPC;

public class LurableNpcInArea extends Filter<RSNPC> {
  
  private RSArea m_area;
  
  public LurableNpcInArea(RSArea area) {
    m_area = area;
  }
  
  @Override
  public boolean accept(RSNPC npc) {
    if (m_area.contains(npc)) {
      for (final String action : npc.getActions()) {
        if (action.equals("Lure")) {
          return true;
        }
      }
    }
    return false;
  }
}
