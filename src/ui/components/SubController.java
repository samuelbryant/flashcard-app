package ui.components;

import ui.questions.QuestionList;
import ui.questions.QuestionListController;
import ui.questions.QuestionState;

public class SubController <T extends QuestionListController> {
  
  public final T questionListController;
  protected final QuestionList questionList;
  protected final QuestionState questionState;
  
  public SubController(T questionListController) {
    this.questionListController = questionListController;
    this.questionList = questionListController.getQuestionList();
    this.questionState = questionListController.getQuestionState();
  }
  
  public T getQuestionListController() {
    return questionListController;
  }
  
}
