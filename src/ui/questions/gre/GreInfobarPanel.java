/*
 * File Overview: TODO
 */
package ui.questions.gre;

import models.Question;
import ui.subcomponents.InfobarPanel;
import ui.subcomponents.LabeledInfoBox;

public class GreInfobarPanel
extends InfobarPanel<Question, GreCtrl> {

  protected LabeledInfoBox statsLabel;
  
  public GreInfobarPanel(GreCtrl ctrl) {
    super(ctrl);
  }
  
  @Override
  public void buildComponents() {
    super.buildComponents();
    
    // Stats label.
    this.statsLabel = this.getInfoBox("List Stats", "Not Started", new LabeledInfoBox.TextGenerator() {
      @Override
      public String generateLabelText() {
        if (ctrl.isStarted()) {
          int numberCorrect = ctrl.getNumberCorrect();
          int totalNumber = ctrl.getNumAnswered();
          return String.format("%d / %d", numberCorrect, totalNumber);
        } else {
          return "Not Started";
        }
      }
    });
    
    this.addInfoBox(this.statsLabel);
  }
  
}
