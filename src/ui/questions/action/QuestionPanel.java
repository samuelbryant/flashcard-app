package ui.questions.action;

import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;
import ui.components.ImageDisplay;
import ui.questions.QuestionList;
import ui.questions.QuestionListController;
import ui.questions.QuestionListImage;

public class QuestionPanel extends ImageDisplay implements Observer {
  
  protected QuestionListController ctrl;
  protected QuestionList questionList;
  
  public QuestionPanel(QuestionListController ctrl, boolean resize) {
    super(resize);
    this.ctrl = ctrl;
    this.questionList = ctrl.getQuestionList();
    this.questionList.addObserver(this);
  }

  @Override
  public BufferedImage generateDisplayImage() {
    return QuestionListImage.getQuestionListImage(questionList, this.getWidth(), this.getHeight());
  }

  @Override
  public void update(Observable o, Object arg) {
    this.repaint();
  }
}
