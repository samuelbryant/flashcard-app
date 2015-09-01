package models;

import core.FatalError;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.io.Serializable;

public class Question implements Serializable {

  // Permanent fields central to class.  
  Integer id = null;  // Non-nullable
  Source source = null;  // Non-nullable
  Integer questionNumber = null;  // Non-nullable
  String databaseImageFilename = null;  // Non-nullable for persistent entries.
  Answer answer = null;
  ArrayList<Subject> subjects = new ArrayList<>();
  ArrayList<Tag> tags = new ArrayList<>();
  ArrayList<String> notes = new ArrayList<>();
  ArrayList<Response> responses = new ArrayList<>();

  // Transient fields which do not get serialized.
  String originalImageFilename = null;
  BufferedImage questionImage = null;
  Boolean persistent = false;

  Question() {}
  
  /**
   * Constructor designed for importing questions.
   * @param source The {QuestionSource} representing where question originated from.
   * @param questionNumber The {Integer} representing what number the question was w.r.t. its source.
   * @param answer The {Answer} representing the correct answer.
   * @param imageFilename The {String} representation of the file with the question's image. 
   */
  public Question(Source source, Integer questionNumber, Answer answer, String imageFilename) {
    this.source = source;
    this.questionNumber = questionNumber;
    this.answer = answer;
    this.originalImageFilename = imageFilename;

    this.persistent = false;
    this.databaseImageFilename = null; 
  }
  
  @Override
  public String toString() {
    return String.format("ID: %05d  (%s  -  #%d)", id, source, questionNumber);
  }
  
  public BufferedImage getImage() {
    if (this.questionImage != null) {
      return this.questionImage;
    } else {
      System.out.printf("LOG: image not loaded yet for %s\n", this);
      QuestionIO.loadQuestionImage(this);
      return this.questionImage;
    }
  }
  
  public boolean isValid() {
    // Ensure proper fields are non-null.
    if (this.id == null) return false;
    if (this.source == null) return false;
    if (this.questionNumber == null) return false;
    return true;
  }
  
  public void validate() {
    if (!this.isValid()) {
      throw new FatalError("Question failed validation: " + this);
    }
  }

  public Answer getAnswer() {
    return this.answer;
  }
}
