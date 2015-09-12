package ui.questions;

import models.AbstractQuestion;
import models.Answer;
import models.DatabaseIO;
import models.Response;
import models.QType;
import ui.core.Controller;
import ui.questions.gre.GreCtrl;

public abstract class QuestionListController<
    Q_TYPE extends AbstractQuestion,
    STATE_TYPE extends QuestionState<STATE_TYPE, Q_TYPE, LIST_TYPE>,
    LIST_TYPE extends QuestionList<LIST_TYPE, Q_TYPE, STATE_TYPE>>
  extends Controller<QuestionListDisplay> {

  // Question list variables.
  protected STATE_TYPE questionState;
  protected LIST_TYPE questionList;
  protected boolean recordAnswers;
  protected Integer numberAnswered;
  protected Integer totalQuestionTime;

  protected QuestionListController(LIST_TYPE list, STATE_TYPE state) {
    this.questionList = list;
    this.questionState = state;
    this.recordAnswers = false;
    this.numberAnswered = 0;
    this.totalQuestionTime = 0;
    this.bind();
  }
  
  private void bind() {
    questionList._questionState = questionState;
    questionState._questionList = questionList;
    questionList.addObserver(questionState);
    questionList._resetList();
  }

  public boolean getRecordAnswers() {
    return this.recordAnswers;
  }
  
  public int getNumberAnswered() {
    return this.numberAnswered;
  }
  
  public int getTotalQuestionTime() {
    return this.totalQuestionTime;
  }

  public void setRecordAnswers(boolean value) {
    this.recordAnswers = value;
  }

  // Question List Controller Proper Methods.

  public void save() {
    DatabaseIO.getQuestionDatabaseIO().save();
  }

  public void answer(Answer answer) {
    if (this.getQuestionState().isAnswered()) {
      this.getQuestionState().changeAnswer(answer);
    } else {
      this.getQuestionState().answer(answer);
      
      // Record answer to database.
      if (this.recordAnswers) {
        Response r = this.getQuestionState().getResponseObject();
        this.questionList.getCurrentQuestion().addResponse(r);
      }
      
      // Record question list stats.
      this.numberAnswered++;
      this.totalQuestionTime += this.getQuestionState().getLastResponseTime();      
    }
  }
  
  public Q_TYPE getQuestion() {
    return this.questionList.getCurrentQuestion();
  }

  public LIST_TYPE getQuestionList() {
    return this.questionList;
  }

  public STATE_TYPE getQuestionState() {
    return this.questionList.getQuestionState();
  }

  public void initialUpdate() {
    this.questionList.initialUpdate();
  }
  
  public static QuestionListController createCtrl(QType t) {
    if (t == QType.GRE) {
      return new GreCtrl();
    } else {
      throw new UnsupportedOperationException("Not implemented yet");
    }
  }

}
