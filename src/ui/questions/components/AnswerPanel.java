/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.questions.components;

import flashcard.question.Question;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import ui.questions.QuestionListController;
import ui.questions.QuestionListDisplay;

/**
 *
 * @author author
 */
public class AnswerPanel extends JPanel {
  
  private QuestionListController ctrl;
  
  public AnswerPanel(QuestionListController ctrl) {
    this.ctrl = ctrl;
    
    this.setLayout(new FlowLayout());
      
    Question.Answer[] answers = Question.Answer.values();
    JButton answerButtons[] = new JButton[answers.length];

    for (int i = 0; i < answers.length; i++) {
      final Question.Answer answer = answers[i];
      answerButtons[i] = new JButton(answer.name());

      answerButtons[i].addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          Question q = AnswerPanel.this.ctrl.getCurrentQuestion();
          AnswerPanel.this.ctrl.answerQuestion(q, answer);
        }
      });

      this.add(answerButtons[i]);

    }
  }
  
}
