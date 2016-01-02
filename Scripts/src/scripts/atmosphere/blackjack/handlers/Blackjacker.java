package scripts.atmosphere.blackjack.handlers;

import java.awt.Graphics;

import org.tribot.api.types.generic.Filter;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.Walking;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;
import org.tribot.script.Script;

import scripts.atmosphere.blackjack.BlackjackSettings;
import scripts.atmosphere.scripts.ConditionHandler;

public class Blackjacker implements ConditionHandler {
  
  private Script m_script;
  private BlackjackSettings m_settings;
  
  public Blackjacker(Script script, BlackjackSettings settings) {
    m_script = script;
    m_settings = settings;
  }

  @Override
  public boolean shouldRun() {
    // Run if the rest aren't. This is the lowest priority task
    return true;
  }

  @Override
  public void run() {
    // Open curtain enter if outside hut
    if (!m_settings.m_area.area().contains(Player.getRSPlayer())) {
      // Open curtain
      RSObject[] curtain = Objects.find(50, m_settings.m_area.curtainClosedFilter());
      if (curtain.length > 0) {
        curtain[0].click("Open");
        m_script.sleep(600);
        while (Player.getRSPlayer().isMoving()) {
          m_script.sleep(50);
        }
      }

      // Enter hut
      Walking.blindWalkTo(m_settings.m_area.spotInsideCurtain());
      m_script.sleep(600);
      while (Player.getRSPlayer().isMoving()) {
        m_script.sleep(50);
      }
      m_script.sleep(200);
    }
    
    // Close curtain if open
    RSObject[] curtain = Objects.find(50, m_settings.m_area.curtainOpenFilter());
    if (curtain.length > 0) {
      curtain[0].click("Close");
      m_script.sleep(600);
      while (Player.getRSPlayer().isMoving()) {
        m_script.sleep(50);
      }
    }
    
    // Try blackjacking the bandit
    RSNPC[] npcs = NPCs.findNearest(new Filter<RSNPC>() {
      @Override
      public boolean accept(RSNPC npc) {
        return npc.getID() == m_settings.m_npc.id() &&
            m_settings.m_area.area().contains(npc);
      }
    });
    
    if (npcs.length > 0) {
      RSNPC target = npcs[0];
      
      if (target.getAnimation() != 838 && (target.getChatMessage() == null || !target.getChatMessage().contains("zzz"))) {
        // Knock out until knocked out
        target.click("Knock-Out");
      }

      if (target.getAnimation() == 838 || (target.getChatMessage() != null && target.getChatMessage().contains("zzz"))) {
        // Pickpocket
        target.click("Pickpocket");
        m_script.sleep(300);
        target.click("Pickpocket");
        m_script.sleep(1200);
      }
    }
  }

  @Override
  public boolean shouldPaint() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void paint(Graphics g) {
    // TODO Auto-generated method stub
    
  }

}
