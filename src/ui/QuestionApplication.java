/*
 * File Overview: TODO
 */
package ui;

import ui.core.DisplayWindow;
import ui.questions.QuestionListController;
import ui.questions.QuestionListDisplay;

/**
 *
 * @author author
 */
public class QuestionApplication extends QuestionListDisplay {
  
  public static final int DEFAULT_WINDOW_WIDTH = 1200;
  public static final int DEFAULT_WINDOW_HEIGHT = 800;
  public static final boolean DEFAULT_RECORD_RESPONSES = true;
  public static final boolean DEFAULT_HIDE_FILTER_BEFORE_RESPONSE = false;
  public static final boolean DEFAULT_HIDE_TAGGER_BEFORE_RESPONSE = false;

  protected int windowWidth, windowHeight;
  protected boolean recordResponses = true;
  protected boolean hideFilterBeforeResponse = false;
  protected boolean hideTaggerBeforeResponse = false;
  
  public QuestionApplication() {
    super(new QuestionListController());
    this.setSettings();
  }
  
  public QuestionApplication(int windowWidth, int windowHeight) {
    super(new QuestionListController());
    this.setSettings(windowWidth, windowHeight);
  }
  
  public QuestionApplication(
    int windowWidth, int windowHeight, boolean recordResponses,
    boolean hideFilterBeforeResponses, boolean hideTaggerBeforeResponses) {
    super(new QuestionListController());
    this.setSettings(
        windowWidth, windowHeight, recordResponses,
        hideFilterBeforeResponse, hideTaggerBeforeResponse);
  }
  
  public QuestionApplication(
    boolean recordResponses, boolean hideFilterBeforeResponses, boolean hideTaggerBeforeResponses) {
    super(new QuestionListController());
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
    super.buildComponents();
    this.filterPanel.setHideBeforeAnswering(this.hideFilterBeforeResponse);
    this.taggerPanel.setHideBeforeAnswering(this.hideTaggerBeforeResponse);
    this.ctrl.setRecordAnswers(recordResponses);
  }
  
  public void go() {
    DisplayWindow window = new DisplayWindow(this.windowWidth, this.windowHeight);
    window.showDisplay(this);
  }
  
  public static void main(String[] args) {
    (new QuestionApplication()).go();
  }
  
}
