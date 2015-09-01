package ui.questions.components;

import models.Question;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JPanel;
import ui.components.FAButton;
import ui.questions.QuestionListController;

public class AnswerPanel extends JPanel {

  public class AnswerButton extends FAButton implements Observer {

    private final Question.Answer _answer;

    public AnswerButton(Question.Answer answer) {
      super(answer.name());
      this._answer = answer;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
      AnswerPanel.this._ctrl.answerQuestion(this._answer);
    }

    @Override
    public void update(Observable o, Object arg) {
      this.setBackground(Color.WHITE);

      if (AnswerPanel.this._ctrl.isAnswered()) {
        boolean isSelected = this._answer == AnswerPanel.this._ctrl.getSelectedAnswer();
        boolean isCorrect = this._answer == AnswerPanel.this._ctrl.getCorrectAnswer();

        if (isSelected && !isCorrect) {
          this.setBackground(Color.RED);
        } else if (isCorrect) {
          this.setBackground(Color.GREEN);
        }
      }
    }

  }
  
  private final QuestionListController _ctrl;
  private final AnswerButton[] _answerButtons;

  public AnswerPanel(QuestionListController ctrl) {
    this._ctrl = ctrl;

    this.setLayout(new FlowLayout());

    Question.Answer[] answers = Question.Answer.values();
    _answerButtons = new AnswerButton[answers.length];

    for (int i = 0; i < answers.length; i++) {
      _answerButtons[i] = new AnswerButton(answers[i]);
      this._ctrl.addObserver(_answerButtons[i]);
      this.add(_answerButtons[i]);
    }
    
    this.setFocusable(false);
  }
}
