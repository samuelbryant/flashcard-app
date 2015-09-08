package ui.questions;

import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import models.Answer;
import models.Question;
import models.Response;
import ui.questions.QuestionList.NotStartedYetException;

/**
 *
 * @author sambryant
 */
public class QuestionState extends Observable implements Observer {

  /**
   *
   */
  public static class QuestionStateException extends RuntimeException {

    /**
     *
     * @param str
     */
    public QuestionStateException(String str) {
      super(str);
    }
  }

  /**
   *
   */
  public static class NotAnsweredYetException extends QuestionStateException {

    /**
     *
     */
    public NotAnsweredYetException() {
      super("Not answered yet");
    }
  }

  /**
   *
   */
  public static class AlreadyAnsweredException extends QuestionStateException {

    /**
     *
     */
    public AlreadyAnsweredException() {
      super("Already answered");
    }
  }

  private final QuestionList _questionList;
  private Question _question;
  private Answer _correctAnswer;
  private Answer _selectedAnswer;
  private Boolean _isAnswered;
  private Long _startTime;
  private Long _endTime;
  private Response _response;

  QuestionState(QuestionList questionList) {
    this._questionList = questionList;
    this._setQuestion(null);
  }

  /**
   * Handler for updates from the question list.
   */
  @Override
  public void update(Observable o, Object arg) {
    try {
      this._setQuestion(this._questionList.getCurrentQuestion());
    } catch(NotStartedYetException ex) {
      this._setQuestion(null);
    }
  }

  private void _setQuestion(Question q) {
    if (q == null) {
      this._question = null;
      this._isAnswered = null;
      this._selectedAnswer = null;
      this._correctAnswer = null;
      this._startTime = null;
      this._endTime = null;
      this._response = null;
    } else {
      this._question = q;
      this._correctAnswer = q.getAnswer();
      this._selectedAnswer = null;
      this._isAnswered = false;
      this._startTime = System.nanoTime();
      this._endTime = null;
      this._response = null;
    }
    this.setChanged();
    this.notifyObservers();
  }

  void changeAnswer(Answer answer) throws NotAnsweredYetException {
    if (this.isAnswered()) {
      this._response.setAnswer(answer);
      this._selectedAnswer = answer;
      this.setChanged();
      this.notifyObservers();
    } else {
      throw new NotAnsweredYetException();
    }
  }

  void answer(Answer answer) throws AlreadyAnsweredException, NotStartedYetException {
    if (this.isAnswered()) {
      throw new AlreadyAnsweredException();
    }
    // TODO: Stop clocks
    this._endTime = System.nanoTime();
    this._selectedAnswer = answer;
    this._isAnswered = true;
    this._response = new Response(this._selectedAnswer, this._endTime - this._startTime, new Date());
    this.setChanged();
    this.notifyObservers();
  }

  /**
   *
   * @return
   * @throws NotAnsweredYetException
   * @throws NotStartedYetException
   */
  public boolean isAnsweredCorrectly() throws NotAnsweredYetException, NotStartedYetException {
    if (this.isAnswered()) {
      return this._selectedAnswer == this._correctAnswer;
    } else {
      throw new NotAnsweredYetException();
    }
  }

  /**
   *
   * @return
   * @throws NotAnsweredYetException
   * @throws NotStartedYetException
   */
  public Response getResponseObject() throws NotAnsweredYetException, NotStartedYetException {
    if (this.isAnswered()) {
      return this._response;
    } else {
      throw new NotAnsweredYetException();
    }
  }

  /**
   *
   * @return
   * @throws NotStartedYetException
   * @throws NotAnsweredYetException
   */
  public Answer getSelectedAnswer() throws NotStartedYetException, NotAnsweredYetException {
    if (this.isAnswered()) {
      return this._selectedAnswer;
    } else {
      throw new NotAnsweredYetException();
    }
  }

  /**
   *
   * @return
   * @throws NotStartedYetException
   */
  public Answer getCorrectAnswer() throws NotStartedYetException {
    if (this._questionList.isStarted()) {
      return this._correctAnswer;
    } else {
      throw new QuestionList.NotStartedYetException("Not started yet");
    }
  }

  /**
   *
   * @return
   * @throws NotStartedYetException
   */
  public boolean isAnswered() throws NotStartedYetException {
    if (this._questionList.isStarted()) {
      return this._isAnswered;
    } else {
      throw new QuestionList.NotStartedYetException("Not started yet");
    }
  }

  /**
   *
   * @return
   * @throws NotAnsweredYetException
   * @throws NotStartedYetException
   */
  public int getLastResponseTime() throws NotAnsweredYetException, NotStartedYetException {
    if (this.isAnswered()) {
      return (int) (Math.floorDiv(this._endTime - this._startTime, 1000000000));
    } else {
      throw new NotAnsweredYetException();
    }
  }

}
