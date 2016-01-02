package scripts;

import java.awt.Graphics;
import java.util.ArrayList;

import org.tribot.script.Script;
import org.tribot.script.interfaces.Painting;

import scripts.atmosphere.mta.MtaData;
import scripts.atmosphere.mta.MtaSettings;
import scripts.atmosphere.mta.alchemy.AlchemyTracker;
import scripts.atmosphere.mta.handlers.Gains;
import scripts.atmosphere.mta.handlers.Walker;
import scripts.atmosphere.mta.handlers.alchemy.Alchemist;
import scripts.atmosphere.mta.handlers.alchemy.Depositer;
import scripts.atmosphere.mta.handlers.alchemy.GuardianListener;
import scripts.atmosphere.mta.handlers.graveyard.Boner;
import scripts.atmosphere.scripts.ConditionHandler;

public class MageTraining extends Script implements Painting {
  
  private static final String SCRIPT_NAME = "Mage training arena";
  private static final long SLEEP_TIME = 50;
  
  private MtaSettings m_settings;
  private MtaData m_data;
  private AlchemyTracker m_tracker;
  private ArrayList<ConditionHandler> m_handlers;

  public MageTraining() {
    m_settings = new MtaSettings();
    m_data = new MtaData();
    m_tracker = new AlchemyTracker();
    m_handlers = new ArrayList<ConditionHandler>();
    // Shared handlers
    m_handlers.add(new Gains(this, m_settings, m_data));
    //m_handlers.add(new Walker(this, m_settings, m_data));
    // Alchemy handlers
    //m_handlers.add(new Depositer(this, m_settings, m_data));
    //m_handlers.add(new GuardianListener(this, m_settings, m_data, m_tracker));
    //m_handlers.add(new Alchemist(this, m_settings, m_data, m_tracker));
    // Graveyard handlers
    //m_handlers.add(new Boner(this, m_settings, m_data));
  }

  @Override
  public void run() {
    System.out.println("Starting " + SCRIPT_NAME);
   
    while (!m_settings.m_exit) {
      for (ConditionHandler handler : m_handlers) {
        if (handler.shouldRun()) {
          handler.run();
        }
      }
      sleep(SLEEP_TIME);
    }
  }

  @Override
  public void onPaint(Graphics g) {
    for (ConditionHandler handler : m_handlers) {
      if (handler.shouldPaint()) {
        handler.paint(g);
      }
    }
  }

}
