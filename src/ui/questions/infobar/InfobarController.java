package ui.questions.infobar;

import ui.questions.QuestionListController;
import ui.questions.SubController;

public class InfobarController <T extends QuestionListController> extends SubController<T> {

  public InfobarController(T questionListController) {
    super(questionListController);
  }
  
}
