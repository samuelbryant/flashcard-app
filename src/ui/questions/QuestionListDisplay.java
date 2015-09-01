package ui.questions;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JComponent;
import ui.FADisplay;
import ui.FAImageDisplay;
import ui.components.FAButton;

public class QuestionListDisplay extends FADisplay<QuestionListController> {

  protected int totalWidth;
  protected int totalHeight;
  protected final FAImageDisplay questionPanel;
  protected final ActionButtonPanel actionPanel;

  public void setupGUI() {
    Dimension questionImageSize = new Dimension(totalWidth, totalHeight - 100);
    Dimension actionPanelSize = new Dimension(totalWidth, 100);

    sizeComponent(questionPanel, questionImageSize);
    sizeComponent(actionPanel, actionPanelSize);

    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    this.add(questionPanel);
    this.add(actionPanel);

    this.repaint();
  }

  protected void sizeComponent(JComponent comp, Dimension size) {
    comp.setSize(size);
    comp.setPreferredSize(size);
    comp.setMinimumSize(size);
  }

  public QuestionListDisplay(final QuestionListController ctrl, int maxWidth, int maxHeight) {
    super(ctrl, maxWidth, maxHeight);
    this.actionPanel = new ActionButtonPanel();
    this.questionPanel = new FAImageDisplay() {
      @Override
      public BufferedImage generateDisplayImage() {
        return ctrl.getCurrentQuestion().getImage();
      }
    };
    this.totalWidth = maxWidth;
    this.totalHeight = maxHeight;
    this.setSize(totalWidth, totalHeight);

    // _answerPanel = new AnswerPanel(ctrl);
    // _subjectPanel = new SubjectTaggerPanel(ctrl);

    // this.setSize(maxWidth, maxHeight);


    // Dimension subjectPanelSize = new Dimension(200, maxHeight);
    // _subjectPanel.setSize(subjectPanelSize);
    // _subjectPanel.setPreferredSize(subjectPanelSize);
    // _subjectPanel.setMinimumSize(subjectPanelSize);

    // JPanel rightPanel = new JPanel();
    // Dimension rightPanelSize = new Dimension(maxWidth - 200, maxHeight);
    // rightPanel.setSize(rightPanelSize);
    // rightPanel.setPreferredSize(rightPanelSize);
    // rightPanel.setMinimumSize(rightPanelSize);

    // Dimension questionImageSize = new Dimension(maxWidth - 200, maxHeight - 100);
    // questionPanel.setSize(questionImageSize);
    // questionPanel.setPreferredSize(questionImageSize);
    // questionPanel.setMinimumSize(questionImageSize);

    // Dimension actionPanelSize = new Dimension(maxWidth - 200, 50);
    // actionPanel.setSize(actionPanelSize);
    // actionPanel.setPreferredSize(actionPanelSize);
    // actionPanel.setMinimumSize(actionPanelSize);

    // Dimension answerPanelSize = new Dimension(maxWidth - 200, 50);
    // _answerPanel.setSize(answerPanelSize);
    // _answerPanel.setPreferredSize(answerPanelSize);
    // _answerPanel.setMinimumSize(answerPanelSize);

    // rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));
    // rightPanel.add(_answerPanel);
    // rightPanel.add(questionPanel);
    // rightPanel.add(actionPanel);

    // this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

    // this.add(_subjectPanel);
    // this.add(rightPanel);

    // this.repaint();
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

      // Add key shortcuts for button presses.
      QuestionListDisplay.this.ctrl.addKeyAction(37, prevB);
      QuestionListDisplay.this.ctrl.addKeyAction(39, nextB);
    }
  }
}
