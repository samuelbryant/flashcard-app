package ui.questions;

import ui.questions.tagger.TaggerPanel;
import ui.questions.tagger.TaggerController;
import models.Database;
import models.DatabaseIO;
import ui.DisplayWindow;
import java.awt.Dimension;
import java.awt.Insets;
import ui.components.FAPanel;
import ui.questions.filter.FilterController;
import ui.questions.filter.FilterPanel;

public class TaggingModeDisplay extends QuestionListDisplay {

  public static final int TOTAL_WIDTH = 1100;
  public static final int TOTAL_HEIGHT = 800;
  public static final int TAGGER_PANEL_WIDTH = 150;
  public static final int FILTER_PANEL_WIDTH = 200;
  public static final int ACTION_PANEL_HEIGHT = 50;
  public static final int BORDER_SIZE = 10;

  protected TaggerPanel taggerPanel;
  protected FilterPanel filterPanel;
  protected FAPanel middlePanel;

  public TaggingModeDisplay(final QuestionListController ctrl) {
    super(ctrl);
  }
  
  @Override
  public void buildComponents() {
    super.buildComponents();
    
    TaggerController<QuestionListController> taggerController = new TaggerController<>(ctrl);
    this.taggerPanel = new TaggerPanel(taggerController);
    this.taggerPanel.buildComponents();
    
    FilterController<QuestionListController> filterController = new FilterController<>(ctrl);
    this.filterPanel = new FilterPanel(filterController);
    this.filterPanel.buildComponents();
    
    this.middlePanel = new FAPanel();
  }
  
  @Override
  public void layoutComponents() {
    this.setLayout(null);
    
    this.taggerPanel.layoutComponents();
    this.actionPanel.layoutComponents();
    this.questionPanel.layoutComponents();
    this.filterPanel.layoutComponents();
    
    this.add(this.taggerPanel);
    this.add(this.actionPanel);
    this.add(this.questionPanel);
    this.add(this.filterPanel);
    
    Dimension size;
    
    Insets insets = this.getInsets();
    int tEdge = insets.top + BORDER_SIZE;
    int lEdge = insets.left + BORDER_SIZE;
    
    size = this.taggerPanel.getPreferredSize();
    this.taggerPanel.setBounds(lEdge, tEdge, size.width, size.height);
    lEdge += size.width + BORDER_SIZE;
    
    size = this.actionPanel.getPreferredSize();
    this.actionPanel.setBounds(lEdge, tEdge, size.width, size.height);
    tEdge += size.height + BORDER_SIZE;
    
    size = this.questionPanel.getPreferredSize();
    this.questionPanel.setBounds(lEdge, tEdge, size.width, size.height);
    lEdge += size.width + BORDER_SIZE;
    tEdge = insets.top + BORDER_SIZE;
    
    size = this.filterPanel.getPreferredSize();
    this.filterPanel.setBounds(lEdge, tEdge, size.width, size.height);
  }

  @Override
  public void sizeComponents(Dimension totalSize) {
    int width = totalSize.width;
    int height = totalSize.height;
    
    Dimension taggerPanelSize = new Dimension(
        TAGGER_PANEL_WIDTH, 
        height - 2 * BORDER_SIZE);
    Dimension filterPanelSize = new Dimension(
        FILTER_PANEL_WIDTH,
        height - 2 * BORDER_SIZE);
    Dimension questionPanelSize = new Dimension(
        width - (TAGGER_PANEL_WIDTH + FILTER_PANEL_WIDTH + 2 * BORDER_SIZE),
        height - (ACTION_PANEL_HEIGHT + 3 * BORDER_SIZE));
    Dimension actionPanelSize = new Dimension(
        width - (TAGGER_PANEL_WIDTH + FILTER_PANEL_WIDTH + 2 * BORDER_SIZE),
        ACTION_PANEL_HEIGHT);
   
    
    this.taggerPanel.sizeComponents(taggerPanelSize);
    this.actionPanel.sizeComponents(actionPanelSize);
    this.questionPanel.sizeComponents(questionPanelSize);
    this.filterPanel.sizeComponents(filterPanelSize);
  }

  public static void main(String[] args) {
    // Load/initialize models.
    Database db = DatabaseIO.loadDatabase();

    // Load/initialize controller/display.
    QuestionListController ctrl = new QuestionListController(db);
    TaggingModeDisplay display = new TaggingModeDisplay(ctrl);

    // Bring it all home.
    DisplayWindow window = new DisplayWindow(TOTAL_WIDTH, TOTAL_HEIGHT);
    window.showDisplay(display);
  }

}
