package ui.subcomponents;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;
import models.AbstractQuestion;
import ui.core.ImageDisplay;
import ui.questions.QuestionList;
import ui.questions.QuestionListController;
import ui.questions.QuestionState;

public abstract class QuestionPanel<
    Q_TYPE extends AbstractQuestion,
    STATE_TYPE extends QuestionState<STATE_TYPE, Q_TYPE, LIST_TYPE>,
    LIST_TYPE extends QuestionList<LIST_TYPE, Q_TYPE, STATE_TYPE>,
    CTRL_TYPE extends QuestionListController<Q_TYPE, STATE_TYPE, LIST_TYPE>> 
    extends ImageDisplay implements Observer {
  
  public static final int LINE_SPACING = 20;
  
  protected CTRL_TYPE ctrl;
  protected LIST_TYPE questionList;
  protected STATE_TYPE questionState;

  public QuestionPanel(CTRL_TYPE ctrl, boolean resize) {
    super(resize);
    this.ctrl = ctrl;
    this.questionList = ctrl.getQuestionList();
    this.questionState = ctrl.getQuestionState();
    this.questionState.addObserver(this);
  }

  @Override
  public BufferedImage generateDisplayImage() {
    if (this.questionList.isStarted()) {
      return this.getCurrentImage();
    } else {
      int width = this.getWidth();
      int height = this.getHeight();
      BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
      Graphics2D gr = img.createGraphics();
      gr.setColor(Color.WHITE);
      gr.fillRect(0, 0, width, height);
      gr.setColor(Color.BLACK);

      int yPos = 40;

      gr.drawString("Questions List Not Started", 40, yPos);
      yPos += LINE_SPACING;
      gr.drawString("Number of questions: " + this.questionList.getNumberOfQuestions(), 40, yPos);
      return img;
    }
  }

  public abstract BufferedImage getCurrentImage();
  
  @Override
  public void update(Observable o, Object arg) {
    this.repaint();
  }
}
