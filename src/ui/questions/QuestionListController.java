package ui.questions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import models.Answer;
import models.Database;
import models.DatabaseIO;
import java.util.List;
import models.Question;
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
    private Answer _selectedAnswer;
    private final Answer _correctAnswer;

    public QuestionDisplay(Question q) {
      this._question = q;
      this._answeredState = AnsweredState.UNANSWERED;
      this._selectedAnswer = null;
      this._correctAnswer = _question.getAnswer();
    }

    private void answer(Answer answer) {
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

  protected QuestionDisplay _currentQuestion;
  protected QuestionIterator _questionIterator;
  protected State _state;
  private Database _database = null;
  protected List<Question> _questionList = null;

  protected QuestionListController(Database db) {
    this._database = db;
    this._setupKeyMap();
  }

  public QuestionListController(QuestionIterator questionIterator) {
    super();
    this._questionIterator = questionIterator;
    this._currentQuestion = new QuestionDisplay(this._questionIterator.next());
    this._state = State.IN_PROGRESS;
    this._setupKeyMap();
  }

  public QuestionListController(Database db, List<Question> questionList) {
    super();
    this._questionIterator = new QuestionListIterator(questionList);
    this._questionList = questionList;
    this._database = db;
    this._currentQuestion = new QuestionDisplay(this._questionIterator.next());
    this._state = State.IN_PROGRESS;
    this._setupKeyMap();
  }

  private void _setupKeyMap() {
    this.addKeyAction(39, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        QuestionListController.this.nextClick();
      }
    });
  }

  public void saveToDatabase() {
    if (_database == null) {
      throw new IllegalStateException("Database reference not available");
    }
    DatabaseIO.writeDatabase(this._database);
    System.out.printf("LOG: saveToDatabase completed\n");
  }

  public Answer getCorrectAnswer() {
    return this._currentQuestion._correctAnswer;
  }

  public Answer getSelectedAnswer() {
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

  public void answerQuestion(Answer answer) {
    this._currentQuestion.answer(answer);
    this.setChanged();
    this.update();
  }

}
