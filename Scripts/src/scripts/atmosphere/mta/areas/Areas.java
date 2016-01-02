package scripts.atmosphere.mta.areas;

import java.util.Arrays;
import java.util.HashSet;

import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;

public class Areas {
  public final static RSArea GROUND_FLOOR;
  public final static RSArea ALCHEMISTS_AREA;
  public final static RSArea GRAVEYARD_AREA;

  static {
    GROUND_FLOOR = new RSArea(new RSTile(3350, 3294, 0), new RSTile(3373, 3324, 0));
    ALCHEMISTS_AREA = new RSArea(new RSTile(3354, 9620, 2), new RSTile(3375, 9651, 2));
    GRAVEYARD_AREA = new RSArea(new RSTile(3344, 9659, 1), new RSTile(3383, 9620, 1));
  }
}
