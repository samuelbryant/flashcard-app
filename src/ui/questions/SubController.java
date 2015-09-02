package ui.questions;

public abstract class SubController <T extends QuestionListController> {
  
  protected T questionListController;
  
  public SubController(T questionListController) {
    this.questionListController = questionListController;
  }
  
  public T getQuestionListController() {
    return questionListController;
  }
  
}
