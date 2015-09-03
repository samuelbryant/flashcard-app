package ui.questions;

import java.util.ArrayList;
import models.Answer;
import models.Database;
import models.DatabaseIO;
import java.util.List;
import models.Question;
import ui.Controller;

public class QuestionListController extends Controller<QuestionListDisplay> {

  public static class OutOfQuestionsException extends Exception {
    public OutOfQuestionsException(String str) {
      super(str);
    }
  }
  
  /**
   * Enum capturing state of question list controller.
   */
  public enum State {
    NOT_STARTED, COMPLETED, IN_PROGRESS;
  }

  private QuestionDisplay currentQuestion;
  private State state;
  private ArrayList<Question> questionList = null;
  private Integer totalQuestions = null;
  private Integer currentIndex = null;
  private final Database database;
  
  protected QuestionListController(Database db) {
    super();
    this.database = db;
    this.setQuestionList(this.database.getQuestionList());
  }
  
  public final void setQuestionList(List<Question> list) {
    this.state = State.NOT_STARTED;
    this.questionList = new ArrayList<>();
    for (Question q: list) {
      this.questionList.add(q);
    }
    this.totalQuestions = this.questionList.size();
    this.currentIndex = null;
  }
  
  public ArrayList<Question> getQuestionList() {
    return this.questionList;
  }
  
  public void saveToDatabase() {
    DatabaseIO.writeDatabase(this.database);
    System.out.printf("LOG: saveToDatabase completed\n");
  }

  public void start() {
    if (this.state != State.NOT_STARTED) {
      throw new IllegalStateException("Cannot start project in state: " + this.state);
    }
    this.state = State.IN_PROGRESS;
    this.currentIndex = 0;
    this.currentQuestion = new QuestionDisplay(this.questionList.get(0));
    this.setChanged();  // TODO: Is this necessary
    this.update();
  }
  
  protected State getState() {
    return this.state;
  }
  
  public boolean isInProgress() {
    return this.state == State.IN_PROGRESS;
  }
  
  public boolean isNotStarted() {
    return this.state == State.NOT_STARTED;
  }
  
  public boolean isFinished() {
    return this.state == State.COMPLETED;
  }
  
  public boolean isAnswered() {
    return this.currentQuestion.isAnswered;
  }
  
  public Answer getSelectedAnswer() {
    return this.currentQuestion.selectedAnswer;
  }
  
  public Answer getCorrectAnswer() {
    return this.currentQuestion.correctAnswer;
  }
  
  public Question getCurrentQuestion() {
    return this.currentQuestion._question;
  }
  
  public boolean hasNextQuestion() {
    return this.currentIndex < (this.totalQuestions - 1);
  }

  public boolean hasPreviousQuestion() {
    return this.currentIndex > 0;
  }
  
  public void answerQuestion(Answer answer) {
    this.currentQuestion.answer(answer);
    this.setChanged();
    this.update();
  }
  
  protected void setQuestion(Integer index) {
    this.currentIndex = index;
    this.currentQuestion = new QuestionDisplay(this.questionList.get(index));
    this.setChanged();  // TODO: Is this necessary
    this.update();
  }
  
  public void nextQuestion() throws OutOfQuestionsException {
    if (this.isNotStarted()) {
      this.start();
    } else if (this.hasNextQuestion()) {
      this.setQuestion(this.currentIndex + 1);
    } else {
      throw new OutOfQuestionsException("Reached end of question list");
    }
  }
  
  public void previousQuestion() throws OutOfQuestionsException {
    if (this.hasPreviousQuestion()) {
      this.setQuestion(this.currentIndex - 1);
    } else {
      throw new OutOfQuestionsException("Reached start of question list");
    }
  }

//  public void saveToDatabase() {
//    if (_database == null) {
//      throw new IllegalStateException("Database reference not available");
//    }
//    DatabaseIO.writeDatabase(this._database);
//    System.out.printf("LOG: saveToDatabase completed\n");
//  }
//  
//  public void setQuestionList(List<Question> list) {
//    this._currentQuestion = null;
//    this._state = State.NOT_STARTED;
//    this._questionList = list;
//    this._questionIterator = new QuestionListIterator(this._questionList);
//    this.update();
//  }
//
//  public Answer getCorrectAnswer() {
//    return this._currentQuestion._correctAnswer;
//  }
//
//  public Answer getSelectedAnswer() {
//    return this._currentQuestion._selectedAnswer;
//  }
//
//  public AnsweredState getAnsweredState() {
//    return this._currentQuestion._answeredState;
//  }
//
//  public boolean isAnswered() {
//    return this._currentQuestion._answeredState != AnsweredState.UNANSWERED;
//  }
//
//  public Question getCurrentQuestion() {
//    return this._currentQuestion._question;
//  }
//
//  public void nextClick() {
//    this._currentQuestion = new QuestionDisplay(this._questionIterator.next());
//    this.setChanged();
//    this.update();
//  }
//
//  public void previousClick() {
//    this._currentQuestion = new QuestionDisplay(this._questionIterator.prev());
//    this.setChanged();
//    this.update();
//  }
//
//  public void shuffleClick() {
//    this._questionIterator.shuffle();
//    this._currentQuestion = new QuestionDisplay(this._questionIterator.next());
//    this.setChanged();
//    this.update();
//  }
//
//  public void answerQuestion(Answer answer) {
//    this._currentQuestion.answer(answer);
//    this.setChanged();
//    this.update();
//  }
//  
//  public Database getDatabase() {
//    return this._database;
//  }

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
    private final Answer correctAnswer;
    private Answer selectedAnswer;
    private boolean isAnswered;

    public QuestionDisplay(Question q) {
      this._question = q;
      this.isAnswered = false;
      this.selectedAnswer = null;
      this.correctAnswer = _question.getAnswer();
    }

    private boolean answer(Answer answer) {
      this.selectedAnswer = answer;
      this.isAnswered = true;
      if (answer == correctAnswer) {
        System.out.println("CORRECT!");
        return true;
      } else {
        System.out.println("WRONG!");
        return false;
      }
    }
  }
  
}
