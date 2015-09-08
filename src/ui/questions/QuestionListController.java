package ui.questions;

import engine.ListFilter;
import engine.ListSorter;
import models.Answer;
import models.DatabaseIO;
import models.Response;
import models.Tag;
import ui.core.Controller;

public class QuestionListController extends Controller<QuestionListDisplay> {

  // Question list variables.
  private final QuestionList _questionList;
  private boolean recordAnswers;

  public QuestionListController() {
    this._questionList = new QuestionList(ListFilter.NULL_FILTER, ListSorter.ID_SORTER);
    this.recordAnswers = false;
  }

  public boolean getRecordAnswers() {
    return this.recordAnswers;
  }

  public void setRecordAnswers(boolean value) {
    this.recordAnswers = value;
  }

  // Question List Controller Proper Methods.
  public void save() {
    DatabaseIO.saveDatabase();
  }

  public void answer(Answer answer) {
    if (this.getQuestionState().isAnswered()) {
      this.getQuestionState().changeAnswer(answer);
    } else {
      this.getQuestionState().answer(answer);
      if (this.recordAnswers) {
        Response r = this.getQuestionState().getResponseObject();
        this._questionList.getCurrentQuestion().addResponse(r);
      }
    }
  }

  public QuestionList getQuestionList() {
    return this._questionList;
  }

  public QuestionState getQuestionState() {
    return this._questionList.getQuestionState();
  }

  public void initialUpdate() {
    this._questionList.initialUpdate();
  }

}
