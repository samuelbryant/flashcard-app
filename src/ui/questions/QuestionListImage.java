package ui.questions;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import ui.questions.QuestionList.NotStartedYetException;

public class QuestionListImage {

  public static final Integer LINE_SPACING = 20;

  public static BufferedImage getQuestionListImage(QuestionList list, int width, int height) {
    try {
      return list.getCurrentQuestion().getImage();
    } catch(NotStartedYetException ex) {
      BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
      Graphics2D gr = img.createGraphics();
      gr.setColor(Color.WHITE);
      gr.fillRect(0, 0, width, height);
      gr.setColor(Color.BLACK);

      int yPos = 40;

      gr.drawString("Questions List Not Started", 40, yPos);
      yPos += LINE_SPACING;
      gr.drawString("Number of questions: " + list.getNumberOfQuestions(), 40, yPos);
      return img;
    }
  }

}
