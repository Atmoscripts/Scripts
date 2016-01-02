package scripts.atmosphere.blackjack.handlers;

import java.awt.Graphics;

import org.tribot.api2007.MessageListener;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSObject;
import org.tribot.script.Script;
import org.tribot.script.interfaces.MessageListening07;

import scripts.atmosphere.blackjack.BlackjackSettings;
import scripts.atmosphere.scripts.ConditionHandler;

public class Escape implements ConditionHandler, MessageListening07 {
  
  private Script m_script;
  private BlackjackSettings m_settings;
  
  private boolean m_needEscape = false;
  
  public Escape(Script script, BlackjackSettings settings) {
    m_script = script;
    m_settings = settings;
    
    MessageListener.addListener(this);
  }

  @Override
  public boolean shouldRun() {
    return m_needEscape || Player.getRSPlayer().getPosition().getPlane() == 1;
  }

  @Override
  public void run() {
    System.out.println("In combat. Escaping");
    
    if (Player.getRSPlayer().getPosition().getPlane() == 0) {
      // Go up ladder
      RSObject[] objs = Objects.find(5, 6261);
      if (objs.length > 0)
        objs[0].click("Climb-up");
      
      m_script.sleep(600);
    }
    else {
      // Go down ladder
      RSObject[] objs = Objects.find(5, 6260);
      if (objs.length > 0)
        objs[0].click("Climb-down");
      
      m_script.sleep(600);
    }
    
    m_needEscape = false;
  }

  @Override
  public void clanMessageReceived(String arg0, String arg1) {
  }

  @Override
  public void duelRequestReceived(String arg0, String arg1) {
  }

  @Override
  public void personalMessageReceived(String arg0, String arg1) {
  }

  @Override
  public void playerMessageReceived(String arg0, String arg1) {
  }

  @Override
  public void serverMessageReceived(String msg) {
    if (msg.contains("You can't")) {
      m_needEscape = true;
    }
  }

  @Override
  public void tradeRequestReceived(String arg0) {
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
