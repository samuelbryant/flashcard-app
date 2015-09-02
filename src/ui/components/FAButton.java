package ui.components;

import javax.swing.JButton;
import ui.Constants;

public class FAButton extends JButton {

  public FAButton(String label) {
    super(label);
    this.setBackground(Constants.BUTTON_COLOR);
    this.setOpaque(true);
    this.setBorderPainted(false);
    this.setFocusable(false);
  }

}
