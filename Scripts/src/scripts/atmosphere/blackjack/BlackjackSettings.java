package scripts.atmosphere.blackjack;

import scripts.atmosphere.blackjack.data.areas.Area;
import scripts.atmosphere.blackjack.data.areas.SmallHut;
import scripts.atmosphere.blackjack.data.npcs.NPC;
import scripts.atmosphere.blackjack.data.npcs.UnbeardedBandit;

public class BlackjackSettings {
  /// The NPC we're blackjacking
  public NPC m_npc = new UnbeardedBandit();
  /// The location we're blackjacking in
  public Area m_area = new SmallHut();
  /// The ID of the food we're using
  public int m_foodId = 379;
  /// The ID of the noted food we're using
  public int m_foodNoteId = 380;
  /// Health to eat at
  public int m_eatHealth = 28;
  /// Whether the script should exit at the end of this update
  public boolean m_exit = false;
}
