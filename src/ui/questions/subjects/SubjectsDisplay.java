package ui.questions.subjects;

import models.Database;
import models.DatabaseIO;
import ui.DisplayWindow;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import ui.questions.QuestionListDisplay;
import ui.questions.filter.FilterDisplay;

public class SubjectsDisplay extends QuestionListDisplay {

  public static final int TOTAL_WIDTH = 1100;
  public static final int TOTAL_HEIGHT = 800;
  public static final int SUBJECT_PANEL_WIDTH = 150;
  public static final int FILTER_PANEL_WIDTH = 200;
  public static final int ACTION_PANEL_HEIGHT = 50;

  protected final SubjectsPanel subjectsPanel;
  protected final FilterDisplay filterPanel;

  public SubjectsDisplay(final SubjectsController ctrl) {
    super(ctrl, TOTAL_WIDTH, TOTAL_HEIGHT);
    this.subjectsPanel = new SubjectsPanel(ctrl);
    this.filterPanel = new FilterDisplay(ctrl);
  }

  @Override
  protected void setupMenuBar() {
    super.setupMenuBar();
  }

  @Override
  protected void setupGUI() {
    //       |Action Panel|       |
    //       |------------|       |
    // Subj. |            | Filter|
    // Panel |   Image    | Panel |
    //       |  Display   |       |
    //       |            |       |


    Dimension subjPanelDim = new Dimension(
        SUBJECT_PANEL_WIDTH, TOTAL_HEIGHT);
    Dimension middlePanelDim = new Dimension(
        TOTAL_WIDTH - (SUBJECT_PANEL_WIDTH + FILTER_PANEL_WIDTH),
        TOTAL_HEIGHT);
    Dimension imagePanelDim = new Dimension(
        TOTAL_WIDTH - (SUBJECT_PANEL_WIDTH + FILTER_PANEL_WIDTH),
        TOTAL_HEIGHT - ACTION_PANEL_HEIGHT);
    Dimension actionPanelDim = new Dimension(
        TOTAL_WIDTH - (SUBJECT_PANEL_WIDTH + FILTER_PANEL_WIDTH),
        ACTION_PANEL_HEIGHT);
    Dimension filterPanelDim = new Dimension(
        FILTER_PANEL_WIDTH, TOTAL_HEIGHT);
    
    JPanel middlePanel = new JPanel();

    this.sizeComponent(this.subjectsPanel, subjPanelDim);
    this.sizeComponent(middlePanel, middlePanelDim);
    this.sizeComponent(this.questionPanel, imagePanelDim);
    this.sizeComponent(this.actionPanel, actionPanelDim);

    // Add image panel, action panel to right panel.
    middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.Y_AXIS));
    middlePanel.add(this.actionPanel);
    middlePanel.add(this.questionPanel);

    // Add subject panel, right panel to top level panel.
    this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    this.add(this.subjectsPanel);
    this.add(middlePanel);
    this.add(this.filterPanel);

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
