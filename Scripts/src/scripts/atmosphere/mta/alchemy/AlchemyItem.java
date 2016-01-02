package scripts.atmosphere.mta.alchemy;

public enum AlchemyItem {
  LEATHER_BOOTS,
  ADAMANT_KITESHIELD,
  ADAMANT_HELM,
  EMERALD,
  RUNE_LONGSWORD,
  EMPTY,
  UNKNOWN;
  
  private String m_name;
  private int m_id;
  
  static {
    LEATHER_BOOTS.m_name = "Leather boots";
    ADAMANT_KITESHIELD.m_name = "Leather boots";
    ADAMANT_HELM.m_name = "Leather boots";
    EMERALD.m_name = "Leather boots";
    RUNE_LONGSWORD.m_name = "Leather boots";
    EMPTY.m_name = "Empty";
    UNKNOWN.m_name = "Unknown";
    
    LEATHER_BOOTS.m_id = 6893;
    ADAMANT_KITESHIELD.m_id = 6894;
    ADAMANT_HELM.m_id = 6895;
    EMERALD.m_id = 6896;
    RUNE_LONGSWORD.m_id = 6897;
    EMPTY.m_id = -1;
    UNKNOWN.m_id = -2;
  }
  
  public String prettyName() {
    return m_name;
  }
  
  public int getId() {
    return m_id;
  }
  
  public static AlchemyItem nameToItem(String name) {
    switch (name) {
      case "Leather boots":
        return AlchemyItem.LEATHER_BOOTS;
      case "Adamant kiteshield":
        return AlchemyItem.ADAMANT_KITESHIELD;
      case "Adamant med helm":
        return AlchemyItem.ADAMANT_HELM;
      case "Emerald":
        return AlchemyItem.EMERALD;
      case "Rune longsword":
        return AlchemyItem.RUNE_LONGSWORD;
      default:
        return AlchemyItem.EMPTY;
    }
  }
}
