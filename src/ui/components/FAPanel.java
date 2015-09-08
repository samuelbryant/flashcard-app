/* Panel with correct background colors and one that is not focusable.
 */
package ui.components;

import java.awt.Dimension;
import javax.swing.JComponent;
import javax.swing.JPanel;
import core.Constants;

public class FAPanel extends JPanel {

  public FAPanel() {
    super();
    this.setFocusable(false);
    this.setBackground(Constants.BACKGROUND_COLOR);
  }

  protected void sizeComponent(JComponent comp, Dimension size) {
    comp.setSize(size);
    comp.setPreferredSize(size);
    comp.setMinimumSize(size);
  }

}
