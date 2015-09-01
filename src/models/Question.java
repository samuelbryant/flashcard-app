/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import core.FatalError;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import core.IO;
import flashcard.Constants;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;

public class Question implements Serializable {

  public enum QuestionSource {
    GRE_1986("1986"), GRE_1991("1991"), GRE_1995("1995"), GRE_2001("2001"), GRE_2008("2008"),
    SAMPLE_1("PT1"), SAMPLE_2("PT2"), SAMPLE_3("PT3"), PRACTICE_BOOK("BOOK"),
    TEXTBOOK("TEXT"), CUSTOM("CUST"), OTHER("OTHER");

    private final String name;

    QuestionSource(String name) {
      this.name = name;
    }
  }

  public enum Answer {
    A, B, C, D, E
  }

  public enum Subject {
    MECHANICS, FLUIDS, ROTATIONAL, EM, CIRCUITS, WAVES, OPTICS, RELATIVITY, QM, ATOMIC, THERMO,
    SPECIAL, LAB_METHODS, MATH, OTHER
  }

  public enum Tag {
    WRONG, HARD, TIME, UNSURE, IMPORTANT
  }

  // Permanent fields central to class.
  public Integer id = null;
  public QuestionSource source = null;
  public Integer questionNumber = null;
  public String databaseImageFilename = null;
  public Answer answer = null;
  public ArrayList<Subject> subjects = new ArrayList<>();
  public ArrayList<Tag> tags = new ArrayList<>();
  public ArrayList<String> notes = new ArrayList<>();

  // Transient fields which do not get serialized.
  public String originalImageFilename = null;
  public BufferedImage questionImage = null;
  public Boolean persistent = false;

  /**
   * Constructor designed for importing questions.
   * @param source The {QuestionSource} representing where question originated from.
   * @param questionNumber The {Integer} representing what number the question was w.r.t. its source.
   * @param imageFilename The {string} representation of the file with the question's image. 
   */
  public Question(QuestionSource source, Integer questionNumber, Answer answer, String imageFilename) {
    this.source = source;
    this.questionNumber = questionNumber;
    this.answer = answer;
    this.originalImageFilename = imageFilename;

    this.persistent = false;
    this.databaseImageFilename = null; 
  }

  /**
   * Constructor designed for reading questions from database.
   */
  public Question(Integer id) {
    this.id = id;
    this.persistent = true;
    this._loadFromQuestionFile();
  }

  private void _loadFromQuestionFile() {
    try {
      BufferedReader br = IO.getBufferedReaderOrDie(Constants.getQuestionDataFilename(this.id));
      this._fromQuestionFile(br);
    } catch(IOException ex) {
      throw new FatalError("loadFromQuestionFile failed", ex);
    }
  }

  public BufferedImage getImage() {
    if (this.questionImage != null) {
      return this.questionImage;
    } else {
      System.out.printf("LOG: No bufferered image found for question: %s", this);
      this.questionImage = IO.loadImageOrDie(this.databaseImageFilename);
      return this.questionImage;
    }
  }

  public boolean hasId() { return this.id != null; }
  public void setId(int id) { this.id = id; }
  public Integer getId() { return this.id; }

  private void _setFromQuestionFileLine(String field, String value) {
    switch (field) {
      case "id":
        this.id = Integer.parseInt(value);
        break;
      case "source":
        this.source = QuestionSource.valueOf(value);
        break;
      case "questionNumber":
        this.questionNumber = Integer.parseInt(value);
        break;
      case "answer":
        this.answer = Answer.valueOf(value);
        break;
      case "databaseImageFilename":
        this.databaseImageFilename = value;
        break;
      case "subjects":
        this.subjects = new ArrayList<>();
        if (value.trim().compareTo("") != 0) {
          for (String str: value.split(",")) {
            System.out.printf("|%s|\n", str);
            this.subjects.add(Subject.valueOf(str));
          }
        }
        break;
      case "tags":
        this.tags = new ArrayList<>();
        if (value.trim().compareTo("") != 0) {
          for (String str: value.split(",")) {
            this.tags.add(Tag.valueOf(str));
          }
        }
        break;
      case "notes":
        this.notes = new ArrayList<>();
        if (value.trim().compareTo("") != 0) {
          this.notes.addAll(Arrays.asList(value.split(",")));
        }
        break;
      default:
        throw new IllegalStateException("Unknown object line: " + field);
    }
  }

  private void _fromQuestionFile(BufferedReader fileReader) throws IOException {
    String line;
    while ((line = fileReader.readLine()) != null) {
      String[] parts = line.split(":");
      String key = parts[0];
      String value;
      if (parts.length == 2) value = parts[1];
      else value = "";
      this._setFromQuestionFileLine(key, value);
    }
  }


}
