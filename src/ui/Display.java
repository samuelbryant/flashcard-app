package ui;

import javax.swing.JPanel;
import javax.swing.JMenuBar;
import java.awt.Dimension;
import javax.swing.JComponent;

/**
 *
 * @author author
 * @param <T>
 */
public abstract class Display <T extends Controller> extends JPanel {

  protected T ctrl;
  protected int totalWidth;
  protected int totalHeight;
  protected final JMenuBar menuBar;

  public Display(T ctrl, int totalWidth, int totalHeight) {
    this.ctrl = ctrl;
    this.ctrl.setDisplay(this);
    this.totalWidth = totalWidth;
    this.totalHeight = totalHeight;
    this.menuBar = new JMenuBar();
    this.sizeComponent(this, new Dimension(totalWidth, totalHeight));
    this.setFocusable(true);
    this.addKeyListener(this.ctrl);
  }

  protected abstract void setupGUI();

  protected abstract void setupMenuBar();

  public Controller getController() {
    return this.ctrl;
  }

  public Dimension getDisplaySize() {
    return new Dimension(totalWidth, totalHeight);
  }

  public JMenuBar getDisplayMenuBar() {
    return this.menuBar;
  }

  public void preDisplay() {
    this.setupMenuBar();
    this.setupGUI();
    this.setSize(this.getDisplaySize());
  }

  protected void sizeComponent(JComponent comp, Dimension size) {
    comp.setSize(size);
    comp.setPreferredSize(size);
    comp.setMinimumSize(size);
  }

}
