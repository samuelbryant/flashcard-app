/*
 * File Overview: TODO
 */
package ui.questions.flc;

import ui.questions.QuestionListDisplay;
import ui.subcomponents.FilterPanel;
import ui.subcomponents.TaggerPanel;

public class FlcListDisplay extends QuestionListDisplay<FlcCtrl> {

  public FlcListDisplay(FlcCtrl ctrl, boolean recordResponses, boolean hideFilter, boolean hideTagger) {
    super(ctrl, recordResponses, hideFilter, hideTagger);
    
    this.actionPanel = new FlcActionPanel(this.ctrl);
    this.actionPanel.buildComponents();

    this.taggerPanel = new TaggerPanel(this.ctrl);
    this.taggerPanel.buildComponents();

    this.filterPanel = new FilterPanel(this.ctrl);
    this.filterPanel.buildComponents();

    this.infoPanel = new FlcInfobarPanel(this.ctrl);
    this.infoPanel.buildComponents();

    this.questionPanel = new FlcQuestionPanel(this.ctrl, true);
    this.questionPanel.buildComponents();
  }
  
}
