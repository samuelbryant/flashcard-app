package ui.questions.infobar;

import ui.questions.QuestionListController;

public class InfoBarController <T extends QuestionListController> {
  
  protected final T questionListController;
  
  public InfoBarController(T questionListController) {
    this.questionListController = questionListController;
  }

}
