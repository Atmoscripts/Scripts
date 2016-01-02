package scripts.atmosphere.blackjack.data.npcs;

import org.tribot.api.types.generic.Filter;
import org.tribot.api2007.types.RSNPC;

public class BeardedBandit implements NPC {

  @Override
  public int id() {
    return 1976;
  }

  @Override
  public int levelReq() {
    return 45;
  }

  @Override
  public Filter<RSNPC> filter() {
    return new Filter<RSNPC>() {
      @Override
      public boolean accept(RSNPC npc) {
        return npc.getID() == id();
      }
    };
  }

}
