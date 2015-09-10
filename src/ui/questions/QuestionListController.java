package ui.questions;

import engine.ListFilter;
import engine.ListSorter;
import models.Answer;
import models.DatabaseIO;
import models.Response;
import ui.core.Controller;

/**
 *
 * @author sambryant
 */
public class QuestionListController extends Controller<QuestionListDisplay> {

  // Question list variables.
  private final QuestionList _questionList;
  private boolean recordAnswers;
  private Integer _numberAnswered;
  private Integer _numberCorrect;
  private Integer _totalQuestionTime;

  /**
   *
   */
  public QuestionListController() {
    this._questionList = new QuestionList(ListFilter.NULL_FILTER, ListSorter.ID_SORTER);
    this.recordAnswers = false;
    this._numberAnswered = 0;
    this._numberCorrect = 0;
    this._totalQuestionTime = 0;
  }

  /**
   *
   * @return
   */
  public boolean getRecordAnswers() {
    return this.recordAnswers;
  }
  
  public int getNumberAnswered() {
    return this._numberAnswered;
  }
  
  public int getNumberCorrect() {
    return this._numberCorrect;
  }
  
  public int getTotalQuestionTime() {
    return this._totalQuestionTime;
  }

  /**
   *
   * @param value
   */
  public void setRecordAnswers(boolean value) {
    this.recordAnswers = value;
  }

  // Question List Controller Proper Methods.

  /**
   *
   */
    public void save() {
    DatabaseIO.saveDatabase();
  }

  /**
   *
   * @param answer
   */
  public void answer(Answer answer) {
    if (this.getQuestionState().isAnswered()) {
      this.getQuestionState().changeAnswer(answer);
    } else {
      this.getQuestionState().answer(answer);
      
      // Record answer to database.
      if (this.recordAnswers) {
        Response r = this.getQuestionState().getResponseObject();
        this._questionList.getCurrentQuestion().addResponse(r);
      }
      
      // Record question list stats.
      this._numberAnswered++;
      if (answer == this._questionList.getCurrentQuestion().getAnswer()) {
        this._numberCorrect++;
      }
      this._totalQuestionTime += this.getQuestionState().getLastResponseTime();
      
    }
  }

  /**
   *
   * @return
   */
  public QuestionList getQuestionList() {
    return this._questionList;
  }

  /**
   *
   * @return
   */
  public QuestionState getQuestionState() {
    return this._questionList.getQuestionState();
  }

  /**
   *
   */
  public void initialUpdate() {
    this._questionList.initialUpdate();
  }

}
