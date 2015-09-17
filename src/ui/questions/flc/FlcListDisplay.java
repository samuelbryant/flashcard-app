/*
 * File Overview: TODO
 */
package ui.questions.flc;

import ui.questions.AppDisplay;
import ui.subcomponents.TaggerPanel;

public class FlcListDisplay extends AppDisplay<FlcCtrl> {

  public FlcListDisplay(boolean recordResponses, boolean hideFilter, boolean hideTagger) {
    super(new FlcCtrl(), recordResponses, hideFilter, hideTagger);
    
    this.actionPanel = new FlcActionPanel(this.ctrl);
    this.actionPanel.buildComponents();

    this.taggerPanel = new TaggerPanel(this.ctrl);
    this.taggerPanel.buildComponents();
    
    this.filterPanel = new FlcFilterPanel(this.ctrl);
    this.filterPanel.buildComponents();

    this.infoPanel = new FlcInfobarPanel(this.ctrl);
    this.infoPanel.buildComponents();

    this.questionPanel = new FlcQuestionPanel(this.ctrl, true);
    this.questionPanel.buildComponents();
  }
  
}
