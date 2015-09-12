/*
 * File Overview: TODO
 */
package ui.questions.gre;

import java.awt.Color;
import models.Answer;
import models.Question;
import ui.core.components.FAButton;
import ui.subcomponents.ActionPanel;

/**
 *
 * @author author
 */
public class GreActionPanel
extends ActionPanel<Question, GreQuestionState, GreQuestionList, GreCtrl> {
  
  public GreActionPanel(GreCtrl controller) {
    super(controller);
  }

  @Override
  protected void observeQuestionChange() {
    super.observeQuestionChange();

    boolean isStarted = this.questionList.isStarted();
    boolean isAnswered = this.questionList.isStarted() && this.questionState.isAnswered();

    for (Answer answer: Answer.values()) {
      FAButton button = this.answerButtons.get(answer);

      button.setEnabled(isStarted);

      if (isAnswered && answer == questionState.getCorrectAnswer()) {
        button.setBackground(Color.GREEN);
      } else if (isAnswered && answer == questionState.getSelectedAnswer()) {
        button.setBackground(Color.RED);
      } else {
        button.setDefaultBackground();
      }
    }
  }
  
}
