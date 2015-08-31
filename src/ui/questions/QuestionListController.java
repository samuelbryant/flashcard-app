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

public class QuestionListController extends Controller<QuestionListDisplay> {

  /**
   * Enum capturing state of question list controller.
   */
  enum State {
    NOT_STARTED, COMPLETED, IN_PROGRESS;
  }

  /**
   * Enum capturing answered state of current question.
   */
  public enum AnsweredState {
    UNANSWERED, CORRECT, INCORRECT;
  }

  /**
   * Class encapsulating state/value of current question displayed.
   */
  public class QuestionDisplay {

    private final Question _question;
    private AnsweredState _answeredState;
    private Question.Answer _selectedAnswer;
    private Question.Answer _correctAnswer;

    public QuestionDisplay(Question q) {
      this._question = q;
      this._answeredState = AnsweredState.UNANSWERED;
      this._selectedAnswer = null;
      this._correctAnswer = _question.answer;
    }

    private void answer(Question.Answer answer) {
      this._selectedAnswer = answer;
      if (answer == _correctAnswer) {
        this._answeredState = AnsweredState.CORRECT;
        System.out.println("CORRECT");
      } else {
        this._answeredState = AnsweredState.INCORRECT;
        System.out.println("INCORRECT");
      }
    }
  }

  private QuestionListDisplay _display;
  private QuestionDisplay _currentQuestion;
  private final QuestionIterator _questionIterator;
  private State _state;

  public QuestionListController(QuestionIterator questionIterator) {
    this._questionIterator = questionIterator;
    this._currentQuestion = new QuestionDisplay(this._questionIterator.next());
    this._state = State.IN_PROGRESS;
  }

  public Question.Answer getCorrectAnswer() {
    return this._currentQuestion._correctAnswer;
  }

  public Question.Answer getSelectedAnswer() {
    return this._currentQuestion._selectedAnswer;
  }

  public AnsweredState getAnsweredState() {
    return this._currentQuestion._answeredState;
  }

  public boolean isAnswered() {
    return this._currentQuestion._answeredState != AnsweredState.UNANSWERED;
  }

  public Question getCurrentQuestion() {
    return this._currentQuestion._question;
  }

  public void setDisplay(QuestionListDisplay d) {
    this._display = d;
  }

  @Override
  public void keyTyped(KeyEvent e) {
  }

  @Override
  public void keyPressed(KeyEvent e) {
    if (e.getKeyCode() == 39) {
      System.out.println("Setting next image");
      this._currentQuestion = new QuestionDisplay(this._questionIterator.next());
      this._display.repaint();
    }
    System.out.printf("KEY TYPED: %d\n", e.getKeyCode());
  }

  @Override
  public void keyReleased(KeyEvent e) {
  }

  public void nextClick() {
    this._currentQuestion = new QuestionDisplay(this._questionIterator.next());
    this._display.repaint();
  }

  public void previousClick() {
    this._currentQuestion = new QuestionDisplay(this._questionIterator.prev());
    this._display.repaint();
  }

  public void shuffleClick() {
    this._questionIterator.shuffle();
    this._currentQuestion = new QuestionDisplay(this._questionIterator.next());
    this._display.repaint();
  }

  public void answerQuestion(Question.Answer answer) {
    this._currentQuestion.answer(answer);
    this._display.repaint();
  }

}
