package ui.questions;

import java.awt.Dimension;
import models.Database;
import models.DatabaseIO;
import ui.components.DisplayWindow;
import ui.components.Display;
import ui.components.ImageDisplay;
import ui.questions.action.ActionPanel;
import ui.questions.action.InfobarPanel;
import ui.questions.action.FilterPanel;
import ui.questions.action.QuestionPanel;
import ui.questions.action.TaggerPanel;

public class QuestionListDisplay extends Display<QuestionListController> {
  
  public static final int BORDER_SIZE = 10;
  public static final int SIDE_COLUMN_WIDTH = 200;
  public static final int ACTION_PANEL_HEIGHT = 50;
  public static final int INFO_PANEL_HEIGHT = 50;
  
  public static final int TOTAL_WIDTH = 1100;
  public static final int TOTAL_HEIGHT = 800;

  protected ImageDisplay questionPanel;
  protected ActionPanel actionPanel;
  protected FilterPanel filterPanel;
  protected InfobarPanel infoPanel;
  protected TaggerPanel taggerPanel;

  public QuestionListDisplay(final QuestionListController ctrl) {
    super(ctrl);
  }
  
  @Override
  public void buildComponents() {
    this.actionPanel = new ActionPanel(this.ctrl);
    this.actionPanel.buildComponents();
    
    this.taggerPanel = new TaggerPanel(this.ctrl);
    this.taggerPanel.buildComponents();
    
    this.filterPanel = new FilterPanel(this.ctrl);
    this.filterPanel.buildComponents();
    
    this.infoPanel = new InfobarPanel(this.ctrl);
    this.infoPanel.buildComponents();
    
    this.questionPanel = new QuestionPanel(this.ctrl, true);
    this.questionPanel.buildComponents();
    
    this.ctrl.initialUpdate();
  }

  @Override
  public void layoutComponents(Dimension totalSize) {
    this.setLayout(null);
    
    this.add(this.taggerPanel);
    this.add(this.actionPanel);
    this.add(this.infoPanel);
    this.add(this.questionPanel);
    this.add(this.filterPanel);
    
    int width = totalSize.width - 2 * BORDER_SIZE;
    int height = totalSize.height - 2 * BORDER_SIZE;
    
    int col1Width = SIDE_COLUMN_WIDTH;
    int col3Width = SIDE_COLUMN_WIDTH;
    int col2Width = width - (col1Width + col3Width + 2 * BORDER_SIZE);
    
    int row1Height = INFO_PANEL_HEIGHT;
    int row2Height = ACTION_PANEL_HEIGHT;
    int row3Height = height - (row1Height + row2Height + 2 * BORDER_SIZE);
    
    Dimension taggerDim = new Dimension(col1Width, height);
    Dimension infoDim = new Dimension(col2Width, row1Height);
    Dimension actionDim = new Dimension(col2Width, row2Height);
    Dimension questionDim = new Dimension(col2Width, row3Height);
    Dimension filterDim = new Dimension(col3Width, height);
    
    int lEdge = BORDER_SIZE;
    int tEdge = BORDER_SIZE;
    
    this.taggerPanel.setBounds(lEdge, tEdge, taggerDim.width, taggerDim.height);
    
    lEdge += taggerDim.width;
    lEdge += BORDER_SIZE;
    
    this.infoPanel.setBounds(lEdge, tEdge, infoDim.width, infoDim.height);
    
    tEdge += infoDim.height;
    tEdge += BORDER_SIZE;
    
    this.actionPanel.setBounds(lEdge, tEdge, actionDim.width, actionDim.height);
    
    tEdge += actionDim.height;
    tEdge += BORDER_SIZE;
    
    this.questionPanel.setBounds(lEdge, tEdge, actionDim.width, actionDim.height);
    
    lEdge += actionDim.width;
    lEdge += BORDER_SIZE;
    tEdge = BORDER_SIZE;
    
    this.filterPanel.setBounds(lEdge, tEdge, filterDim.width, filterDim.height);
    
    this.infoPanel.layoutComponents(infoDim);
    this.taggerPanel.layoutComponents(taggerDim);
    this.actionPanel.layoutComponents(actionDim);
    this.questionPanel.layoutComponents(questionDim);
    this.filterPanel.layoutComponents(filterDim);
    
    this.sizeComponent(this, totalSize);
  }
  
  public static void main(String[] args) {
    // Load/initialize models.
    Database db = DatabaseIO.loadDatabase();

    // Load/initialize controller/display.
    QuestionListController ctrl = new QuestionListController();
    QuestionListDisplay display = new QuestionListDisplay(ctrl);

    // Bring it all home.
    DisplayWindow window = new DisplayWindow(TOTAL_WIDTH, TOTAL_HEIGHT);
    window.showDisplay(display);
  }

  @Override
  protected void setupMenuBar() {}
  
}
