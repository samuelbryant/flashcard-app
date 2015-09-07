package ui.core;

import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JFrame;

public class DisplayWindow extends JFrame implements ComponentListener {

  private Controller ctrl = null;
  private Display display = null;
  private final int totalWidth, totalHeight;
  
  public DisplayWindow(int totalWidth, int totalHeight) {
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.totalWidth = totalWidth;
    this.totalHeight = totalHeight;
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
    this.setJMenuBar(d.getDisplayMenuBar());
    
    this.display.buildComponents();
    this.display.layoutComponents(new Dimension(totalWidth, totalHeight));
    Dimension thisSize = new Dimension(totalWidth, totalHeight + 50);
    this.setSize(thisSize);

    // Redo UI stuff.
    this.getContentPane().add(d);
    this.repaint();

    // Display window.
    this.setVisible(true);
    this.addComponentListener(this);
  }

  @Override
  public void componentResized(ComponentEvent e) {
    Dimension size = this.getSize();
    this.display.layoutComponents(new Dimension(size.width, size.height - 50));
  }

  @Override
  public void componentMoved(ComponentEvent e) {}

  @Override
  public void componentShown(ComponentEvent e) {}

  @Override
  public void componentHidden(ComponentEvent e) {}

}
