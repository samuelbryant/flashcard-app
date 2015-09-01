package ui.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import java.awt.Color;

public abstract class FAButton extends JButton implements ActionListener {

  public FAButton(String label) {
    super(label);
    this.setBackground(Color.WHITE);
    this.setOpaque(true);
    this.setBorderPainted(false);
    this.setFocusable(false);
    this.addActionListener(this);
  }

  @Override
  public abstract void actionPerformed(ActionEvent ev);

}
