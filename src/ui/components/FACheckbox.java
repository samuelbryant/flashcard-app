package ui.components;

import javax.swing.JCheckBox;

/**
 *
 * @author sambryant
 */
public class FACheckbox extends JCheckBox{

  /**
   *
   * @param label
   */
  public FACheckbox(String label) {
    super(label);
    this.setFocusable(false);
  }
}
