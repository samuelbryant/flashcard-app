/*
 * File Overview: TODO
 */
package ui.questions.flc;

import java.awt.image.BufferedImage;
import models.Flashcard;
import ui.subcomponents.QuestionPanel;

public class FlcQuestionPanel
extends QuestionPanel<Flashcard, FlcQuestionState, FlcQuestionList, FlcCtrl> {

  public FlcQuestionPanel(FlcCtrl ctrl, boolean resize) {
    super(ctrl, resize);
  }

  @Override
  public BufferedImage getCurrentImage() {
    System.out.println("Getting current image");
    if (this.questionList.getQuestionState().isRevealed()) {
      return this.questionList.getCurrentQuestion().getAnswerImage();
    } else {
      return this.questionList.getCurrentQuestion().getQuestionImage();
    }
  }
  
}
