package ui.components;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JLabel;
import ui.Constants;

public class FASectionLabel extends JLabel {
  
  private Component parent;
  
  public FASectionLabel(String text, Component parent) {
    super(text);
    this.parent = parent;
    this.setBackground(Constants.BACKGROUND_COLOR2);
  }
  
  @Override
  public Dimension getPreferredSize() {
    return new Dimension(parent.getPreferredSize().width, super.getPreferredSize().height);
  }
  
}
