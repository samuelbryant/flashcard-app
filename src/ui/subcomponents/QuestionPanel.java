package ui.subcomponents;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;
import models.AbstractQuestion;
import ui.core.ImageDisplay;
import ui.questions.AppCtrlImpl;

public abstract class QuestionPanel
<Q_TYPE extends AbstractQuestion, CTRL_TYPE extends AppCtrlImpl<Q_TYPE>> 
extends ImageDisplay implements Observer {
  
  public static final int LINE_SPACING = 20;
  
  protected CTRL_TYPE ctrl;

  public QuestionPanel(CTRL_TYPE ctrl, boolean resize) {
    super(resize);
    this.ctrl = ctrl;
    this.ctrl.addObserver(this);
  }

  @Override
  public BufferedImage generateDisplayImage() {
    if (this.ctrl.isStarted()) {
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
      gr.drawString("Number of questions: " + this.ctrl.getQuestionNumber(), 40, yPos);
      return img;
    }
  }

  public abstract BufferedImage getCurrentImage();
  
  @Override
  public void update(Observable o, Object arg) {
    this.repaint();
  }
}
