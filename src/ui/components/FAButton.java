package ui.components;

import java.awt.Color;
import javax.swing.JButton;
import core.Constants;

/**
 *
 * @author sambryant
 */
public class FAButton extends JButton {

  /**
   *
   */
  protected Color defaultColor;

  /**
   *
   * @param label
   */
  public FAButton(String label) {
    super(label);
    this.setBackground(Constants.BUTTON_COLOR);
    this.defaultColor = Constants.BUTTON_COLOR;
    this.setOpaque(true);
    this.setBorderPainted(false);
    this.setFocusable(false);
  }

  /**
   *
   * @param label
   * @param color
   */
  public FAButton(String label, Color color) {
    super(label);
    this.setBackground(color);
    this.defaultColor = color;
    this.setOpaque(true);
    this.setBorderPainted(false);
    this.setFocusable(false);
  }

  /**
   *
   * @return
   */
  public Color getDefaultColor() {
    return this.defaultColor;
  }

  /**
   *
   */
  public void setDefaultBackground() {
    this.setBackground(defaultColor);
  }

}
