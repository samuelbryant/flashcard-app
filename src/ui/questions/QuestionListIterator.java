/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.questions;

import models.Question;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author author
 */
public class QuestionListIterator implements QuestionIterator {
  
  private List<Question> questions;
  private int index;
  
  public QuestionListIterator(List<Question> questions) {
    this.questions = questions;
    this.index = 0;
  }
  
  @Override
  public Question current() {
    return this.questions.get(index);
  }

  @Override
  public Question next() {
    if (this.index < this.questions.size() - 1) {
      this.index++;
      return this.questions.get(this.index);
    } else {
      return null;
    }
  }

  @Override
  public Question prev() {
    if (this.index > 0) {
      this.index--;
      return this.questions.get(this.index);
    } else {
      return null;
    }
  }

  @Override
  public int count() {
    return this.questions.size();
  }

  @Override
  public int index() {
    return this.index;
  }

  @Override
  public void shuffle() {
    Collections.shuffle(questions);
    this.index = 0;
  }
  
}
