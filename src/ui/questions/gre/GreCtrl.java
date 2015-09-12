/*
 * File Overview: TODO
 */
package ui.questions.gre;

import models.Answer;
import models.Question;
import models.Response;
import ui.questions.QuestionListController;

/**
 *
 * @author author
 */
public class GreCtrl extends QuestionListController<
    Question,
    GreQuestionState,
    GreQuestionList>{
  
  protected int numberCorrect;
  
  public GreCtrl() {
    super(new GreQuestionList(), new GreQuestionState());
    this.numberCorrect = 0;
  }
  
  public int getNumberCorrect() {
    return numberCorrect;
  }

  @Override
  public void answer(Answer answer) {
    super.answer(answer);
    if (this.getQuestionState().isAnswered()) {
      if (this.getQuestionState().isAnsweredCorrectly()) {
        this.numberCorrect++;
      }
    }
  }
  
}
