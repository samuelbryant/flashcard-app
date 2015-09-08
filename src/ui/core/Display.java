package ui.core;

import javax.swing.JMenuBar;
import java.awt.Dimension;
import ui.core.components.FABuildable;
import ui.core.components.FABuildable;
import ui.core.components.FAPanel;
import ui.core.components.FAPanel;

/**
 *
 * @author sambryant
 * @param <T>
 */
public abstract class Display <T extends Controller> extends FAPanel implements FABuildable {

  /**
   *
   */
  protected T ctrl;

  /**
   *
   */
  protected final JMenuBar menuBar;

  /**
   *
   * @param ctrl
   */
  public Display(T ctrl) {
    super();
    this.ctrl = ctrl;
    this.menuBar = new JMenuBar();
    this.setFocusable(true);
    this.addKeyListener(this.ctrl);
    this.addFocusListener(this.ctrl);
    this.ctrl.setDisplay(this);
  }

  /**
   *
   */
  @Override
  public abstract void buildComponents();

  /**
   *
   * @param totalSize
   */
  @Override
  public abstract void layoutComponents(Dimension totalSize);

  /**
   *
   */
  public void componentResized() {

  }

  /**
   *
   */
  protected abstract void setupMenuBar();

  /**
   *
   * @return
   */
  public Controller getController() {
    return this.ctrl;
  }

  /**
   *
   * @return
   */
  public JMenuBar getDisplayMenuBar() {
    return this.menuBar;
  }

}
