/*
 * File Overview: TODO
 */
package ui.questions.flc;

import models.Answer;
import models.Flashcard;
import models.QType;
import ui.questions.AppCtrlImpl;

public class FlcCtrl extends AppCtrlImpl<Flashcard> {
  
  // Settings variables.
//  protected boolean recordAnswers = true;
//  protected boolean rememberAnswers = true;
//  protected boolean canChangeAnswers = true;
//  protected boolean recordTime = true;

  // Question list state.
//  protected boolean isStarted = false;
//  protected ArrayList<Q_TYPE> questions = null;
//  protected int totalNumber;
//  protected int currentQuestionIndex;
      
  // Current question state.
//  protected long questionStartTime;
//  protected long currentQuestionTime;
//  protected Response currentResponse = null;
//  protected Q_TYPE currentQuestion = null;
//  protected boolean isAnswered = false;
    protected boolean isRevealed = false;

  // History.
//  protected Map<Q_TYPE, Response> responseHistory = new HashMap<>();
//  protected int numberAnswered;
//  protected int numberTimesRecorded;
//  protected double totalQuestionTime;
    protected int totalConfidenceScore;
  
  public FlcCtrl() {
    super(new engine.ListFilter.NullFilter<Flashcard>(),
          new engine.ListSorter.IdSorter<Flashcard>());
    this.rememberAnswers = false;
  }
  
  @Override
  public void resetHistoryUpdate() {
    super.resetHistoryUpdate();
    this.totalConfidenceScore = 0;
  }
  
  public boolean isRevealed() throws NotStartedYetException {
    if (this.isStarted) {
      return this.isRevealed;
    } else {
      throw new NotStartedYetException();
    }
  }
  
  @Override
  protected void setQuestion(Flashcard q) {
    super.setQuestion(q);
    this.isRevealed = false;
  }
  
  @Override
  protected void answerUpdate(Answer answer, boolean recordTime) throws NotStartedYetException, AlreadyAnsweredException {
    boolean alreadyAnswered = this.isAnswered;
    Integer oldSelectedAnswer = this.isAnswered ? this.getSelectedAnswer().getFlashcardAnswer() : null;
    Integer newSelectedAnswer = answer.getFlashcardAnswer();
    
    super.answerUpdate(answer, recordTime);
    
    if (alreadyAnswered) {
      this.totalConfidenceScore -= oldSelectedAnswer;
    }
    this.totalConfidenceScore += newSelectedAnswer;
  }
  
  public final void reveal() throws AlreadyAnsweredException, NotStartedYetException {
    this.revealUpdate();
    this.updateObservers();
  }
  
  public final void hide() throws AlreadyAnsweredException, NotStartedYetException {
    this.hideUpdate();
    this.updateObservers();
  }
  
  public final void toggleVisibility() throws NotStartedYetException {
    this.toggleVisibilityUpdate();
    this.updateObservers();
  }
  
  protected void toggleVisibilityUpdate() throws NotStartedYetException {
    if (this.isStarted) {
      this.isRevealed = !this.isRevealed;
    } else {
      throw new NotStartedYetException();
    }
  }
  
  protected void revealUpdate() throws AlreadyAnsweredException, NotStartedYetException {
    if (this.isStarted && !this.isRevealed) {
      this.isRevealed = true;
    } else if (this.isStarted) {
      throw new AlreadyAnsweredException();
    } else {
      throw new NotStartedYetException();
    }
  }
  
  protected void hideUpdate() throws AlreadyAnsweredException, NotStartedYetException {
    if (this.isStarted && this.isRevealed) {
      this.isRevealed = false;
    } else if (this.isStarted) {
      throw new NotAnsweredYetException();
    } else {
      throw new NotStartedYetException();
    }
  }

  @Override
  public QType getType() {
    return QType.FLASHCARD;
  }
}
