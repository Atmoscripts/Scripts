package scripts.atmosphere.blackjack.data.npcs;

import org.tribot.api.types.generic.Filter;
import org.tribot.api2007.types.RSNPC;

public interface NPC {
  public int id();
  public int levelReq();
  public Filter<RSNPC> filter();
}
