package ui.questions.components;

import models.Question;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import ui.components.FAButton;
import ui.questions.QuestionListController;

public class AnswerPanel extends JPanel {

  private final QuestionListController _ctrl;

  public class AnswerButton extends FAButton implements ActionListener {

    private final Question.Answer _answer;

    public AnswerButton(Question.Answer answer) {
      super(answer.name());
      this._answer = answer;
    }

    @Override
    public void paintComponent(Graphics gr) {
      super.paintComponent(gr);

      this.setBackground(Color.WHITE);

      if (AnswerPanel.this._ctrl.isAnswered()) {
        boolean isSelected = this._answer == AnswerPanel.this._ctrl.getSelectedAnswer();
        boolean isCorrect = this._answer == AnswerPanel.this._ctrl.getCorrectAnswer();

        if (isSelected && !isCorrect) {
          this.setBackground(Color.RED);
          System.out.println("SETting to red");
        } else if (isCorrect) {
          this.setBackground(Color.GREEN);
        }
      }
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
      AnswerPanel.this._ctrl.answerQuestion(this._answer);
    }
  }

  public AnswerPanel(QuestionListController ctrl) {
    this._ctrl = ctrl;

    this.setLayout(new FlowLayout());

    Question.Answer[] answers = Question.Answer.values();
    AnswerButton answerButtons[] = new AnswerButton[answers.length];

    for (int i = 0; i < answers.length; i++) {
      answerButtons[i] = new AnswerButton(answers[i]);
      answerButtons[i].addActionListener(answerButtons[i]);
      this.add(answerButtons[i]);

    }
  }
}
