/* Panel with correct background colors and one that is not focusable.
 */
package ui.components;

import javax.swing.JPanel;
import ui.Constants;

public class FAPanel extends JPanel {
  
  public FAPanel() {
    super();
    this.setFocusable(false);
    this.setBackground(Constants.BACKGROUND_COLOR);
  }
  
}
