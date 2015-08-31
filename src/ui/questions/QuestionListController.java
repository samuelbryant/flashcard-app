/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.questions;

import flashcard.question.Question;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.List;
import ui.Controller;

/**
 *
 * @author author
 */
public class QuestionListController extends Controller<QuestionListDisplay> {
  
  private QuestionListDisplay display;
  private Question currentQuestion;
  private final QuestionIterator questionIterator;
  
  public QuestionListController(QuestionIterator questionIterator) {
    this.questionIterator = questionIterator;
    this.currentQuestion = this.questionIterator.next();
  }
  
  public Question getCurrentQuestion() {
    return this.currentQuestion;
  }
  
  public void setDisplay(QuestionListDisplay d) {
    this.display = d;
  }

  @Override
  public void keyTyped(KeyEvent e) {
  }

  @Override
  public void keyPressed(KeyEvent e) {
    if (e.getKeyCode() == 39) {
      System.out.println("Setting next image");
      this.currentQuestion = this.questionIterator.next();
      this.display.repaint();
    }
    System.out.printf("KEY TYPED: %d\n", e.getKeyCode());
  }

  @Override
  public void keyReleased(KeyEvent e) {
  } 

  public void nextClick() {
    this.currentQuestion = this.questionIterator.next();
    this.display.repaint();
  }
  
  public void previousClick() {
    this.currentQuestion = this.questionIterator.prev();
    this.display.repaint();
  }
  
  public void shuffleClick() {
    this.questionIterator.shuffle();
    this.currentQuestion = this.questionIterator.next();
    this.display.repaint();
  }

  public void answerQuestion(Question q, Question.Answer answer) {
    if (q.answer == null) {
      System.out.println("LOG: Answer not in system");
    } else if (q.answer == answer) {
      System.out.printf("CORRECT");
    } else {
      System.out.printf("INCORRECT");
    }
  }

}
