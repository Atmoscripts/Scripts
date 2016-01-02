package scripts.atmosphere.mta.handlers.alchemy;

import java.awt.Graphics;

import org.tribot.api2007.NPCs;
import org.tribot.api2007.types.RSNPC;
import org.tribot.script.Script;

import scripts.atmosphere.mta.MtaData;
import scripts.atmosphere.mta.MtaMode;
import scripts.atmosphere.mta.MtaSettings;
import scripts.atmosphere.mta.alchemy.AlchemyTracker;
import scripts.atmosphere.scripts.ConditionHandler;

public class GuardianListener implements ConditionHandler, Runnable {
  
  private Script m_script;
  private MtaSettings m_settings;
  private MtaData m_data;
  private AlchemyTracker m_tracker;
  private Thread m_thread;
  
  /// Last guardian update time, so we don't just keep resetting forever
  private long m_lastUpdate;
  
  public GuardianListener(Script script, MtaSettings settings, MtaData data, AlchemyTracker tracker) {
    m_script = script;
    m_settings = settings;
    m_data = data;
    m_tracker = tracker;
    m_lastUpdate = 0;
    
    m_thread = new Thread(this);
    m_thread.start();
  }

  @Override
  public boolean shouldRun() {
    return m_settings.m_currentMode == MtaMode.ALCHEMIST;
  }

  @Override
  public void run() {
    while (!m_settings.m_exit) {
      if (System.currentTimeMillis() - m_lastUpdate > 5000) {
        RSNPC[] npc = NPCs.find("Alchemy Guardian");
        if (npc.length > 0) {
          RSNPC guardian = npc[0];
          if (guardian.getChatMessage() != null) {
            System.out.println("Guardian speaking");
            if (guardian.getChatMessage().contains("The costs are changing")) {
              System.out.println("Costs changing. Resetting");
              m_tracker.reset();
              m_tracker.updatePrices();
              m_lastUpdate = System.currentTimeMillis();
            }
            else if (guardian.getChatMessage().contains("is free to convert")) {
              System.out.println("Costs changing. Resetting");
              m_tracker.reset();
              m_tracker.updatePrices();
              m_lastUpdate = System.currentTimeMillis();
            }
          }
        }
      }
      
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
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
