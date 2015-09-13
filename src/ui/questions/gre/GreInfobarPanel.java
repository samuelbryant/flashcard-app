/*
 * File Overview: TODO
 */
package ui.questions.gre;

import models.Question;
import ui.subcomponents.InfobarPanel;
import ui.subcomponents.LabeledInfoBox;

public class GreInfobarPanel
extends InfobarPanel<Question, GreQuestionState, GreQuestionList, GreCtrl> {

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
        if (questionList.isStarted()) {
          int numberCorrect = questionListController.getNumberCorrect();
          int totalNumber = questionListController.getNumberAnswered();
          return String.format("%d / %d", numberCorrect, totalNumber);
        } else {
          return "Not Started";
        }
      }
    });
    
    this.addInfoBox(this.statsLabel);
  }
  
}
