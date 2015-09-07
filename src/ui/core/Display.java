package ui.core;

import javax.swing.JMenuBar;
import java.awt.Dimension;
import ui.components.FABuildable;
import ui.components.FABuildable;
import ui.components.FAPanel;
import ui.components.FAPanel;

public abstract class Display <T extends Controller> extends FAPanel implements FABuildable {

  protected T ctrl;
  protected final JMenuBar menuBar;

  public Display(T ctrl) {
    super();
    this.ctrl = ctrl;
    this.menuBar = new JMenuBar();
    this.setFocusable(true);
    this.addKeyListener(this.ctrl);
    this.addFocusListener(this.ctrl);
    this.ctrl.setDisplay(this);
  }
  
  @Override
  public abstract void buildComponents();
  
  @Override
  public abstract void layoutComponents(Dimension totalSize);
  
  public void componentResized() {
    
  }
  
  protected abstract void setupMenuBar();

  public Controller getController() {
    return this.ctrl;
  }

  public JMenuBar getDisplayMenuBar() {
    return this.menuBar;
  }

}
