package ui.questions;

import core.FatalError;
import engine.ListFilter;
import engine.ListSorter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import models.AbstractQuestion;
import models.Answer;
import models.Database;
import models.DatabaseIO;
import models.QType;
import models.Response;
import ui.core.Controller;

public abstract class AppCtrlImpl <Q_TYPE extends AbstractQuestion> extends Controller<AppDisplay> implements AppCtrl<Q_TYPE> {
  
  // Settings variables.
  protected boolean recordAnswers = true;
  protected boolean rememberAnswers = true;
  protected boolean canChangeAnswers = true;
  private boolean recordTime = true;
  
  // Question list state.
  protected boolean isStarted = false;
  protected ArrayList<Q_TYPE> questions = null;
  protected int totalNumber;
  protected int currentQuestionIndex;
      
  // Current question state.
  protected long questionStartTime;
  protected long currentQuestionTime;
  protected Response currentResponse = null;
  protected Q_TYPE currentQuestion = null;
  protected boolean isAnswered = false;
  
  // History.
  protected Map<Q_TYPE, Response> responseHistory = new HashMap<>();
  protected int numberAnswered;
  protected int numberTimesRecorded;
  protected double totalQuestionTime;
  
  public AppCtrlImpl(ListFilter<Q_TYPE> filter, ListSorter<Q_TYPE> sorter) {
    this.initialize(filter, sorter);
  }
  
  public AppCtrlImpl(
      ListFilter<Q_TYPE> filter, ListSorter<Q_TYPE> sorter,
      boolean recordAnswers, boolean rememberAnswers, boolean canChangeAnswer) {
    this.recordAnswers = recordAnswers;
    this.rememberAnswers = rememberAnswers;
    this.canChangeAnswers = canChangeAnswer;
    this.initialize(filter, sorter);
  }
  
  private void initialize(ListFilter<Q_TYPE> filter, ListSorter<Q_TYPE> sorter) {
    this.setList(filter, sorter);
    this.resetHistory();
  }
  
  @Override
  public void updateObservers() {
    this.setChanged();
    this.notifyObservers();
  }
  
  @Override
  public void save() {
    DatabaseIO.getDatabaseIO(this.getType()).save();
    System.out.println("Database saved!");
  }
  
  @Override
  public final void setList(ListFilter<Q_TYPE> filter, ListSorter<Q_TYPE> sorter) {
    this.setListUpdate(filter, sorter);
    this.updateObservers();
  }
  
  protected void setListUpdate(ListFilter<Q_TYPE> filter, ListSorter<Q_TYPE> sorter) {
    DatabaseIO<Q_TYPE> io = (DatabaseIO<Q_TYPE>) DatabaseIO.getDatabaseIO(this.getType());
    Database<Q_TYPE> db = io.get();
    this.questions = db.getQuestions(filter, sorter);
    this.totalNumber = this.questions.size();
    System.out.println("Total number: " + this.totalNumber);
    this.currentQuestionIndex = -1;
    this.isStarted = false;
    this.setQuestion(null);
  }
  
  @Override
  public final void resetHistory() {
    this.resetHistoryUpdate();
    this.updateObservers();
  }
  
  protected void resetHistoryUpdate() {
    this.responseHistory.clear();
    this.numberAnswered = 0;
    this.totalQuestionTime = 0.0;
    this.numberTimesRecorded = 0;
  }

  @Override
  public int getQuestionNumber() {
    return this.totalNumber;
  }
  
  @Override
  public int getCurrentIndex() throws NotStartedYetException {
    if (this.isStarted) {
      return this.currentQuestionIndex;
    } else {
      throw new NotStartedYetException();
    }
  }
  
  @Override
  public Q_TYPE getCurrentQuestion() throws NotStartedYetException {
    if (this.isStarted) {
      return this.currentQuestion;
    } else {
      throw new NotStartedYetException();
    }
  }
  
  @Override
  public Answer getSelectedAnswer() throws NotStartedYetException, NotAnsweredYetException {
    if (this.isStarted && this.isAnswered) {
      return this.currentResponse.getSelectedAnswer();
    } else if (this.isStarted) {
      throw new NotAnsweredYetException();
    } else {
      throw new NotStartedYetException();
    }
  }
  
  @Override
  public int getLastResponseTime() throws NotStartedYetException, NotAnsweredYetException {
    if (this.isStarted && this.isAnswered) {
      return (int) (this.currentQuestionTime / 1000000000);
    } else if (this.isStarted) {
      throw new NotAnsweredYetException();
    } else {
      throw new NotStartedYetException();
    }
  }
  
  @Override
  public int getNumAnswered() {
    return this.numberAnswered;
  }

  @Override
  public double getTotalQuestionTime() {
    return this.totalQuestionTime;
  }

  @Override
  public double getAverageQuestionTime() throws NotAnsweredYetException {
    if (this.hasAverageQuestionTime()) {
      return this.totalQuestionTime / this.numberTimesRecorded;
    } else {
      throw new NotAnsweredYetException();
    }
  }

