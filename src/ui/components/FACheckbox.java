package ui.components;

import javax.swing.JCheckBox;

public class FACheckbox extends JCheckBox{

  public FACheckbox(String label) {
    super(label);
    this.setFocusable(false);
  }
}
