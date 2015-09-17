/*
 * File Overview: TODO
 */
package ui.questions.flc;

import models.Flashcard;
import ui.subcomponents.InfobarPanel;
import ui.subcomponents.LabeledInfoBox;

class FlcInfobarPanel
extends InfobarPanel<Flashcard, FlcCtrl> {
  
  protected LabeledInfoBox totalProgressLabel;

  FlcInfobarPanel(FlcCtrl ctrl) {
    super(ctrl);
  }
  
  @Override
  public void buildComponents() {
    super.buildComponents();
    
    // Stats label.
    this.totalProgressLabel = this.getInfoBox("Total Progress", "-", new LabeledInfoBox.TextGenerator() {
      @Override
      public String generateLabelText() {
        double score = ctrl.computeTotalProgress();
        return String.format("%2.1f%%", score * 100);
      }
    });
    
    this.addInfoBox(this.totalProgressLabel);
  }

}
