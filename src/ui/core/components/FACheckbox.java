package ui.core.components;

import core.Constants;
import java.awt.Dimension;
import javax.swing.JCheckBox;

public class FACheckbox extends JCheckBox{

  public FACheckbox(String label) {
    super(label);
    this.setFocusable(false);
    this.setFont(Constants.BASIC_FONT);
    Dimension size = this.getPreferredSize();
    this.setPreferredSize(new Dimension(size.width, size.height - 4));
  }
}
