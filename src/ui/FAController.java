/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import ui.components.FAKeyMap;

/**
 *
 * @author sambryant
 * @param <T>
 */
public abstract class FAController <T extends FADisplay> extends Observable implements KeyListener, FAKeyMap {

  private final Map<Integer, ActionListener> _keyMap;
  protected T display;

  public FAController() {
    super();
    this._keyMap = new HashMap<>();
  }

  final void setDisplay(T d) {
    this.display = d;
  }

  public void update() {
    this.notifyObservers();
    this.display.repaint();
  }

  @Override
  public final void addKeyAction(int keyCode, ActionListener al) {
    this._keyMap.put(keyCode, al);
  }

  @Override
  public final void keyTyped(KeyEvent e) {}

  @Override
  public final void keyPressed(KeyEvent e) {
    ActionListener al = this._keyMap.get(e.getKeyCode());
    if (al != null) {
      System.out.printf("Found actionlistener for key code: %d\n", e.getKeyCode());
      al.actionPerformed(null);
    } else {
      System.out.printf("Unknown key code: %d\n", e.getKeyCode());
    }
  }
  
  @Override
  public final void keyReleased(KeyEvent e) {}
  
}
