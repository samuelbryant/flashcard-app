/*
 * File Overview: TODO
 */
package ui.questions.gre;

import java.awt.image.BufferedImage;
import models.Question;
import ui.subcomponents.QuestionPanel;

public class GreQuestionPanel extends QuestionPanel<Question, GreCtrl> {

  public GreQuestionPanel(GreCtrl ctrl, boolean resize) {
    super(ctrl, resize);
  }

  @Override
  public BufferedImage getCurrentImage() {
    return this.ctrl.getCurrentQuestion().getImage();
  }
  
}
