package models;

import core.FatalError;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

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

  /**
   * Performs necessary post-loading/creating processing work.
   * This includes:
   *  - Sorting responses by date
   */
  void initialize() {
    Collections.sort(this.responses);
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

  public boolean hasSubject(Subject subject) {
    return this.subjects.contains(subject);
  }

  @Deprecated
  public Subject[] getSubjectsArray() {
    Subject[] subjects = new Subject[this.subjects.size()];
    for (int i=0; i<this.subjects.size(); i++) {
      subjects[i] = this.subjects.get(i);
    }
    return subjects;
  }

  public void setSubject(Subject subject, boolean value) {
    if (this.subjects.contains(subject)) {
      if (!value) {
        this.subjects.remove(subject);
      }
    } else if (value) {
      this.subjects.add(subject);
    }
  }

  public void addResponse(Response r) {
    this.responses.add(r);
    // Keep responses sorted by last answered date.
    Collections.sort(this.responses);
  }

  public Source getSource() {
    return this.source;
  }

  public ArrayList<Response> getResponses() {
    return this.responses;
  }

  public ArrayList<Subject> getSubjects() {
    return this.subjects;
  }

  public ArrayList<Tag> getTags() {
    return this.tags;
  }

  public int getId() {
    return this.id;
  }

  public boolean hasTag(Tag tag) {
    return this.tags.contains(tag);
  }

  public void setTag(Tag tag, boolean value) {
    if (this.tags.contains(tag)) {
      if (!value) {
        this.tags.remove(tag);
      }
    } else if (value) {
      this.tags.add(tag);
    }
  }

  public void setQuestionTags(ArrayList<Tag> tags) {
    this.tags = tags;
  }

  public void setQuestionSubjects(ArrayList<Subject> subjects) {
    this.subjects = subjects;
  }

  public Object getQuestionNumber() {
    return this.questionNumber;
  }

  public void addTag(Tag tag) {
    this.tags.add(tag);
  }

  /**
   * Gets most recent Date that this question was answered.
   * @return Date of last response or null if no responses.
   */
  public Date getLastResponseTime() {
    Collections.sort(this.responses);  // should be necessary.
    if (this.responses.isEmpty()) {
      return null;
    } else {
      return this.responses.get(0).date;
    }
  }

  public ArrayList<Boolean> getGradedResponses() {
    ArrayList<Boolean> list = new ArrayList<>();
    for (Response r: this.responses) {
      list.add(r.response == this.answer);
    }
    return list;
  }

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

}
