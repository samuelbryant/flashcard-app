/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import javax.swing.JPanel;

/**
 *
 * @author author
 */
public abstract class Display <T extends Controller> extends JPanel {
  
  protected T ctrl;
  protected int maxWidth;
  protected int maxHeight;
  
  public Display(T ctrl, int maxWidth, int maxHeight) {
    this.ctrl = ctrl;
    this.ctrl.setDisplay(this);
    this.maxWidth = maxWidth;
    this.maxHeight = maxHeight;
    this.setSize(maxWidth, maxHeight);
    this.setFocusable(true);
    this.addKeyListener(this.ctrl);
  }
  
  public Controller getController() {
    return this.ctrl;
  }  
}
