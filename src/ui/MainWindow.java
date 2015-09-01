package ui;

import javax.swing.JFrame;

public class MainWindow extends JFrame {

  private FAController ctrl = null;
  private FADisplay display = null;

  public MainWindow() {
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  public void showDisplay(FADisplay d) {
    // Remove old display.
    if (this.display != null) {
      this.getContentPane().remove(this.display);
    }

    // Set new controller / display.
    this.display = d;
    this.ctrl = d.getController();

    // Setup this component based on display.
    this.display.preDisplay();
    this.setSize(d.getDisplaySize());
    this.setJMenuBar(d.getDisplayMenuBar());

    // Redo UI stuff.
    this.getContentPane().add(d);
    this.repaint();

    // Display window.
    this.setVisible(true);
  }

}
