/*
 * File Overview: TODO
 */
package ui.questions.flc;

import models.Answer;
import models.Flashcard;
import ui.questions.QuestionListController;

/**
 *
 * @author author
 */
public class FlcCtrl extends QuestionListController<
    Flashcard,
    FlcQuestionState,
    FlcQuestionList>{
  
  public FlcCtrl() {
    super(new FlcQuestionList(), new FlcQuestionState());
  }

  @Override
  public void answer(Answer answer) {
    super.answer(answer);
  }
  
  public void reveal() {
    this.questionState.reveal();
  }
  
}
