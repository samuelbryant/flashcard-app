/*
 * File Overview: TODO
 */
package ui.questions.flc;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import models.Flashcard;
import ui.subcomponents.QuestionPanel;

public class FlcQuestionPanel extends QuestionPanel<Flashcard, FlcCtrl> 
implements MouseListener {

  public FlcQuestionPanel(FlcCtrl ctrl, boolean resize) {
    super(ctrl, resize);
    this.addMouseListener(this);
  }

  @Override
  public BufferedImage getCurrentImage() {
    if (this.ctrl.isRevealed()) {
      return this.ctrl.getCurrentQuestion().getAnswerImage();
    } else {
      return this.ctrl.getCurrentQuestion().getQuestionImage();
    }
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    this.ctrl.toggleVisibility();
  }

  @Override
  public void mousePressed(MouseEvent e) {
  }

  @Override
  public void mouseReleased(MouseEvent e) {
  }

  @Override
  public void mouseEntered(MouseEvent e) {
  }

  @Override
  public void mouseExited(MouseEvent e) {
  }
  
}
