package scripts.atmosphere.blackjack.data.areas;

import org.tribot.api.types.generic.Filter;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;

public interface Area {
  public RSArea area();
  public RSTile lureOutPos();
  public RSTile spotInsideCurtain();
  public RSTile spotOutsideCurtain();
  public Filter<RSObject> curtainOpenFilter();
  public Filter<RSObject> curtainClosedFilter();
}
