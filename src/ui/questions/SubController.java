package ui.questions;

public abstract class SubController <T extends QuestionListController> {
  
  public T questionListController;
  
  public SubController(T questionListController) {
    this.questionListController = questionListController;
  }
  
  public T getQuestionListController() {
    return questionListController;
  }
  
}
