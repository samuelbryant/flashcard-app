package ui.core;

import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;
import models.AbstractQuestion;
import ui.core.components.FABuildable;
import ui.core.components.FAPanel;
import ui.questions.ListCtrlImpl;

public abstract class SubPanel <Q_TYPE extends AbstractQuestion, CTRL_TYPE extends ListCtrlImpl<Q_TYPE>>
extends FAPanel implements Observer, FABuildable {

  protected final CTRL_TYPE ctrl;

  public SubPanel(CTRL_TYPE controller) {
    super();
    this.ctrl = controller;
    this.ctrl.addObserver(this);
  }

  @Override
  public abstract void buildComponents();

  @Override
  public abstract void layoutComponents(Dimension totalDimension);

  @Override
  public abstract void update(Observable o, Object args);

}
