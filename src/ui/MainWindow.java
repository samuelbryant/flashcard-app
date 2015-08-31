/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import javax.swing.JFrame;

/**
 *
 * @author author
 */
public class MainWindow extends JFrame {
  
  private Controller ctrl = null;
  private Display display = null;
  private final int maxHeight;
  private final int maxWidth;
  
  public MainWindow(int maxWidth, int maxHeight) {
    this.maxWidth = maxWidth;
    this.maxHeight = maxHeight;
    this.setSize(maxWidth, maxHeight);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
  
  public void setDisplay(Display d) {
    // Remove old controller / display.
    if (this.display != null) {
      this.getContentPane().remove(this.display);
    }
    
    // Set new controller / display.
    this.display = d;
    this.ctrl = d.getController();
    
    // Redo UI stuff.
    this.getContentPane().add(d);
    
    this.repaint();
  }
  
}
