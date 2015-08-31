/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.questions;

import flashcard.question.Question;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import ui.Display;
import ui.ImageDisplay;
import ui.components.FAButton;
import ui.questions.components.AnswerPanel;

/**
 *
 * @author author
 */
public class QuestionListDisplay extends Display<QuestionListController> {

  private JPanel answerPanel;

  public QuestionListDisplay(final QuestionListController ctrl, int maxWidth, int maxHeight) {
    super(ctrl, maxWidth, maxHeight);
    JPanel buttonPanel = new ActionButtonPanel();
    JPanel questionPanel = new ImageDisplay() {
      @Override
      public BufferedImage generateDisplayImage() {
        return ctrl.getCurrentQuestion().getImage();
      }
    };

    answerPanel = new AnswerPanel(ctrl);

    this.setSize(maxWidth, maxHeight);

    Dimension questionImageSize = new Dimension(maxWidth, maxHeight - 100);
    questionPanel.setSize(questionImageSize);
    questionPanel.setPreferredSize(questionImageSize);
    questionPanel.setMinimumSize(questionImageSize);

    Dimension actionPanelSize = new Dimension(maxWidth, 50);
    buttonPanel.setSize(actionPanelSize);
    buttonPanel.setPreferredSize(actionPanelSize);
    buttonPanel.setMinimumSize(actionPanelSize);

    Dimension answerPanelSize = new Dimension(maxWidth, 50);
    answerPanel.setSize(answerPanelSize);
    answerPanel.setPreferredSize(answerPanelSize);
    answerPanel.setMinimumSize(answerPanelSize);

    this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    this.add(answerPanel);
    this.add(questionPanel);
    this.add(buttonPanel);

    this.repaint();
  }

  class ActionButtonPanel extends JPanel {

    private ActionButtonPanel() {
      FAButton nextB = new FAButton("Next") {
        @Override
        public void actionPerformed(ActionEvent e) {
          QuestionListDisplay.this.ctrl.nextClick();
        }
      };
      FAButton prevB = new FAButton("Prev") {
        @Override
        public void actionPerformed(ActionEvent e) {
          QuestionListDisplay.this.ctrl.previousClick();
        }
      };
      FAButton shufB = new FAButton("Shuffle") {
        @Override
        public void actionPerformed(ActionEvent e) {
          QuestionListDisplay.this.ctrl.shuffleClick();
        }
      };

      this.setLayout(new FlowLayout());
      this.add(nextB);
      this.add(prevB);
      this.add(shufB);
    }
  }
}
