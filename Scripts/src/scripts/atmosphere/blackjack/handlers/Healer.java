package scripts.atmosphere.blackjack.handlers;

import java.awt.Graphics;

import org.tribot.api2007.Inventory;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.types.RSItem;
import org.tribot.script.Script;

import scripts.atmosphere.blackjack.BlackjackSettings;
import scripts.atmosphere.scripts.ConditionHandler;

public class Healer implements ConditionHandler {
  
  private Script m_script;
  private BlackjackSettings m_settings;
  
  public Healer(Script script, BlackjackSettings settings) {
    m_script = script;
    m_settings = settings;
  }

  @Override
  public boolean shouldRun() {
    return Skills.getCurrentLevel(SKILLS.HITPOINTS) <= m_settings.m_eatHealth;
  }

  @Override
  public void run() {
    System.out.println("Low health.. Healing");
    
    RSItem[] item = Inventory.find(m_settings.m_foodId);
    if (item.length > 0) {
      item[0].click("Eat");
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
