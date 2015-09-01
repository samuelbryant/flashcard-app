package ui.questions;

import models.Database;
import models.DatabaseIO;
import ui.MainWindow;
import java.awt.Dimension;
import javax.swing.BoxLayout;
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
  protected void setupMenuBar() {
    super.setupMenuBar();
  }

  @Override
  protected void setupGUI() {
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
    // Load/initialize models.
    Database db = DatabaseIO.loadDatabase();

    // Load/initialize controller/display.
    QuestionListController ctrl = new QuestionListController(db, db.getQuestionList());
    QuestionListDisplay display = new QuizDisplay(ctrl);

    // Bring it all home.
    MainWindow window = new MainWindow();
    window.showDisplay(display);
  }

}