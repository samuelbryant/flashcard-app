package ui.questions;

import models.Database;
import models.DatabaseIO;
import ui.MainWindow;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import ui.questions.components.SubjectsPanel;

public class SubjectsDisplay extends QuestionListDisplay {

  public static final int TOTAL_WIDTH = 750;
  public static final int TOTAL_HEIGHT = 600;
  public static final int SUBJECT_PANEL_WIDTH = 150;
  public static final int QUESTION_PANEL_HEIGHT = 500;

  protected final SubjectsPanel subjectsPanel;

  public SubjectsDisplay(final QuestionListController ctrl) {
    super(ctrl, TOTAL_WIDTH, TOTAL_HEIGHT);
    this.subjectsPanel = new SubjectsPanel(ctrl);
  }

  @Override
  protected void setupMenuBar() {

  }

  @Override
  protected void setupGUI() {
    //       |            |
    //       |   Image    |
    // Subj. |  Display   |
    // Panel |            |
    //       |------------|
    //       |Action Panel|
    Dimension subjPanelDim = new Dimension(
        SUBJECT_PANEL_WIDTH, TOTAL_HEIGHT);
    Dimension rightPanelDim = new Dimension(
        TOTAL_WIDTH - SUBJECT_PANEL_WIDTH, TOTAL_HEIGHT);
    Dimension imagePanelDim = new Dimension(
        TOTAL_WIDTH - SUBJECT_PANEL_WIDTH, QUESTION_PANEL_HEIGHT);
    Dimension actionPanelDim = new Dimension(
        TOTAL_WIDTH - SUBJECT_PANEL_WIDTH, TOTAL_HEIGHT - QUESTION_PANEL_HEIGHT);

    JPanel rightPanel = new JPanel();

    this.sizeComponent(this.subjectsPanel, subjPanelDim);
    this.sizeComponent(rightPanel, rightPanelDim);
    this.sizeComponent(this.questionPanel, imagePanelDim);
    this.sizeComponent(this.actionPanel, actionPanelDim);

    // Add image panel, action panel to right panel.
    rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
    rightPanel.add(this.questionPanel);
    rightPanel.add(this.actionPanel);

    // Add subject panel, right panel to top level panel.
    this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    this.add(this.subjectsPanel);
    this.add(rightPanel);

    this.repaint();
  }

  public static void main(String[] args) {
    // Load/initialize models.
    Database db = DatabaseIO.loadDatabase();
    QuestionIterator iter = new QuestionListIterator(db.getQuestionListCopy());

    // Load/initialize controller/display.
    QuestionListController ctrl = new QuestionListController(iter);
    QuestionListDisplay display = new SubjectsDisplay(ctrl);

    // Bring it all home.
    MainWindow window = new MainWindow();
    window.showDisplay(display);
  }

}
