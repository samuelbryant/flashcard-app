package ui.core.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author sambryant
 */
public abstract class FAActionButton extends FAButton implements ActionListener {

  /**
   *
   * @param label
   */
  public FAActionButton(String label) {
    super(label);
    this.addActionListener(this);
  }

  @Override
  public abstract void actionPerformed(ActionEvent ev);

}
