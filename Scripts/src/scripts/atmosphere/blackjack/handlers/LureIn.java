package scripts.atmosphere.blackjack.handlers;

import java.awt.Graphics;
import java.util.Arrays;
import java.util.List;

import org.tribot.api.types.generic.Filter;
import org.tribot.api2007.NPCChat;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Options;
import org.tribot.api2007.Player;
import org.tribot.api2007.Walking;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;
import org.tribot.script.Script;

import scripts.atmosphere.blackjack.BlackjackSettings;
import scripts.atmosphere.scripts.ConditionHandler;

public class LureIn implements ConditionHandler {
  
  private Script m_script;
  private BlackjackSettings m_settings;
  
  public LureIn(Script script, BlackjackSettings settings) {
    m_script = script;
    m_settings = settings;
  }

  @Override
  public boolean shouldRun() {
    return getTargetNPCsInArea().size() == 0;
  }

  @Override
  public void run() {
    System.out.println("LureIn taking over");
    
    while (shouldRun()) {
      // Check curtain open
      RSObject[] curtain = Objects.find(50, m_settings.m_area.curtainClosedFilter());
      if (curtain.length > 0) {
        curtain[0].click("Open");
        m_script.sleep(600);
        while (Player.getRSPlayer().isMoving()) {
          m_script.sleep(50);
        }
      }
      
      // Find target NPC
      List<RSNPC> targetNpcs = getTargetNPCs();
      if (targetNpcs.size() > 0) {
        RSNPC target = targetNpcs.get(0);
        
        Options.setRunOn(true);
        
        while (!target.isOnScreen()) {
          Walking.blindWalkTo(target);
          m_script.sleep(600);
          while (Player.getRSPlayer().isMoving()) {
            m_script.sleep(50);
          }
        }
        
        // Check curtain open
        curtain = Objects.find(50, m_settings.m_area.curtainClosedFilter());
        if (curtain.length > 0) {
          curtain[0].click("Open");
          m_script.sleep(600);
          while (Player.getRSPlayer().isMoving()) {
            m_script.sleep(50);
          }
          Walking.blindWalkTo(m_settings.m_area.area().getRandomTile());
          m_script.sleep(200);
        }
        
        do {
          while (NPCChat.getMessage() == null) {
            if (!target.isOnScreen()) {
              WebWalking.walkTo(target);
            }
            target.click("Lure");
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
        
        Options.setRunOn(false);
        
        int i = 0;
        while (!m_settings.m_area.area().contains(target)) {
          Walking.blindWalkTo(m_settings.m_area.area().getRandomTile());
          m_script.sleep(600);
          while (Player.getRSPlayer().isMoving()) {
            m_script.sleep(50);
          }
          if (i++ > 5) {
            break;
          }
        }
        
        // Stop npc following us
        while (NPCChat.getMessage() == null) {
          target.click("Lure");
          m_script.sleep(750);
        }
        
        while (NPCChat.getMessage() != null) {
          NPCChat.clickContinue(true);
        }
        
        Options.setRunOn(true);
        
        curtain = Objects.find(50, m_settings.m_area.curtainOpenFilter());
        if (curtain.length > 0) {
          curtain[0].click("Close");
          m_script.sleep(600);
          while (Player.getRSPlayer().isMoving()) {
            m_script.sleep(50);
          }
        }        
      }
    }
  }
  
  private List<RSNPC> getTargetNPCs() {
    return Arrays.asList(NPCs.findNearest(m_settings.m_npc.filter()));
  }
  
  private List<RSNPC> getTargetNPCsInArea() {
    return Arrays.asList(NPCs.find(new Filter<RSNPC>() {
      @Override
      public boolean accept(RSNPC npc) {
        return m_settings.m_area.area().contains(npc) &&
            npc.getID() == m_settings.m_npc.id();
      }
    }));
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
