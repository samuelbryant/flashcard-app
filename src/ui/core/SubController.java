package ui.core;

import ui.questions.QuestionList;
import ui.questions.QuestionListController;
import ui.questions.QuestionState;

/**
 *
 * @author sambryant
 * @param <T>
 */
public class SubController <T extends QuestionListController> {

  /**
   *
   */
  public final T questionListController;

  /**
   *
   */
  protected final QuestionList questionList;

  /**
   *
   */
  protected final QuestionState questionState;

  /**
   *
   * @param questionListController
   */
  public SubController(T questionListController) {
    this.questionListController = questionListController;
    this.questionList = questionListController.getQuestionList();
    this.questionState = questionListController.getQuestionState();
  }

  /**
   *
   * @return
   */
  public T getQuestionListController() {
    return questionListController;
  }

}
