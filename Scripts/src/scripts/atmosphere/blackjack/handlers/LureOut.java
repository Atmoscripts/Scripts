package scripts.atmosphere.blackjack.handlers;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.tribot.api2007.NPCChat;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Options;
import org.tribot.api2007.Player;
import org.tribot.api2007.Walking;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;
import org.tribot.script.Script;

import scripts.atmosphere.blackjack.BlackjackSettings;
import scripts.atmosphere.blackjack.filters.LurableNpcInArea;
import scripts.atmosphere.scripts.ConditionHandler;

public class LureOut implements ConditionHandler {
  
  private Script m_script;
  private BlackjackSettings m_settings;
  
  public LureOut(Script script, BlackjackSettings settings) {
    m_script = script;
    m_settings = settings;
  }

  @Override
  public boolean shouldRun() {
    return getExtraneousNpcs().size() > 0;
  }

  @Override
  public void run() {
    System.out.println("LureOut taking over");
    
    List<RSNPC> npcs = getExtraneousNpcs();
    System.out.print("Extraneous NPCs in area: " + npcs.size());
    
    Options.setRunOn(false);
    
    while (shouldRun()) {
      npcs = getExtraneousNpcs();
      
      if (npcs.size() > 0) {
        // Try luring the first NPC
        do {
          while (NPCChat.getMessage() == null) {
            npcs.get(0).click("Lure");
            System.out.println("Waiting for options");
            m_script.sleep(750);
          }
          NPCChat.clickContinue(true);
          m_script.sleep(750);
        } while (NPCChat.getMessage() == null || !NPCChat.getMessage().contains("What is it?"));
        
        while (NPCChat.getMessage() != null) {
          NPCChat.clickContinue(true);
          m_script.sleep(100);
        }

        // Make sure curtain is open
        RSObject[] curtains = Objects.findNearest(10, m_settings.m_area.curtainClosedFilter());
        if (curtains.length > 0) {
          curtains[0].click("Open");
          m_script.sleep(600);
          
          while (Player.getRSPlayer().isMoving()) {
            m_script.sleep(50);
          }
        }
        
        // Walk until npc is out of area
        Walking.blindWalkTo(m_settings.m_area.lureOutPos());
        
        int i = 0;
        while (m_settings.m_area.area().contains(npcs.get(0))) {
          m_script.sleep(50);
          if ((i++) > 20) {
            break;
          }
        }
        
        // Stop npc following us
        while (NPCChat.getMessage() == null) {
          npcs.get(0).click("Lure");
          m_script.sleep(750);
        }
        
        while (NPCChat.getMessage() != null) {
          NPCChat.clickContinue(true);
        }
      }
      
      Options.setRunOn(true);
      
      // Walk back to area
      Walking.blindWalkTo(m_settings.m_area.area().getRandomTile());
      
      while (Player.getRSPlayer().isMoving()) {
        m_script.sleep(50);
      }
      
      // Close curtain
      RSObject[] curtains = Objects.findNearest(10, m_settings.m_area.curtainOpenFilter());
      if (curtains.length > 0) {
        curtains[0].click("Close");
        m_script.sleep(600);
        
        while (Player.getRSPlayer().isMoving()) {
          m_script.sleep(50);
        }
      }
    }
  }
  
  private List<RSNPC> getExtraneousNpcs() {
    // Check for NPCs in location
    RSNPC[] npcarr = NPCs.findNearest(new LurableNpcInArea(m_settings.m_area.area()));
    ArrayList<RSNPC> npcs = new ArrayList<RSNPC>(Arrays.asList(npcarr));
    for (int i = 0; i < npcs.size(); ++i) {
      // Allow one NPC of the desired type
      if (npcs.get(i).getID() == m_settings.m_npc.id()) {
        npcs.remove(i);
        break;
      }
    }
    
    return npcs;
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
