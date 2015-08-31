package ui.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import java.awt.Color;

public class FAButton extends JButton implements ActionListener {

  public FAButton(String label) {
    super(label);
    this.setBackground(Color.WHITE);
    this.setFocusable(false);
    this.addActionListener(this);
  }

  public void actionPerformed(ActionEvent ev) {

  }

}
