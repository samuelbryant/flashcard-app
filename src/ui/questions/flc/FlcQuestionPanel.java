/*
 * File Overview: TODO
 */
package ui.questions.flc;

import java.awt.image.BufferedImage;
import models.Flashcard;
import ui.subcomponents.QuestionPanel;

public class FlcQuestionPanel extends QuestionPanel<Flashcard, FlcCtrl> {

  public FlcQuestionPanel(FlcCtrl ctrl, boolean resize) {
    super(ctrl, resize);
  }

  @Override
  public BufferedImage getCurrentImage() {
    if (this.ctrl.isRevealed()) {
      return this.ctrl.getCurrentQuestion().getAnswerImage();
    } else {
      return this.ctrl.getCurrentQuestion().getQuestionImage();
    }
  }
  
}
