package ui.questions;

import engine.ListFilter;
import engine.ListSorter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import models.AbstractQuestion;
import models.Answer;
import models.Database;
import models.DatabaseIO;
import models.QType;
import models.Response;

/**
 * Class which captures state of a list of questions.
 * This includes keeping track of current question index, total number of questions, etc.
 * @param <Q_TYPE>
 * @param <SELF>
 * @param <STATE_TYPE>
 */
public abstract class QuestionList<
    SELF extends QuestionList<SELF, Q_TYPE, STATE_TYPE>,
    Q_TYPE extends AbstractQuestion,
    STATE_TYPE extends QuestionState<STATE_TYPE, Q_TYPE, SELF>>
 extends Observable {
  
  public static class QuestionListException extends Exception {
  public QuestionListException(String str) {
      super(str);
    }
  }
  public static class OutOfQuestionsException extends QuestionListException {
    public OutOfQuestionsException(String str) {
      super(str);
    }
  }
  public static class NotStartedYetException extends RuntimeException {
    public NotStartedYetException(String str) {
      super(str);
    }
  }
  public static class NoQuestionsException extends QuestionListException {
    public NoQuestionsException(String str) {
      super(str);
    }
  }
  public static enum State {
    NOT_STARTED,
    STARTED;
  }

  protected STATE_TYPE _questionState;
  protected ArrayList<Q_TYPE> _questionList;
  protected ListFilter<Q_TYPE> _listFilter;
  protected ListSorter<Q_TYPE> _listSorter;
  protected Integer _currentIndex;
  protected Integer _totalNumber;
  protected State _state;
  protected Map<Q_TYPE, Response> responseValues = new HashMap<>();

  protected QuestionList(ListFilter<Q_TYPE> filter, ListSorter<Q_TYPE> sorter) {
    this._listFilter = filter;
    this._listSorter = sorter;
    this._state = State.NOT_STARTED;
  }
  
  protected abstract QType getType();
  
  void _resetList() {
    DatabaseIO<Q_TYPE> io = (DatabaseIO<Q_TYPE>) DatabaseIO.getDatabaseIO(this.getType());
    Database<Q_TYPE> db = io.get();
 
    this._questionList = db.getQuestions(_listFilter, _listSorter);
    this._currentIndex = null;
    this._totalNumber = this._questionList.size();
    this._state = State.NOT_STARTED;
    this.setChanged();
    this.notifyObservers();
  }

  /**
   *
   * @param filter
   * @param sorter
   */
  public void setFilterSorter(ListFilter<Q_TYPE> filter, ListSorter<Q_TYPE> sorter) {
    this._listFilter = filter;
    this._listSorter = sorter;
    this._resetList();
  }

  public void answer(Answer answer) {
    if (this.getQuestionState().isAnswered()) {
      this.getQuestionState().changeAnswer(answer);
    } else {
      this.getQuestionState().answer(answer);
      this.responseValues.put(this.getCurrentQuestion(), this.getQuestionState().getResponseObject());
    }
  }
  
  public Boolean isStarted() {
    return this._state == State.STARTED;
  }

  public STATE_TYPE getQuestionState() {
    return this._questionState;
  }

  public Integer getNumberOfQuestions() {
    return this._totalNumber;
  }

  public Integer getCurrentIndex() throws NotStartedYetException {
    if (this._state != State.STARTED) {
      throw new NotStartedYetException("Question list not started yet");
    } else {
      return this._currentIndex;
    }
  }

  public Q_TYPE getCurrentQuestion() throws NotStartedYetException {
    if (this._state != State.STARTED) {
      throw new NotStartedYetException("Question list not started yet");
    } else {
      return this._questionList.get(this._currentIndex);
    }
  }

  public Boolean hasNextQuestion() {
    if (this._state != State.STARTED) {
      return this._totalNumber > 0;
    } else {
      return this._currentIndex + 1 < this._totalNumber;
    }
  }

  public Boolean hasLastQuestion() {
    if (this._state != State.STARTED) {
      return false;
    } else {
      return this._currentIndex > 0;
    }
  }
  
  private void questionChanged() {
    this.setChanged();
    this.notifyObservers();
  }

  public void nextQuestion() throws OutOfQuestionsException {
    if (this._state == State.NOT_STARTED) {
      this._currentIndex = -1;
      this._state = State.STARTED;
      this.setChanged();
    }
    if (this.hasNextQuestion()) {
      this._currentIndex++;
      this.questionChanged();
    } else {
      this.notifyObservers();
      throw new OutOfQuestionsException("No more questions (total: " + this._totalNumber + ")");
    }
  }

  public void lastQuestion() throws OutOfQuestionsException, NotStartedYetException {
    if (this._state == State.NOT_STARTED) {
      throw new NotStartedYetException("Cannot go back when quiz not started");
    }
    if (this.hasLastQuestion()) {
      this._currentIndex--;
      this.questionChanged();
    } else {
      throw new OutOfQuestionsException("No more questions (total: " + this._totalNumber + ")");
    }
  }

  void initialUpdate() {
    this.setChanged();
    this.notifyObservers();
  }

}
