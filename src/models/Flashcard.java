/*
 * File Overview: TODO
 */
package models;

import java.awt.image.BufferedImage;

public class Flashcard extends AbstractQuestion {
  
  // Permanent fields.
  protected String databaseQuestionFilename = null;  // Non-nullable for persistent entries.
  protected String databaseAnswerFilename = null;  // Non-nullable for persistent entries.
  
  // Transient fields.
  protected String originalQuestionFilename;
  protected String originalAnswerFilename;
  protected BufferedImage answerImage;
  protected BufferedImage questionImage;

  
  public Flashcard(BufferedImage qImg, BufferedImage aImg) {
    this.questionImage = qImg;
    this.answerImage = aImg;
  }
  
  Flashcard(int id) {
    super(id);
  }
  
  @Override
  public QType getType() {
    return QType.FLASHCARD;
  }

  @Override
  public boolean isValid() {
    if (this.id == null)
      return false;
    if (!((databaseQuestionFilename != null && databaseAnswerFilename != null && this.persistent) ||
          (questionImage != null && answerImage != null && !this.persistent))) {
      return false;
    }
    return true;
  }
  
  public BufferedImage getQuestionImage() {
    if (this.questionImage != null) {
      return this.questionImage;
    } else {
      QuestionIO.getFlashcardIO().loadQuestionImages(this);
      return this.questionImage;
    }
  }
  
  public BufferedImage getAnswerImage() {
    if (this.answerImage != null) {
      return this.answerImage;
    } else {
      QuestionIO.getFlashcardIO().loadQuestionImages(this);
      return this.answerImage;
    }
  }
  
  @Override
  public void refreshImages() {
    this.answerImage = null;
    this.questionImage = null;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Flashcard) {
      Flashcard q = (Flashcard) obj;
      return this.id == q.id;
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return super.id;
  }

  @Override
  public String toDisplayName() {
    return "FLC " + this.id.toString();
  }
   
}
