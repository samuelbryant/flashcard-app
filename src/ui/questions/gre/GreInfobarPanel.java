/*
 * File Overview: TODO
 */
package ui.questions.gre;

import models.Question;
import ui.subcomponents.InfobarPanel;

public class GreInfobarPanel
extends InfobarPanel<Question, GreQuestionState, GreQuestionList, GreCtrl> {

  public GreInfobarPanel(GreCtrl ctrl) {
    super(ctrl);
  }
  
  @Override
  protected void observeQuestionChange() {
    super.observeQuestionChange();
    
    if (this.questionList.isStarted()) {
      String source = this.questionList.getCurrentQuestion().getSource().toString();
      String number = this.questionList.getCurrentQuestion().getQuestionNumber().toString();
      this.questionLabel.setText(String.format("Q: %s - %s", source, number));
      
      int numCorrect = this.questionListController.getNumberCorrect();
      int numAnswered = this.questionListController.getNumberAnswered();
      if (numAnswered != 0) {
        this.totalStatsLabel.setText(String.format("Stats: %d/%d", numCorrect, numAnswered));
      } else {
        this.totalStatsLabel.setText("Stats: -/-");
      } 
    }
  }
  
}
