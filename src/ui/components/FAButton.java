package ui.components;

import java.awt.Color;
import javax.swing.JButton;
import ui.Constants;

public class FAButton extends JButton {
  
  protected Color defaultColor;

  public FAButton(String label) {
    super(label);
    this.setBackground(Constants.BUTTON_COLOR);
    this.defaultColor = Constants.BUTTON_COLOR;
    this.setOpaque(true);
    this.setBorderPainted(false);
    this.setFocusable(false);
  }
  
  public FAButton(String label, Color color) {
    super(label);
    this.setBackground(color);
    this.defaultColor = color;
    this.setOpaque(true);
    this.setBorderPainted(false);
    this.setFocusable(false);
  }
  
  public Color getDefaultColor() {
    return this.defaultColor;
  }
  
  public void setDefaultBackground() {
    this.setBackground(defaultColor);
  }

}
