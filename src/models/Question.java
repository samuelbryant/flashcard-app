package models;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Objects;

public class Question extends AbstractQuestion {

  protected Source source = null;  // Non-nullable
  protected Integer questionNumber = null;  // Non-nullable
  protected String databaseImageFilename = null;  // Non-nullable for persistent entries.
  protected Answer answer = null;
  
  // Transient fields which do not get serialized.
  protected String originalImageFilename = null;
  protected BufferedImage questionImage = null;
  
  /**
   * Constructor used when reading a question from database
   */
  Question(int id) {
    super(id);
  }

  /**
   * Constructor designed for importing questions.
   * @param source The {QuestionSource} representing where question originated from.
   * @param questionNumber The {Integer} representing what number the question was w.r.t. its source.
   * @param answer The {Answer} representing the correct answer.
   * @param imageFilename The {String} representation of the file with the question's image. 
   */
  public Question(Source source, Integer questionNumber, Answer answer, String imageFilename) {
    super();
    this.source = source;
    this.questionNumber = questionNumber;
    this.answer = answer;
    this.originalImageFilename = imageFilename;
    this.databaseImageFilename = null;
  }
  
  /**
   * Constructor used for question comparison (equals compares source and number).
   * @param source
   * @param questionNumber 
   */
  public Question(Source source, Integer questionNumber) {
    super();
    this.source = source;
    this.questionNumber = questionNumber;
  }

  @Override
  public String toString() {
    return super.toString() + String.format(" (%s - #%d)", source, questionNumber);
  }

  /**
   * Returns image for this Question.
   * @return BufferedImage of this GRE Question.
   */
  public BufferedImage getImage() {
    if (this.questionImage != null) {
      return this.questionImage;
    } else {
      System.out.printf("LOG: image not loaded yet for %s\n", this);
      QuestionIO.getGREIO().loadQuestionImages(this);
      return this.questionImage;
    }
  }

  /**
   * Checks if this Question is valid (ready to be written to database).
   * @return
   */
  @Override
  public boolean isValid() {
    // Ensure proper fields are non-null.
    if (this.id == null) 
      return false;
    if (this.source == null) 
      return false;
    if (this.questionNumber == null) 
      return false;
    if (!((databaseImageFilename != null && this.persistent) ||
          (originalImageFilename != null && !this.persistent))) {
      return false;
    }
    return true;
  }

  /**
   *
   * @return
   */
  public Answer getAnswer() {
    return this.answer;
  }

  /**
   *
   * @return
   */
  public Source getSource() {
    return this.source;
  }

  /**
   *
   * @return
   */
  public Object getQuestionNumber() {
    return this.questionNumber;
  }

  /**
   *
   * @return
   */
  public ArrayList<Boolean> getGradedResponses() {
    ArrayList<Boolean> list = new ArrayList<>();
    for (Response r: this.responses) {
      list.add(r.response == this.answer);
    }
    return list;
  }

  /**
   *
   * @return
   */
  public int[] getTimesRightWrong() {
    int[] grades = new int[]{0,0};
    for (Response r: this.responses) {
      if (r.getSelectedAnswer() == this.answer) {
        grades[0]++;
      } else {
        grades[1]++;
      }
    }
    return grades;
  }

  @Override
  public QType getType() {
    return QType.GRE;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Question) {
      Question q = (Question) obj;
      return q.source == this.source && Objects.equals(q.questionNumber, this.questionNumber);
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 17 * hash + Objects.hashCode(this.source);
    hash = 17 * hash + Objects.hashCode(this.questionNumber);
    return hash;
  }

}
