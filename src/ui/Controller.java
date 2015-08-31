/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.awt.event.KeyListener;

/**
 *
 * @author author
 */
public abstract class Controller <T extends Display> implements KeyListener {
  
  public abstract void setDisplay(T d);
  
}
