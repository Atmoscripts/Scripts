package scripts.atmosphere.mta;

public class MtaSettings {
  
  /// The current mode
  public MtaMode m_currentMode = MtaMode.GRAVEYARD;
  /// Whether the script should exit at the end of this update
  public boolean m_exit = false;
  
  /// How many coins to deposit at (alchemy mode)
  public int m_coinsDepositAt = 10000;
  /// Whether to prefer the free item (alchemy mode)
  public boolean m_preferFree = true;
  
  /// What health % to eat at (graveyard mode)
  public float m_eatHealth = 0.5f;
  /// What health % to eat to (graveyard mode)
  public float m_eatTo = 0.95f;
}
