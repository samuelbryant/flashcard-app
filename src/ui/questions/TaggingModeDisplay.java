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

public class TaggingModeDisplay extends QuestionListDisplay {

  public static final int TOTAL_WIDTH = 1100;
  public static final int TOTAL_HEIGHT = 800;
  public static final int SUBJECT_PANEL_WIDTH = 150;
  public static final int FILTER_PANEL_WIDTH = 200;
  public static final int ACTION_PANEL_HEIGHT = 50;

  protected final TaggerPanel taggerPanel;
  protected final FilterPanel filterPanel;

  public TaggingModeDisplay(final QuestionListController ctrl) {
    super(ctrl, TOTAL_WIDTH, TOTAL_HEIGHT);
    
    TaggerController<QuestionListController> taggerController = new TaggerController<>(ctrl);
    this.taggerPanel = new TaggerPanel(taggerController);
    
    FilterController<QuestionListController> filterController = new FilterController<>(ctrl);
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
    this.taggerPanel.buildComponents();
    this.taggerPanel.layoutComponents();
    this.taggerPanel.sizeComponents(subjPanelDim);
    this.add(this.taggerPanel);
    
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
    QuestionListController ctrl = new QuestionListController(db);
    TaggingModeDisplay display = new TaggingModeDisplay(ctrl);

    // Bring it all home.
    DisplayWindow window = new DisplayWindow();
    window.showDisplay(display);
  }

}
