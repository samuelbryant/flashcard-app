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
    
    this.actionPanel = new GreActionPanel(ctrl);
    this.actionPanel.buildComponents();

    this.taggerPanel = new TaggerPanel(ctrl);
    this.taggerPanel.buildComponents();

    this.filterPanel = new FilterPanel(ctrl);
    this.filterPanel.buildComponents();

    this.infoPanel = new GreInfobarPanel(ctrl);
    this.infoPanel.buildComponents();

    this.questionPanel = new GreQuestionPanel(ctrl, true);
    this.questionPanel.buildComponents();
  }
  
  public static void main(String[] args) {
    GreCtrl ctrl = new GreCtrl();
    GreListDisplay disp = new GreListDisplay(ctrl, true, false, false);
    disp.go();
  }
  
}
