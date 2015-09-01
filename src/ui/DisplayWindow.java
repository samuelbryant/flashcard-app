package ui;

import java.awt.Dimension;
import javax.swing.JFrame;

public class DisplayWindow extends JFrame {

  private Controller ctrl = null;
  private Display display = null;

  public DisplayWindow() {
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  public void showDisplay(Display d) {
    // Remove old display.
    if (this.display != null) {
      this.getContentPane().remove(this.display);
    }

    // Set new controller / display.
    this.display = d;
    this.ctrl = d.getController();

    // Setup this component based on display.
    this.display.preDisplay();
    Dimension displaySize = d.getDisplaySize();
    Dimension thisSize = new Dimension(displaySize.width, displaySize.height + 50);
    this.setSize(thisSize);
    this.setJMenuBar(d.getDisplayMenuBar());

    // Redo UI stuff.
    this.getContentPane().add(d);
    this.repaint();

    // Display window.
    this.setVisible(true);
  }

}
