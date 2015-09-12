/*
 * File Overview: TODO
 */
package ui.questions.flc;

import models.Flashcard;
import ui.questions.QuestionState;


public class FlcQuestionState extends QuestionState<
    FlcQuestionState,
    Flashcard, 
    FlcQuestionList> {
  
  protected boolean revealed = false;
  
  FlcQuestionState() {
    super();
  }
  
  public void reveal() {
    if (!revealed) {
      revealed = true;
      this.setChanged();
      this.notifyObservers();
      System.out.println("NOtifying reveal change\n");
    } else {
      throw new NotAnsweredYetException();
    }
  }
  
  public boolean isRevealed() {
    return this.revealed;
  }
  
  @Override
  protected void setQuestion(Flashcard q) {
    this.revealed = false;
    super.setQuestion(q);
  }
}
