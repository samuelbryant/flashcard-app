package ui.questions;

import java.awt.Dimension;
import ui.core.Display;
import ui.core.DisplayWindow;
import ui.core.ImageDisplay;
import ui.subcomponents.ActionPanel;
import ui.subcomponents.InfobarPanel;
import ui.subcomponents.FilterPanel;
import ui.subcomponents.TaggerPanel;

public abstract class QuestionListDisplay<CTRL_TYPE extends QuestionListController>
extends Display<CTRL_TYPE> {

  public static final int DEFAULT_WINDOW_WIDTH = 1200;
  public static final int DEFAULT_WINDOW_HEIGHT = 800;
  public static final boolean DEFAULT_RECORD_RESPONSES = true;
  public static final boolean DEFAULT_HIDE_FILTER_BEFORE_RESPONSE = false;
  public static final boolean DEFAULT_HIDE_TAGGER_BEFORE_RESPONSE = false;

  protected int windowWidth, windowHeight;
  protected boolean recordResponses = true;
  protected boolean hideFilterBeforeResponse = false;
  protected boolean hideTaggerBeforeResponse = false;
  
  public static final int BORDER_SIZE = 10;
  public static final int SIDE_COLUMN_WIDTH = 200;
  public static final int ACTION_PANEL_HEIGHT = 50;
  public static final int INFO_PANEL_HEIGHT = 50;
  // public static final int TOTAL_WIDTH = 1100;
  // public static final int TOTAL_HEIGHT = 800;

  protected ImageDisplay questionPanel;
  protected ActionPanel actionPanel;
  protected FilterPanel filterPanel;
  protected InfobarPanel infoPanel;
  protected TaggerPanel taggerPanel;

  public QuestionListDisplay(CTRL_TYPE ctrl) {
    super(ctrl);
    this.setSettings();
  }
  
  public QuestionListDisplay(CTRL_TYPE ctrl, int windowWidth, int windowHeight) {
    super(ctrl);
    this.setSettings(windowWidth, windowHeight);
  }
  
  public QuestionListDisplay(
    CTRL_TYPE ctrl, int windowWidth, int windowHeight, boolean recordResponses,
    boolean hideFilterBeforeResponses, boolean hideTaggerBeforeResponses) {
    super(ctrl);
    this.setSettings(
        windowWidth, windowHeight, recordResponses,
        hideFilterBeforeResponse, hideTaggerBeforeResponse);
  }
  
  public QuestionListDisplay(
    CTRL_TYPE ctrl, boolean recordResponses, boolean hideFilterBeforeResponses, boolean hideTaggerBeforeResponses) {
    super(ctrl);
    this.setSettings(
        recordResponses, hideFilterBeforeResponse, hideTaggerBeforeResponse);
  }
  
  public final void setSettings() {
    this.setSettings(
        DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT, DEFAULT_RECORD_RESPONSES,
        DEFAULT_HIDE_FILTER_BEFORE_RESPONSE,
        DEFAULT_HIDE_TAGGER_BEFORE_RESPONSE);
  }
  
  public final void setSettings(int windowWidth, int windowHeight) {
    this.setSettings(
        windowWidth, windowHeight, DEFAULT_RECORD_RESPONSES,
        DEFAULT_HIDE_FILTER_BEFORE_RESPONSE,
        DEFAULT_HIDE_TAGGER_BEFORE_RESPONSE);
  }
  
  public final void setSettings(
      boolean recordResponses, boolean hideFilterBeforeResponse, boolean hideTaggerBeforeResponse) {
    this.setSettings(
        DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT, 
        recordResponses, hideFilterBeforeResponse, hideTaggerBeforeResponse);
  }
  
  public final void setSettings(
      int windowWidth, int windowHeight, boolean recordResponses,
      boolean hideFilterBeforeResponse, boolean hideTaggerBeforeResponse) {
    this.windowWidth = windowWidth;
    this.windowHeight = windowHeight;
    this.recordResponses = recordResponses;
    this.hideFilterBeforeResponse = hideFilterBeforeResponse;
    this.hideTaggerBeforeResponse = hideTaggerBeforeResponse;
  }

  @Override
  public void buildComponents() {
    this.actionPanel.buildComponents();
    this.taggerPanel.buildComponents();
    this.filterPanel.buildComponents();
    this.infoPanel.buildComponents();
    this.questionPanel.buildComponents();
    this.filterPanel.setHideBeforeAnswering(this.hideFilterBeforeResponse);
    this.taggerPanel.setHideBeforeAnswering(this.hideTaggerBeforeResponse);
    this.ctrl.setRecordAnswers(recordResponses);
    this.ctrl.initialUpdate();
  }
  
  public void go() {
    DisplayWindow window = new DisplayWindow(this.windowWidth, this.windowHeight);
    window.showDisplay(this);
  }

  /**
   *
   * @param totalSize
   */
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

  @Override
  protected void setupMenuBar() {}

}
