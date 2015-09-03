package ui.questions.action;

import models.Answer;
import ui.questions.QuestionListController;
import ui.questions.SubController;

public class ActionController <T extends QuestionListController> extends SubController<T> {

  public ActionController(T questionListController) {
    super(questionListController);
  }
  
  void answerQuestion(Answer answer) {
    this.questionListController.answerQuestion(answer);
  }
  
}
