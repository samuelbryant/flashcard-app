/*
 * File Overview: TODO
 */
package ui.questions.gre;

import models.Answer;
import models.Question;
import ui.questions.QuestionState;


public class GreQuestionState extends QuestionState<
    GreQuestionState,
    Question, 
    GreQuestionList> {

  protected Answer correctAnswer;
  
  GreQuestionState() {
    super();
    this.correctAnswer = null;
  }
  
  @Override
  protected void setQuestion(Question q) {
    if (q == null) {
      this.correctAnswer = null;
    } else {
      this.correctAnswer = q.getAnswer();
    }
    super.setQuestion(q);
  }
  
  public Boolean isAnsweredCorrectly() {
    if (this.isAnswered()) {
      return this.correctAnswer == this._selectedAnswer;
    } else {
      throw new QuestionState.NotAnsweredYetException();
    }
  }
  
  public Answer getCorrectAnswer() {
    return this.correctAnswer;
  }
}
