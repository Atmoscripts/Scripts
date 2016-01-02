package scripts.atmosphere.mta.handlers;

import java.awt.Graphics;

import org.tribot.api2007.Inventory;
import org.tribot.api2007.types.RSItem;
import org.tribot.script.Script;

import scripts.atmosphere.mta.MtaData;
import scripts.atmosphere.mta.MtaSettings;
import scripts.atmosphere.scripts.ConditionHandler;

public class Gains implements ConditionHandler {
  
  private Script m_script;
  private MtaSettings m_settings;
  private MtaData m_data;
  
  private boolean m_initialised;
  
  public Gains(Script script, MtaSettings settings, MtaData data) {
    m_script = script;
    m_settings = settings;
    m_data = data;
    m_initialised = false;
  }

  @Override
  public boolean shouldRun() {
    return true;
  }

  @Override
  public void run() {
    RSItem[] items = Inventory.find(6885);
    System.out.println("Found " + items.length);
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
