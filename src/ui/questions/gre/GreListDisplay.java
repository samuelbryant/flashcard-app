/*
 * File Overview: TODO
 */
package ui.questions.gre;

import ui.questions.AppDisplay;
import ui.subcomponents.TaggerPanel;

public class GreListDisplay extends AppDisplay<GreCtrl> {

  public GreListDisplay(boolean recordResponses, boolean hideFilter, boolean hideTagger) {
    super(new GreCtrl(), recordResponses, hideFilter, hideTagger);
    this.actionPanel = new GreActionPanel(ctrl);
    this.taggerPanel = new TaggerPanel(ctrl);
    this.filterPanel = new GreFilterPanel(ctrl);
    this.infoPanel = new GreInfobarPanel(ctrl);
    this.questionPanel = new GreQuestionPanel(ctrl, true);
  }
  
  @Override
  public void buildComponents() {
    super.buildComponents();
    this.filterPanel.setHideBeforeAnswering(false);
    this.taggerPanel.setHideBeforeAnswering(true);
  }
  
  public static void main(String[] args) {
    GreListDisplay disp = new GreListDisplay(true, false, false);
    disp.go();
  }
  
}
