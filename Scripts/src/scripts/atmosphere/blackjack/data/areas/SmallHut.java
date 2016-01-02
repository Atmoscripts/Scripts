package scripts.atmosphere.blackjack.data.areas;

import org.tribot.api.types.generic.Filter;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;

import scripts.atmosphere.blackjack.data.ObjectIDs;

public class SmallHut implements Area {
  @Override
  public RSArea area() {
    return new RSArea(
      new RSTile(3363, 3000, 0),
      new RSTile(3364, 3002, 0)
    );
  }

  @Override
  public RSTile lureOutPos() {
    return new RSTile(3364, 2998, 0);
  }
  
  @Override
  public RSTile spotInsideCurtain() {
    return new RSTile(3364, 3000, 0);
  }
  
  @Override
  public RSTile spotOutsideCurtain() {
    return new RSTile(3364, 2999, 0);
  }
  
  @Override
  public Filter<RSObject> curtainOpenFilter() {
    return new Filter<RSObject>() {
      @Override
      public boolean accept(RSObject npc) {
        return npc.getID() == ObjectIDs.CURTAIN_OPEN &&
            npc.getPosition().equals(new RSTile(3364, 2999, 0));
      }
    };
  }
  
  @Override
  public Filter<RSObject> curtainClosedFilter() {
    return new Filter<RSObject>() {
      @Override
      public boolean accept(RSObject npc) {
        return npc.getID() == ObjectIDs.CURTAIN_CLOSED &&
            npc.getPosition().equals(new RSTile(3364, 2999, 0));
      }
    };
  }
}
