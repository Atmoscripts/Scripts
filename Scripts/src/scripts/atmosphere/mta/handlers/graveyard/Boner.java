package scripts.atmosphere.mta.handlers.graveyard;

import java.awt.Graphics;
import java.util.HashMap;

import org.tribot.api2007.Inventory;
import org.tribot.api2007.Magic;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.Walking;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;
import org.tribot.script.Script;

import scripts.atmosphere.mta.MtaData;
import scripts.atmosphere.mta.MtaMode;
import scripts.atmosphere.mta.MtaSettings;
import scripts.atmosphere.scripts.ConditionHandler;

public class Boner implements ConditionHandler {
  
  private Script m_script;
  private MtaSettings m_settings;
  private MtaData m_data;
  
  public Boner(Script script, MtaSettings settings, MtaData data) {
    m_script = script;
    m_settings = settings;
    m_data = data;
  }

  @Override
  public boolean shouldRun() {
    return m_settings.m_currentMode == MtaMode.GRAVEYARD;
  }

  @Override
  public void run() {
    // Count slots used for not-bones vs how many bananas worth of bones we have
    int slotsUsed = 0;
    int potentialBananas = 0;
    RSItem[] items = Inventory.getAll();
    for (RSItem item : items) {
      // If this isn't a bone count it
      if (item.getID() < 6904 || item.getID() > 6907) {
        slotsUsed++;
      }
      else {
        // Item id    bone amount
        // 6904       1
        // 6905       2
        // 6906       3
        // 6907       4
        potentialBananas += 1 + item.getID() - 6904;
      }
    }
    
    int slotsAvailable = 28 - slotsUsed;

    RSItem[] food = Inventory.find(1963);

    // If have bananas/peaches, deposit them, else collect bones
    if (food.length > 0) {
      // Heal and then deposit
      float eatHealth = m_settings.m_eatHealth * Skills.getActualLevel(SKILLS.HITPOINTS);
      float eatTo = m_settings.m_eatTo * Skills.getActualLevel(SKILLS.HITPOINTS);
      
      if (food.length > 0 && Skills.getCurrentLevel(SKILLS.HITPOINTS) < eatHealth) {
        // Eat
        while (food.length > 0 && Skills.getCurrentLevel(SKILLS.HITPOINTS) < eatTo) {
          food[0].click("Eat");
          m_script.sleep(300, 600);
          food = Inventory.find(1963);
        }
      }
      else {
        // Deposit
        RSObject[] objs = Objects.findNearest(50, "Food chute");
        if (objs.length > 0) {
          while (!objs[0].isOnScreen()) {
            Walking.blindWalkTo(objs[0]);
            m_script.sleep(250);
          }
          objs[0].click("Deposit");
          m_script.sleep(1200);
        }
      }
    }
    else {
      // If we have more than a full load of bananas worth of bones, cast bones to peaches
      if (potentialBananas >= slotsAvailable) {
        // Cast bones to peaches
        Magic.selectSpell("Bones to Bananas");
      }
      else {
        // Collect bananas
        RSObject[] bonePiles = Objects.findNearest(10725, 10726, 10727, 10728);
        if (bonePiles.length > 0) {
          while (!bonePiles[0].isOnScreen()) {
            Walking.blindWalkTo(bonePiles[0]);
            m_script.sleep(250);
          }
          bonePiles[0].click("Grab");
          m_script.sleep(150);
        }
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
