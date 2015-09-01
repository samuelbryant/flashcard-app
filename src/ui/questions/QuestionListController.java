/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.questions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import models.Question;
import java.awt.event.KeyEvent;
import ui.FAController;

public class QuestionListController extends FAController<QuestionListDisplay> {

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
    private final Question.Answer _correctAnswer;

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

  private QuestionDisplay _currentQuestion;
  private final QuestionIterator _questionIterator;
  private State _state;

  public QuestionListController(QuestionIterator questionIterator) {
    super();
    this._questionIterator = questionIterator;
    this._currentQuestion = new QuestionDisplay(this._questionIterator.next());
    this._state = State.IN_PROGRESS;
    this._setupKeyMap();
      }
  
  private final void _setupKeyMap() {
    this.addKeyAction(39, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        QuestionListController.this.nextClick();
      }
    });
  
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
  

  public void nextClick() {
    this._currentQuestion = new QuestionDisplay(this._questionIterator.next());
    this.setChanged();
    this.update();
  }

  public void previousClick() {
    this._currentQuestion = new QuestionDisplay(this._questionIterator.prev());
    this.setChanged();
    this.update();
  }

  public void shuffleClick() {
    this._questionIterator.shuffle();
    this._currentQuestion = new QuestionDisplay(this._questionIterator.next());
    this.setChanged();
    this.update();
  }

  public void answerQuestion(Question.Answer answer) {
    this._currentQuestion.answer(answer);
    this.setChanged();
    this.update();
  }

}
