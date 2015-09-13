/* Panel with correct background colors and one that is not focusable.
 */
package ui.core.components;

import java.awt.Dimension;
import javax.swing.JComponent;
import javax.swing.JPanel;
import core.Constants;

/**
 *
 * @author sambryant
 */
public class FAPanel extends JPanel {

  /**
   *
   */
  public FAPanel() {
    super();
    this.setFocusable(false);
    this.setBackground(Constants.BACKGROUND_COLOR);
    this.setFont(Constants.BASIC_FONT);
  }

  /**
   *
   * @param comp
   * @param size
   */
  protected void sizeComponent(JComponent comp, Dimension size) {
    comp.setSize(size);
    comp.setPreferredSize(size);
    comp.setMinimumSize(size);
  }

}
