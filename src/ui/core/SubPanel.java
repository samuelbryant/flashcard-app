package ui.core;

import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;
import ui.components.FABuildable;
import ui.components.FABuildable;
import ui.components.FAPanel;
import ui.components.FAPanel;
import ui.questions.QuestionList;
import ui.questions.QuestionListController;
import ui.questions.QuestionState;

public abstract class SubPanel <T extends QuestionListController, K extends SubController<T>> extends FAPanel implements Observer, FABuildable {

  protected final K componentController;
  protected final T questionListController;
  protected final QuestionList questionList;
  protected final QuestionState questionState;

  public SubPanel(K componentController) {
    super();
    this.componentController = componentController;
    this.questionListController = this.componentController.getQuestionListController();
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
