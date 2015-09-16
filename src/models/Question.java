package models;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Date;
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
  
  public boolean lastAnswerIsWrong() {
    if (!this.hasAnswered()) throw new IllegalStateException("Not answered yet");
    return this.getLastResponse().getSelectedAnswer() != this.answer;
  }
  
  public boolean hasAnsweredWrong() {
    if (!this.hasAnswered()) return false;
    for (Response r: this.responses) {
      if (r.getSelectedAnswer() != this.answer) {
        return true;
      }
    }
    return false;
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

  @Override
  public String toDisplayName() {
    return this.source.toDisplayName() + " #" + this.questionNumber.toString() + " (" + this.id + ")";
  }
  
  @Override
  public void refreshImages() {
    this.questionImage = null;
  }

  public static void main(String[] args) {
    Question q = new Question(0);
    
    Date before = new Date();
    Date after = new Date(2014, 10, 10);
    System.out.println(before.before(after));
    Response r1 = new Response(Answer.A, null, before);
    Response r2 = new Response(Answer.B, null, after);
    q.addResponse(r2);
    q.addResponse(r1);
    
    System.out.println(q.getLastResponseValue());
    
    q.addResponse(r2);
    q.addResponse(r1);
    
    System.out.println(q.getLastResponseValue());
  }
  
}
