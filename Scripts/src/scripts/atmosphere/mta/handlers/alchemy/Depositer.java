package scripts.atmosphere.mta.handlers.alchemy;

import java.awt.Graphics;

import org.tribot.api2007.Inventory;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.Walking;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSObject;
import org.tribot.script.Script;

import scripts.atmosphere.mta.MtaData;
import scripts.atmosphere.mta.MtaMode;
import scripts.atmosphere.mta.MtaSettings;
import scripts.atmosphere.scripts.ConditionHandler;

public class Depositer implements ConditionHandler {
  
  private Script m_script;
  private MtaSettings m_settings;
  private MtaData m_data;
  
  public Depositer(Script script, MtaSettings settings, MtaData data) {
    m_script = script;
    m_settings = settings;
    m_data = data;
  }

  @Override
  public boolean shouldRun() {
    if (m_settings.m_currentMode == MtaMode.ALCHEMIST) {
      RSItem[] coins = Inventory.find(8890);
      if (coins.length > 0) {
        if (coins[0].getStack() > m_settings.m_coinsDepositAt) {
          System.out.println("Have " + coins[0].getStack() + " coins");
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public void run() {
    System.out.println("Depositer taking over");
    
    RSObject[] obj = Objects.find(50, "Coin Collector");
    if (obj.length > 0) {
      while (!obj[0].isOnScreen()) {
        Walking.blindWalkTo(obj[0]);
        m_script.sleep(250);
      }
      
      // Deposit coins
      obj[0].click("Deposit");
      m_script.sleep(600, 1200);
      while (Player.isMoving()) {
        m_script.sleep(50);
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
