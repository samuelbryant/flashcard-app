package ui.core.components;

import core.Constants;
import java.awt.Dimension;
import javax.swing.JComboBox;

public class FAComboBox extends JComboBox {
  
  public FAComboBox(Object[] objects) {
    super(objects);
    this.setFont(Constants.BASIC_FONT);
    this.setForeground(Constants.COMBO_BOX_COLOR);
    this.setBackground(Constants.BACKGROUND_COLOR);
    this.setLightWeightPopupEnabled(true);
    this.getEditor().getEditorComponent().setBackground(Constants.BACKGROUND_COLOR);
  }
  
}
