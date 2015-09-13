package ui.questions.gre;

import core.Constants;
import java.awt.Color;
import java.util.Observable;
import models.Answer;
import models.Question;
import ui.core.components.FAButton;
import ui.subcomponents.ActionPanel;

public class GreActionPanel extends ActionPanel<Question, GreCtrl> {
  
  public GreActionPanel(GreCtrl controller) {
    super(controller);
  }

  @Override
  public void update(Observable o, Object args) {
    super.update(o, args);

    boolean isStarted = this.ctrl.isStarted();
    boolean isAnswered = this.ctrl.isAnswered();
    Answer correctAnswer = isAnswered ? this.ctrl.getCurrentQuestion().getAnswer() : null;
    Answer selectedAnswer = isAnswered ? this.ctrl.getSelectedAnswer() : null;
    
    for (Answer answer: Answer.values()) {
      FAButton button = this.answerButtons.get(answer);
      if (isAnswered && answer == correctAnswer) {
        button.setBackground(Constants.CORRECT_COLOR);
      } else if (isAnswered && answer == selectedAnswer) {
        button.setBackground(Constants.INCORRECT_COLOR);
      }
    }
  }
  
}
