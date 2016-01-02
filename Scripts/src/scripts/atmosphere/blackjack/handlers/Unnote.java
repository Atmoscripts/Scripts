package scripts.atmosphere.blackjack.handlers;

import java.awt.Graphics;

import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCChat;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Options;
import org.tribot.api2007.Player;
import org.tribot.api2007.Walking;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;
import org.tribot.script.Script;

import scripts.atmosphere.blackjack.BlackjackSettings;
import scripts.atmosphere.scripts.ConditionHandler;

public class Unnote implements ConditionHandler {
  
  private Script m_script;
  private BlackjackSettings m_settings;
  
  public Unnote(Script script, BlackjackSettings settings) {
    m_script = script;
    m_settings = settings;
  }

  @Override
  public boolean shouldRun() {
    return Inventory.find(m_settings.m_foodId).length == 0 &&
        Inventory.find(m_settings.m_foodNoteId).length > 0;
  }

  @Override
  public void run() {
    System.out.println("Unnoter taking over");
    
    while (shouldRun()) {
      // Open curtain and leave hut if in hut
      if (m_settings.m_area.area().contains(Player.getRSPlayer())) {
        // Open curtain
        RSObject[] curtain = Objects.find(50, m_settings.m_area.curtainClosedFilter());
        if (curtain.length > 0) {
          curtain[0].click("Open");
          m_script.sleep(600);
          while (Player.getRSPlayer().isMoving()) {
            m_script.sleep(50);
          }
        }

        // Leave hut
        Walking.blindWalkTo(m_settings.m_area.spotOutsideCurtain());
        m_script.sleep(600);
        while (Player.getRSPlayer().isMoving()) {
          m_script.sleep(50);
        }
        m_script.sleep(200);
        
        // Close curtain
        curtain = Objects.find(50, m_settings.m_area.curtainOpenFilter());
        if (curtain.length > 0) {
          curtain[0].click("Close");
          m_script.sleep(600);
          while (Player.getRSPlayer().isMoving()) {
            m_script.sleep(50);
          }
        }
      }
        
      Options.setRunOn(true);
      
      // Run to unnoter
      RSNPC[] npcs = NPCs.findNearest(1615);
      if (npcs.length > 0) {
        RSNPC unnoter = npcs[0];
        
        while (!unnoter.isOnScreen()) {
          WebWalking.walkTo(unnoter);
          m_script.sleep(600);
        }
        
        // Get noted food in inventory
        RSItem[] items = Inventory.find(m_settings.m_foodNoteId);
        if (items.length > 0) {
          items[0].click("Use");
          unnoter.click("Use");
          m_script.sleep(800);
        }
        
        NPCChat.selectOption("Exchange All", true);
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
