/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.questions;

import models.Question;

/**
 *
 * @author author
 */
public interface QuestionIterator {
  
  public Question next();
  public Question prev();
  public Question current();
  public int count();
  public int index();
  public void shuffle();
  
}
