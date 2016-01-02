package scripts;

import java.util.ArrayList;

import org.tribot.script.Script;

import scripts.atmosphere.blackjack.BlackjackSettings;
import scripts.atmosphere.blackjack.handlers.Blackjacker;
import scripts.atmosphere.blackjack.handlers.Escape;
import scripts.atmosphere.blackjack.handlers.Healer;
import scripts.atmosphere.blackjack.handlers.LureIn;
import scripts.atmosphere.blackjack.handlers.LureOut;
import scripts.atmosphere.blackjack.handlers.Unnote;
import scripts.atmosphere.scripts.ConditionHandler;

public class Blackjack extends Script {
  
  private static final String SCRIPT_NAME = "Blackjack";
  private static final long SLEEP_TIME = 50;
  
  private BlackjackSettings m_settings;
  private ArrayList<ConditionHandler> m_handlers;

  public Blackjack() {
    m_settings = new BlackjackSettings();
    m_handlers = new ArrayList<ConditionHandler>();
    m_handlers.add(new Healer(this, m_settings));
    m_handlers.add(new Escape(this, m_settings));
    m_handlers.add(new LureOut(this, m_settings));
    m_handlers.add(new LureIn(this, m_settings));
    m_handlers.add(new Unnote(this, m_settings));
    m_handlers.add(new Blackjacker(this, m_settings));
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

}
