/*
 * File Overview: TODO
 */
package ui.questions.gre;

import models.Answer;
import models.QType;
import models.Question;
import ui.questions.AppCtrlImpl;

class GreCtrl extends AppCtrlImpl<Question> {
  
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
//  
  // History.
//  protected Map<Q_TYPE, Response> responseHistory = new HashMap<>();
//  protected int numberAnswered;
//  protected int numberTimesRecorded;
//  protected double totalQuestionTime;
  protected int numberCorrect;
  
  GreCtrl() {
    super(new engine.ListFilter.NullFilter<Question>(),
          new engine.ListSorter.IdSorter<Question>());
  }
  
  @Override
  public void resetHistoryUpdate() {
    super.resetHistoryUpdate();
    this.numberCorrect = 0;
  }
  
  public Answer getCorrectAnswer() throws NotStartedYetException {
    return this.getCurrentQuestion().getAnswer();
  }
  
  public boolean isAnsweredCorrectly() throws NotStartedYetException, NotAnsweredYetException {
    return this.getCorrectAnswer() == this.getSelectedAnswer();
  }
  
  public int getNumberCorrect() {
    return numberCorrect;
  }

  @Override
  public void answerUpdate(Answer answer, boolean recordTime) throws NotStartedYetException, AlreadyAnsweredException {
    boolean alreadyAnswered = this.isAnswered;
    Answer oldSelectedAnswer = this.isAnswered ? this.getSelectedAnswer() : null;
    
    super.answerUpdate(answer, recordTime);
    
    Answer correctAnswer = this.getCurrentQuestion().getAnswer();
    
    // If not already answered, update this.numberCorrect as usual.
    if (!alreadyAnswered) {
      if (answer == correctAnswer) {
        this.numberCorrect++;
      }
    }
    // If it was already answered and we can change answers, update numberCorrect accordingly.
    else if (this.canChangeAnswers) {
      Answer newSelectedAnswer = this.getSelectedAnswer();
      if (correctAnswer == oldSelectedAnswer && correctAnswer != newSelectedAnswer) {
        this.numberCorrect--;
      } else if (correctAnswer == newSelectedAnswer && correctAnswer != oldSelectedAnswer) {
        this.numberCorrect++;
      }
    }
  }

  @Override
  public QType getType() {
    return QType.GRE;
  }
  
}
