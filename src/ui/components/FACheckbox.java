package ui.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;

public abstract class FACheckbox extends JCheckBox implements ActionListener {

  public FACheckbox(String label) {
    super(label);
    this.setFocusable(false);
    this.addActionListener(this);
  }

  @Override
  public abstract void actionPerformed(ActionEvent ev);

}
