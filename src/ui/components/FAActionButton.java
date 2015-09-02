package ui.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class FAActionButton extends FAButton implements ActionListener {

  public FAActionButton(String label) {
    super(label);
    this.addActionListener(this);
  }

  @Override
  public abstract void actionPerformed(ActionEvent ev);

}
