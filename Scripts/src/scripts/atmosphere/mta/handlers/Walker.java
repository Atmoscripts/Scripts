package scripts.atmosphere.mta.handlers;

import java.awt.Graphics;

import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.Walking;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSObject;
import org.tribot.script.Script;

import scripts.atmosphere.mta.MtaData;
import scripts.atmosphere.mta.MtaSettings;
import scripts.atmosphere.mta.areas.Areas;
import scripts.atmosphere.scripts.ConditionHandler;

public class Walker implements ConditionHandler {
  
  private Script m_script;
  private MtaSettings m_settings;
  private MtaData m_data;
  
  public Walker(Script script, MtaSettings settings, MtaData data) {
    m_script = script;
    m_settings = settings;
    m_data = data;
  }

  @Override
  public boolean shouldRun() {
    return Areas.GROUND_FLOOR.contains(Player.getPosition()) || !getTargetArea().contains(Player.getPosition());
  }
  
  public RSArea getTargetArea() {
    switch (m_settings.m_currentMode) {
    case ALCHEMIST: return Areas.ALCHEMISTS_AREA;
    case GRAVEYARD: return Areas.GRAVEYARD_AREA;
    default: return null;
    }
  }
  
  public String getTargetName() {
    switch (m_settings.m_currentMode) {
    case ALCHEMIST: return "Alchemists Teleport";
    case GRAVEYARD: return "Graveyard Teleport";
    default: return null;
    }
  }

  @Override
  public void run() {
    System.out.println("Not in the right area. Running to it");
    
    // If not in the ground floor, look for an exit
    // Otherwise, go to the right entrance
    if (Areas.GROUND_FLOOR.contains(Player.getPosition())) {
      System.out.println("Entering portal");
      RSObject[] objs = Objects.findNearest(50, getTargetName());
      if (objs.length > 0) {
        while (!objs[0].isOnScreen()) {
          Walking.blindWalkTo(objs[0]);
          m_script.sleep(250);
        }
        objs[0].click("Enter");
        m_script.sleep(1200);
      }
      else {
        System.out.println("Can't find way to right room. Exiting");
        m_settings.m_exit = true;
      }
    }
    else {
      System.out.println("This is the wrong room. Leaving it");
      RSObject[] objs = Objects.findNearest(50, "Exit Teleport");
      if (objs.length > 0) {
        while (!objs[0].isOnScreen()) {
          Walking.blindWalkTo(objs[0]);
          m_script.sleep(250);
        }
        objs[0].click("Enter");
        m_script.sleep(1200);
      }
      else {
        System.out.println("Can't find way to right room. Exiting");
        m_settings.m_exit = true;
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
