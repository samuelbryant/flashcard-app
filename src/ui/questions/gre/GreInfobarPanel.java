/*
 * File Overview: TODO
 */
package ui.questions.gre;

import models.Question;
import ui.subcomponents.InfobarPanel;
import ui.subcomponents.LabeledInfoBox;

class GreInfobarPanel
extends InfobarPanel<Question, GreCtrl> {

  protected LabeledInfoBox listStatsLabel;
  protected LabeledInfoBox questionStatsLabel;
  
  GreInfobarPanel(GreCtrl ctrl) {
    super(ctrl);
  }
  
  @Override
  public void buildComponents() {
    super.buildComponents();
    
    // Stats label.
    this.listStatsLabel = this.getInfoBox("List Stats", "Not Started", new LabeledInfoBox.TextGenerator() {
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
    
    // Stats label.
    this.questionStatsLabel = this.getInfoBox("Question Stats", "-", new LabeledInfoBox.TextGenerator() {
      @Override
      public String generateLabelText() {
        if (ctrl.isStarted() && ctrl.isAnswered()) {
          int numberRightWrong[] = ctrl.getCurrentQuestion().getTimesRightWrong();
          int right = numberRightWrong[0];
          int total = right + numberRightWrong[1];
          return String.format("%d / %d", right, total);
        } else {
          return "-";
        }
      }
    });
    
    this.addInfoBox(this.listStatsLabel);
    this.addInfoBox(this.questionStatsLabel);
  }
  
}
