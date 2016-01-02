package scripts.atmosphere.mta.alchemy;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;

import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Objects;
import org.tribot.api2007.types.RSInterfaceMaster;
import org.tribot.api2007.types.RSObject;

import scripts.atmosphere.mta.MtaConstants;

public class AlchemyTracker {
  private final static int[] CUPBOARD_IDS = { 23678, 23680, 23682, 23684, 23686, 23688, 23690, 23692 };
  private final static AlchemyItem[] ITEMS = {
      AlchemyItem.LEATHER_BOOTS, AlchemyItem.ADAMANT_KITESHIELD, AlchemyItem.ADAMANT_HELM,
      AlchemyItem.EMERALD, AlchemyItem.RUNE_LONGSWORD, AlchemyItem.EMPTY, AlchemyItem.EMPTY,
      AlchemyItem.EMPTY
    };
  
  // The current items per cupboard, according to the ids above
  private AlchemyItem[] m_currentItems;
  
  // The current item prices, updated by calling updatePrices()
  private int[] m_currentPrices;
  
  public AlchemyTracker() {
    m_currentItems = new AlchemyItem[8];
    m_currentPrices = new int[8];
    reset();
    updatePrices();
  }
  
  /// Reset item locations to unknown
  public void reset() {
    Arrays.fill(m_currentItems, AlchemyItem.UNKNOWN);
    Arrays.fill(m_currentPrices, -1);
  }
  
  /// Update prices from gui
  public void updatePrices() {
    RSInterfaceMaster alchemyInterface = Interfaces.get(MtaConstants.INTERFACE_ID);
    if (alchemyInterface != null) {
      for (int i = 0; i < 5; ++i) {
        int price = Integer.parseInt(alchemyInterface.getChild(i+10).getText());
        m_currentPrices[i] = price;
      }
    }
  }
  
  /// Get all unknown cupboards by distance
  public ArrayList<RSObject> unknowns() {
    ArrayList<RSObject> unknowns = new ArrayList<RSObject>();

    RSObject[] cupboards = Objects.findNearest(100, 23678, 23679, 23680, 23681, 23682,
        23683, 23684, 23685, 23686, 23687, 23688, 23689, 23690, 23691, 23692, 23693);
    
    for (RSObject cupboard : cupboards) {
      if (m_currentItems[cupboardIndex(cupboard.getID())] == AlchemyItem.UNKNOWN) {
        unknowns.add(cupboard);
      }
    }
    
    return unknowns;
  }
  
  /// Highest price item
  public ItemEntry highestPriceItem() {
    // Get highest price item by enum
    for (int i = 0; i < 5; ++i) {
      if (m_currentPrices[i] == 30) {
        AlchemyItem item = ITEMS[i];
        return getItemEntry(item);
      }
    }
    return new ItemEntry(-1, -1);
  }
  
  /// Free item
  public ItemEntry freeItem() {
    RSInterfaceMaster gui = Interfaces.get(MtaConstants.INTERFACE_ID);

    if (gui != null) {
      if (!gui.getChild(20).isHidden())
        return getItemEntry(AlchemyItem.LEATHER_BOOTS);
      else if (!gui.getChild(21).isHidden())
        return getItemEntry(AlchemyItem.ADAMANT_KITESHIELD);
      else if (!gui.getChild(22).isHidden())
        return getItemEntry(AlchemyItem.ADAMANT_HELM);
      else if (!gui.getChild(23).isHidden())
        return getItemEntry(AlchemyItem.EMERALD);
      else if (!gui.getChild(24).isHidden())
        return getItemEntry(AlchemyItem.RUNE_LONGSWORD);
    }
    
    return new ItemEntry(-1, -1);
  }
  
  public ItemEntry getItemEntry(AlchemyItem item) {
    // Find cupboard
    for (int j = 0; j < 8; ++j) {
      if (m_currentItems[j] == item) {
        return new ItemEntry(CUPBOARD_IDS[j], item.getId());
      }
    }
    return new ItemEntry(-1, -1);
  }
  
  /// Update item locations
  public void update(int cupboardId, AlchemyItem item) {
    final int idx = cupboardIndex(cupboardId);
    if (item == AlchemyItem.EMPTY) {
      m_currentItems[idx] = item;
    }
    else {
      int startIdx;
      for (startIdx = 0; startIdx < 8; ++startIdx) {
        if (ITEMS[startIdx] == item) {
          break;
        }
      }
      for (int i = idx; i < 8; ++i) {
        m_currentItems[i] = ITEMS[(startIdx+i-idx)%8];
      }
      for (int i = 0; i < idx; ++i) {
        m_currentItems[i] = ITEMS[(startIdx+8+i-idx)%8];
      }
    }
  }
  
  /// Finds an alchemy item and returns the cupboard id, or -1 if it couldn't be found
  public int findItem(AlchemyItem item) {
    for (int i = 0; i < 8; ++i) {
      if (m_currentItems[8] == item) {
        return CUPBOARD_IDS[i];
      }
    }
    return -1;
  }
  
  /// Convert cupboard id to index
  public int cupboardIndex(int id) {
    // Minus one if it's an open cupboard id
    if (id % 2 == 1) {
      id -= 1;
    }
    return (id - CUPBOARD_IDS[0]) / 2;
  }
  
  /// Convert index to cupboard id
  public int cupboardId(int idx) {
    return CUPBOARD_IDS[idx];
  }
  
  public void paint(Graphics g) {
    RSObject[] cupboards = Objects.find(100, 23678, 23679, 23680, 23681, 23682,
        23683, 23684, 23685, 23686, 23687, 23688, 23689, 23690, 23691, 23692, 23693);
    
    g.setColor(Color.GREEN);
    
    for (RSObject cupboard : cupboards) {
      if (cupboard.isOnScreen()) {
        Point screenPos = cupboard.getModel().getCentrePoint();
        String name = m_currentItems[cupboardIndex(cupboard.getID())].toString();
        g.drawString(name, screenPos.x, screenPos.y);
      }
    }
  }
}
