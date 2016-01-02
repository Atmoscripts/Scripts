package scripts.atmosphere.mta.handlers.alchemy;

import java.awt.Graphics;
import java.util.ArrayList;

import org.tribot.api.types.generic.Condition;
import org.tribot.api.types.generic.Filter;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Magic;
import org.tribot.api2007.MessageListener;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.Walking;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSObject;
import org.tribot.script.Script;
import org.tribot.script.interfaces.MessageListening07;

import scripts.atmosphere.mta.MtaData;
import scripts.atmosphere.mta.MtaMode;
import scripts.atmosphere.mta.MtaSettings;
import scripts.atmosphere.mta.alchemy.AlchemyItem;
import scripts.atmosphere.mta.alchemy.AlchemyTracker;
import scripts.atmosphere.mta.alchemy.ItemEntry;
import scripts.atmosphere.scripts.ConditionHandler;

public class Alchemist implements ConditionHandler, MessageListening07 {
  
  private Script m_script;
  private MtaSettings m_settings;
  private MtaData m_data;
  private AlchemyTracker m_tracker;
  private boolean m_inventFull = false;
  
  public Alchemist(Script script, MtaSettings settings, MtaData data, AlchemyTracker tracker) {
    m_script = script;
    m_settings = settings;
    m_data = data;
    m_tracker = tracker;
    MessageListener.addListener(this);
  }

  @Override
  public boolean shouldRun() {
    return m_settings.m_currentMode == MtaMode.ALCHEMIST;
  }

  @Override
  public void run() {
    m_tracker.updatePrices();
    ItemEntry topItem = m_tracker.highestPriceItem();
    ItemEntry freeItem = m_tracker.freeItem();
    
    // The item to do
    ItemEntry item;
    if (m_settings.m_preferFree && freeItem.m_itemId > 0)
      item = freeItem;
    else
      item = topItem;
    
    // Reset if there are no unknowns but the item can't be found
    // It probably changed mid update
    if (m_tracker.unknowns().size() == 0 && item.m_itemId < 0) {
      m_tracker.reset();
    }
    
    // Drop junk if inventory full
    if (m_inventFull) {
      m_inventFull = false;
      
      RSItem[] items = Inventory.find(new Filter<RSItem>() {
        @Override
        public boolean accept(RSItem i) {
          if (i.getID() == 6893 || i.getID() == 6894 || i.getID() == 6895 ||
              i.getID() == 6896 || i.getID() == 6897) {
            if (i.getID() != item.m_itemId) {
              return true;
            }
          }
          return false;
        }
      });
      
      for (RSItem i : items) {
        i.click("Drop");
      }
    }
    
    // Alch and then do other stuff during the animation
    RSItem[] items = Inventory.find(item.m_itemId);
    if (items.length > 0) {
      Magic.selectSpell("High Level Alchemy");
      items[0].click("Cast");
    }
    
    // Identify unknown cupboards until we know them all
    ArrayList<RSObject> unknowns = m_tracker.unknowns();
    if (!unknowns.isEmpty()) {
      RSObject cupboard = unknowns.get(0);
      // Walk to cupboard if not visible
      if (!cupboard.isOnScreen()) {
        Walking.blindWalkTo(cupboard);
      }
      else {
        // Search cupboard
        cupboard.click("Search");
        m_script.sleep(800);
        while (Player.isMoving()) {
          m_script.sleep(50);
        }
      }
    }
    else {
      // Go loot the cupboard for the best item
      if (item.m_itemId > 0) {
        RSObject[] objs = Objects.findNearest(50, item.m_cupboardId, item.m_cupboardId+1);
        if (objs.length > 0) {
          openCupboard(objs[0]);
        }
      }
    }
  }

  @Override
  public void serverMessageReceived(String msg) {
    if (msg.contains("The cupboard is empty")) {
      RSObject[] objs = Objects.findNearest(2, "Cupboard");
      if (objs.length > 0) {
        m_tracker.update(objs[0].getID(), AlchemyItem.EMPTY);
      }
    }
    else if (msg.contains("You found:")) {
      String itemName = msg.substring(11);
      RSObject[] objs = Objects.findNearest(2, "Cupboard");
      if (objs.length > 0) {
        m_tracker.update(objs[0].getID(), AlchemyItem.nameToItem(itemName));
      }
    }
    else if (msg.contains("You have no free space")) {
      m_inventFull = true;
      System.out.println("Inventory full. Dropping junk");
    }
  }

  @Override
  public void clanMessageReceived(String arg0, String arg1) {}
  @Override
  public void duelRequestReceived(String arg0, String arg1) {}
  @Override
  public void personalMessageReceived(String arg0, String arg1) {}
  @Override
  public void playerMessageReceived(String arg0, String arg1) {}
  @Override
  public void tradeRequestReceived(String arg0) {}

  @Override
  public boolean shouldPaint() {
    return m_settings.m_currentMode == MtaMode.ALCHEMIST;
  }

  @Override
  public void paint(Graphics g) {
    m_tracker.paint(g);
  }
  
  private void openCupboard(RSObject cupboard) {
    // Walk to cuboard
    Walking.blindWalkTo(cupboard, new Condition() {
      @Override
      public boolean active() {
        return cupboard.isOnScreen();
      }
    }, 50);
    
    // Search cupboard
    cupboard.click("Search");
    m_script.sleep(600, 1200);
    while (Player.isMoving()) {
      m_script.sleep(50);
    }
  }
}
