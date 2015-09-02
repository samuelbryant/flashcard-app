package ui.questions;

import ui.questions.tagger.TaggerPanel;
import ui.questions.tagger.TaggerController;
import java.awt.Color;
import java.awt.Component;
import models.Database;
import models.DatabaseIO;
import ui.DisplayWindow;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import ui.questions.filter.FilterController;
import ui.questions.filter.FilterPanel;

public class TaggingDisplay extends QuestionListDisplay {

  public static final int TOTAL_WIDTH = 1100;
  public static final int TOTAL_HEIGHT = 800;
  public static final int SUBJECT_PANEL_WIDTH = 150;
  public static final int FILTER_PANEL_WIDTH = 200;
  public static final int ACTION_PANEL_HEIGHT = 50;

  protected final TaggerPanel subjectsPanel;
  protected final FilterPanel filterPanel;

  public TaggingDisplay(final TaggerController ctrl) {
    super(ctrl, TOTAL_WIDTH, TOTAL_HEIGHT);
    this.subjectsPanel = new TaggerPanel(ctrl);
    
    FilterController<TaggerController> filterController = new FilterController<>(ctrl);
    this.filterPanel = new FilterPanel(filterController);
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
    
    this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    
    // BUILD LEFT PANEL.
    this.sizeComponent(this.subjectsPanel, subjPanelDim);
    this.subjectsPanel.setAlignmentY(Component.TOP_ALIGNMENT);
    this.add(this.subjectsPanel, Component.TOP_ALIGNMENT);
    
    // BUILD MIDDLE PANEL.
    JPanel middlePanel = new JPanel();
    middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.Y_AXIS));
    this.sizeComponent(this.actionPanel, actionPanelDim);
    middlePanel.add(this.actionPanel);
    this.sizeComponent(this.questionPanel, imagePanelDim);
    middlePanel.add(this.questionPanel);
    this.sizeComponent(middlePanel, middlePanelDim);
    middlePanel.setAlignmentY(Component.TOP_ALIGNMENT);
    this.add(middlePanel);
    
    // BUILD RIGHT PANEL.
    this.filterPanel.buildComponents();
    this.filterPanel.layoutComponents();
    this.filterPanel.sizeComponents(filterPanelDim);
    this.add(this.filterPanel);

    this.repaint();
  }

  public static void main(String[] args) {
    // Load/initialize models.
    Database db = DatabaseIO.loadDatabase();

    // Load/initialize controller/display.
    TaggerController ctrl = new TaggerController(db);
    TaggingDisplay display = new TaggingDisplay(ctrl);

    // Bring it all home.
    DisplayWindow window = new DisplayWindow();
    window.showDisplay(display);
  }

}
