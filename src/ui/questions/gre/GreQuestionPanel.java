/*
 * File Overview: TODO
 */
package ui.questions.gre;

import java.awt.image.BufferedImage;
import models.Question;
import ui.subcomponents.QuestionPanel;

class GreQuestionPanel extends QuestionPanel<Question, GreCtrl> {

  GreQuestionPanel(GreCtrl ctrl, boolean resize) {
    super(ctrl, resize);
  }

  @Override
  public BufferedImage getCurrentImage() {
    return this.ctrl.getCurrentQuestion().getImage();
  }
  
}
