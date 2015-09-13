package ui.core;

import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;
import models.AbstractQuestion;
import ui.core.components.FABuildable;
import ui.core.components.FAPanel;
import ui.questions.QuestionList;
import ui.questions.QuestionListController;
import ui.questions.QuestionState;

public abstract class SubPanel <
    Q_TYPE extends AbstractQuestion,
    STATE_TYPE extends QuestionState<STATE_TYPE, Q_TYPE, LIST_TYPE>,
    LIST_TYPE extends QuestionList<LIST_TYPE, Q_TYPE, STATE_TYPE>,
    CTRL_TYPE extends QuestionListController<Q_TYPE, STATE_TYPE, LIST_TYPE>>
extends FAPanel implements Observer, FABuildable {

  protected final CTRL_TYPE questionListController;

  protected final LIST_TYPE questionList;

  protected final STATE_TYPE questionState;

  public SubPanel(CTRL_TYPE controller) {
    super();
    this.questionListController = controller;
    this.questionState = this.questionListController.getQuestionState();
    this.questionList = this.questionListController.getQuestionList();
    this.questionState.addObserver(this);
    this.questionList.addObserver(this);
  }

  @Override
  public abstract void buildComponents();

  @Override
  public abstract void layoutComponents(Dimension totalDimension);

  protected abstract void observeListChange();

  protected abstract void observeQuestionChange();

  @Override
  public void update(Observable o, Object args) {
    if (o == this.questionList) {
      this.observeListChange();
    } else if (o == this.questionState) {
      this.observeQuestionChange();
    } else throw new RuntimeException("Unknown object update: " + o);
  }

}
