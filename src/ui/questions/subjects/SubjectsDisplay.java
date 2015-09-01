package ui.questions.subjects;

import models.Database;
import models.DatabaseIO;
import ui.DisplayWindow;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import ui.questions.QuestionListDisplay;

public class SubjectsDisplay extends QuestionListDisplay {

  public static final int TOTAL_WIDTH = 600;
  public static final int TOTAL_HEIGHT = 600;
  public static final int SUBJECT_PANEL_WIDTH = 150;
  public static final int ACTION_PANEL_HEIGHT = 50;

  protected final SubjectsPanel subjectsPanel;

  public SubjectsDisplay(final SubjectsController ctrl) {
    super(ctrl, TOTAL_WIDTH, TOTAL_HEIGHT);
    this.subjectsPanel = new SubjectsPanel(ctrl);
  }

  @Override
  protected void setupMenuBar() {
    super.setupMenuBar();
  }

  @Override
  protected void setupGUI() {
    //       |Action Panel|
    //       |------------|
    // Subj. |            |
    // Panel |   Image    |
    //       |  Display   |
    //       |            |


    Dimension subjPanelDim = new Dimension(
        SUBJECT_PANEL_WIDTH, TOTAL_HEIGHT);
    Dimension rightPanelDim = new Dimension(
        TOTAL_WIDTH - SUBJECT_PANEL_WIDTH, TOTAL_HEIGHT);
    Dimension imagePanelDim = new Dimension(
        TOTAL_WIDTH - SUBJECT_PANEL_WIDTH, TOTAL_HEIGHT - ACTION_PANEL_HEIGHT);
    Dimension actionPanelDim = new Dimension(
        TOTAL_WIDTH - SUBJECT_PANEL_WIDTH, ACTION_PANEL_HEIGHT);
    
    JPanel rightPanel = new JPanel();

    this.sizeComponent(this.subjectsPanel, subjPanelDim);
    this.sizeComponent(rightPanel, rightPanelDim);
    this.sizeComponent(this.questionPanel, imagePanelDim);
    this.sizeComponent(this.actionPanel, actionPanelDim);

    // Add image panel, action panel to right panel.
    rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
    rightPanel.add(this.actionPanel);
    rightPanel.add(this.questionPanel);

    // Add subject panel, right panel to top level panel.
    this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    this.add(this.subjectsPanel);
    this.add(rightPanel);

    this.repaint();
  }

  public static void main(String[] args) {
    // Load/initialize models.
    Database db = DatabaseIO.loadDatabase();

    // Load/initialize controller/display.
    SubjectsController ctrl = new SubjectsController(db);
    SubjectsDisplay display = new SubjectsDisplay(ctrl);

    // Bring it all home.
    DisplayWindow window = new DisplayWindow();
    window.showDisplay(display);
  }

}
