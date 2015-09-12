/*
 * File Overview: TODO
 */
package ui.questions.gre;

import ui.questions.QuestionListDisplay;
import ui.subcomponents.FilterPanel;
import ui.subcomponents.TaggerPanel;

public class GreListDisplay extends QuestionListDisplay<GreCtrl> {

  public GreListDisplay(GreCtrl ctrl, boolean recordResponses, boolean hideFilter, boolean hideTagger) {
    super(ctrl, recordResponses, hideFilter, hideTagger);
    
    this.actionPanel = new GreActionPanel(this.ctrl);
    this.actionPanel.buildComponents();

    this.taggerPanel = new TaggerPanel(this.ctrl);
    this.taggerPanel.buildComponents();

    this.filterPanel = new FilterPanel(this.ctrl);
    this.filterPanel.buildComponents();

    this.infoPanel = new GreInfobarPanel(this.ctrl);
    this.infoPanel.buildComponents();

    this.questionPanel = new GreQuestionPanel(this.ctrl, true);
    this.questionPanel.buildComponents();
  }
  
}
