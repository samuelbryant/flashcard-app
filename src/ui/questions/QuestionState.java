package ui.questions;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import models.AbstractQuestion;
import models.Answer;
import models.Flashcard;
import models.Question;
import models.Response;
import ui.questions.QuestionList.NotStartedYetException;

/**
 *
 * @author sambryant
 * @param <Q_TYPE>
 * @param <SELF>
 * @param <LIST_TYPE>
 */
public abstract class QuestionState<
    SELF extends QuestionState<SELF, Q_TYPE, LIST_TYPE>,
    Q_TYPE extends AbstractQuestion,
    LIST_TYPE extends QuestionList<LIST_TYPE, Q_TYPE, SELF>>
    extends Observable implements Observer {

  public static class QuestionStateException extends RuntimeException {
    public QuestionStateException(String str) {
      super(str);
    }
  }

  public static class NotAnsweredYetException extends QuestionStateException {
    public NotAnsweredYetException() {
      super("Not answered yet");
    }
  }

  public static class AlreadyAnsweredException extends QuestionStateException {
    public AlreadyAnsweredException() {
      super("Already answered");
    }
  }

  protected LIST_TYPE _questionList;
  protected Q_TYPE _question;
  protected Answer _selectedAnswer;
  protected Boolean _isAnswered;
  protected Long _startTime;
  protected Long _endTime;
  protected Response _response;
  protected Map<Integer, Response> responseHistory = new HashMap<>();

  protected QuestionState() {}
  
  /**
   * Handler for updates from the question list.
   */
  @Override
  public void update(Observable o, Object arg) {
    try {
      Q_TYPE q = this._questionList.getCurrentQuestion();
      this.setQuestion(q);
    } catch(NotStartedYetException ex) {
      this.setQuestion(null);
    }
  }

  protected void setQuestion(Q_TYPE q) {
    if (q == null) {
      this._question = null;
      this._isAnswered = null;
      this._selectedAnswer = null;
      this._startTime = null;
      this._endTime = null;
      this._response = null;
    } else {
      this._question = q;
      this._selectedAnswer = null;
      this._isAnswered = false;
      this._startTime = System.nanoTime();
      this._endTime = null;
      this._response = null;
      if (this.responseHistory.containsKey(q.getId())) {
        this.setResponse(this.responseHistory.get(q.getId()));
      }
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

  void setResponse(Response r) {
    this._selectedAnswer = r.getSelectedAnswer();
    this._response = r;
    this._isAnswered = true;
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
    this.responseHistory.put(this._questionList.getCurrentQuestion().getId(), _response);
    this.setChanged();
    this.notifyObservers();
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
      return (int) this._response.getTimeInSeconds().intValue();
    } else {
      throw new NotAnsweredYetException();
    }
  }

}
