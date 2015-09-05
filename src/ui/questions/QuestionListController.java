package ui.questions;

import ui.questions.sorters.QuestionListSorter;
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
  private QuestionListSorter questionListSorter = null;
  private ArrayList<Question> questionList = null;
  private Integer totalQuestions = null;
  private Integer currentIndex = null;
  private final Database database;
  
  protected QuestionListController(Database db) {
    super();
    this.database = db;
    this.questionListSorter = QuestionListSorter.DEFAULT_SORTER;
    this.setQuestionList(this.database.getQuestionList());
  }
  
  public final void setQuestionListSorter(QuestionListSorter qls) {
    ArrayList<Question> baseList = this.questionListSorter.getBaseList();
    this.questionListSorter = qls;
    this.setQuestionList(baseList);
  }
  
  public final void setQuestionList(List<Question> list) {
    this.state = State.NOT_STARTED;
    ArrayList<Question> arrList = new ArrayList<>();
    for (Question q: list) {
      arrList.add(q);
    }
    this.questionListSorter.setBaseList(arrList);
    this.questionList = this.questionListSorter.getSortedArrayList();
    this.totalQuestions = this.questionList.size();
    this.currentIndex = null;
    this.setChanged();  // TODO: Is this necessary
    this.update();
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