  @Override
  public boolean hasAverageQuestionTime() {
    return this.numberTimesRecorded > 0;
  }
  
  @Override
  public boolean isStarted() {
    return this.isStarted;
  }

  @Override
  public boolean isAnswered() {
    return this.isStarted && this.isAnswered;
  }

  @Override
  public boolean hasNextQuestion() {
    if (this.isStarted) {
      return this.currentQuestionIndex + 1 < this.totalNumber;
    } else {
      return this.totalNumber > 0;
    }
  }

  @Override
  public boolean hasPrevQuestion() {
    if (this.isStarted) {
      return this.currentQuestionIndex > 0;
    } else {
      return false;
    }
  }
  
  protected void setQuestion(Q_TYPE q) {
    this.currentQuestion = q;

    if (q != null) {
      // Check if this question has already been answered in this session's history.
      if (this.rememberAnswers && this.responseHistory.containsKey(this.currentQuestion)) {
        this.setResponse(this.responseHistory.get(this.currentQuestion));
      } else {
        this.setResponse(null);
      }
      this.questionStartTime = System.nanoTime();
    } else {
      this.setResponse(null);
      this.questionStartTime = -1;
    }
  }
  
  protected void setResponse(Response r) {
    if (r == null) {
      this.currentResponse = null;
      this.isAnswered = false;
      this.currentQuestionTime = -1;
    } else {
      this.currentResponse = r;
      this.isAnswered = true;
      if (this.currentResponse.getResponseTime() != null) {
        this.currentQuestionTime = this.currentResponse.getResponseTime();
      } else {
        this.currentQuestionTime = -1;
      }
      this.responseHistory.put(currentQuestion, currentResponse);
    }
  }

  @Override
  public final void nextQuestion() throws NoQuestionsException {
    this.nextQuestionUpdate();
    this.updateObservers();
  }
  
  protected void nextQuestionUpdate() throws NoQuestionsException {
    if (this.hasNextQuestion()) {
      if (this.isStarted) {
        this.currentQuestionIndex++;
      } else {
        this.currentQuestionIndex = 0;
        this.isStarted = true;
      }
      this.setQuestion(this.questions.get(this.currentQuestionIndex));
    } else {
      throw new NoQuestionsException();
    }
  }

  @Override
  public final void prevQuestion() throws NotStartedYetException, NoQuestionsException {
    this.prevQuestionUpdate();
    this.updateObservers();
  }
  
  protected void prevQuestionUpdate() throws NotStartedYetException, NoQuestionsException {
    if (this.hasPrevQuestion()) {
      this.currentQuestionIndex--;
      this.setQuestion(this.questions.get(this.currentQuestionIndex));
    } else if (this.isStarted) {
      throw new NoQuestionsException();
    } else {
      throw new NotStartedYetException();
    }
  }
  
  @Override
  public boolean canAnswerQuestion() {
    if (this.isStarted) {
      if (!this.isAnswered) {
        return true;
      } else if (this.canChangeAnswers) {
        return true;
      }
    }
    return false;
  }
  
  @Override
  public final void answer(Answer answer, boolean recordTime) throws NotStartedYetException, AlreadyAnsweredException {
    this.answerUpdate(answer, recordTime);
    this.updateObservers();
  }
  
  protected void answerUpdate(Answer answer, boolean recordTime) throws NotStartedYetException, AlreadyAnsweredException {
    if (!this.canAnswerQuestion()) {
      if (!this.isStarted) {
        throw new NotStartedYetException();
      } else if (this.isAnswered && !this.canChangeAnswers) {
        throw new AlreadyAnsweredException();
      } else throw new FatalError("Unreachable line");
    }
    
    if (!this.isAnswered) {
      long endTime = System.nanoTime();
      
      Long qTime = null;
      // Only record time if flag is set.
      if (recordTime) {
        qTime = endTime - this.questionStartTime;
        this.numberTimesRecorded++;
        this.totalQuestionTime += ((double) qTime) / (1000000000);
      }
      
      this.setResponse(new Response(answer, qTime, new Date()));

      this.numberAnswered++;
                
      if (this.recordAnswers) {
        this.currentQuestion.addResponse(currentResponse);
      }
    }
    // Change answer.
    else {
      this.currentResponse.setAnswer(answer);
    }
  }
  
  @Override
  public void setRecordTimes(boolean value) {
    this.recordTime = value;
  }
  
  @Override
  public boolean getRecordTimes() {
    return this.recordTime;
  }
  
  public void setRecordAnswers(boolean value) {
    this.recordAnswers = value;
  }
  
  public void setCanChangeAnswers(boolean value) {
    this.canChangeAnswers = value;
  }
  
  public void setRememberAnswers(boolean value) {
    this.rememberAnswers = value;
  }
  
  @Override
  public void refresh() {
    for (Q_TYPE q: ((Database<Q_TYPE>) DatabaseIO.getDatabaseIO(this.getType()).get()).getQuestions()) {
      q.refreshImages();
    }
    this.requestFocus();
  }
  
  @Override
  public abstract QType getType();
  
}
