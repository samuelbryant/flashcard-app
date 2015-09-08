package ui.subcomponents;

import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;
import ui.core.ImageDisplay;
import ui.questions.QuestionList;
import ui.questions.QuestionListController;
import ui.questions.QuestionListImage;

/**
 *
 * @author sambryant
 */
public class QuestionPanel extends ImageDisplay implements Observer {

  /**
   *
   */
  protected QuestionListController ctrl;

  /**
   *
   */
  protected QuestionList questionList;

  /**
   *
   * @param ctrl
   * @param resize
   */
  public QuestionPanel(QuestionListController ctrl, boolean resize) {
    super(resize);
    this.ctrl = ctrl;
    this.questionList = ctrl.getQuestionList();
    this.questionList.addObserver(this);
  }

  /**
   *
   * @return
   */
  @Override
  public BufferedImage generateDisplayImage() {
    return QuestionListImage.getQuestionListImage(questionList, this.getWidth(), this.getHeight());
  }

  @Override
  public void update(Observable o, Object arg) {
    this.repaint();
  }
}
