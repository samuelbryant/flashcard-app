package ui.questions;

import models.Database;
import models.DatabaseIO;
import ui.MainWindow;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import ui.FADisplay;
import ui.FAImageDisplay;
import ui.components.FAButton;
import ui.questions.components.AnswerPanel;

public class QuizDisplay extends QuestionListDisplay {

  public static final int TOTAL_WIDTH = 600;
  public static final int TOTAL_HEIGHT = 600;
  public static final int ANSWER_PANEL_HEIGHT = 50;
  public static final int ACTION_PANEL_HEIGHT = 50;

  protected final AnswerPanel answerPanel;

  public QuizDisplay(final QuestionListController ctrl) {
    super(ctrl, TOTAL_WIDTH, TOTAL_HEIGHT);
    this.answerPanel = new AnswerPanel(ctrl);
  }

  @Override
  public void setupGUI() {
    // |Answer Panel|
    // |------------|
    // |            |
    // |   Image    |
    // |  Display   |
    // |            |
    // |------------|
    // |Action Panel|
    Dimension answerPanelDim = new Dimension(
        TOTAL_WIDTH, ANSWER_PANEL_HEIGHT);
    Dimension imagePanelDim = new Dimension(
        TOTAL_WIDTH, TOTAL_HEIGHT - (ANSWER_PANEL_HEIGHT + ACTION_PANEL_HEIGHT));
    Dimension actionPanelDim = new Dimension(
        TOTAL_WIDTH, ACTION_PANEL_HEIGHT);

    this.sizeComponent(this.answerPanel, answerPanelDim);
    this.sizeComponent(this.questionPanel, imagePanelDim);
    this.sizeComponent(this.actionPanel, actionPanelDim);

    // Add subject panel, right panel to top level panel.
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    this.add(this.answerPanel);
    this.add(this.questionPanel);
    this.add(this.actionPanel);

    this.repaint();
  }

  public static void main(String[] args) {
    Database db = DatabaseIO.loadDatabase();

    MainWindow window = new MainWindow(TOTAL_WIDTH, TOTAL_HEIGHT);
    QuestionIterator iter = new QuestionListIterator(db.getQuestionListCopy());
    QuestionListController ctrl = new QuestionListController(iter);
    QuestionListDisplay display = new QuizDisplay(ctrl);
    window.setDisplay(display);

    display.setupGUI();

    window.setVisible(true);
  }

}
