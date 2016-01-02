package scripts.atmosphere.scripts;

import java.awt.Graphics;

public interface ConditionHandler {
  public boolean shouldRun();
  public void run();
  public boolean shouldPaint();
  public void paint(Graphics g);
}
