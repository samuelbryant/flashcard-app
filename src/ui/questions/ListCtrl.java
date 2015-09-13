package ui.questions;

import engine.ListFilter;
import engine.ListSorter;
import models.AbstractQuestion;
import models.Answer;
import models.QType;

public interface ListCtrl <Q_TYPE extends AbstractQuestion> {  
  public QType getType();
  
  // Other
  public void updateObservers();
  
  // Macro state change functions.
  public void resetHistory();
  public void setList(ListFilter<Q_TYPE> filter, ListSorter<Q_TYPE> sorter);
  public void setRecordTimes(boolean value);
  public void save();
  
  // Micro state changing functions.
  public void nextQuestion() throws NoQuestionsException;
  public void prevQuestion() throws NotStartedYetException, NoQuestionsException;
  public void answer(Answer answer) throws NotStartedYetException, AlreadyAnsweredException;
  
  // Getting list information.
  public int getQuestionNumber();
  public int getCurrentIndex() throws NotStartedYetException;
  public Q_TYPE getCurrentQuestion() throws NotStartedYetException;
  public Answer getSelectedAnswer() throws NotStartedYetException, NotAnsweredYetException;
  public int getLastResponseTime() throws NotStartedYetException, NotAnsweredYetException;
  
  // Getting history.
  public int getNumAnswered();
  public double getTotalQuestionTime();
  public double getAverageQuestionTime() throws NotAnsweredYetException;
  public boolean hasAverageQuestionTime();
  
  // Getting boolean state information.
  public boolean isStarted();
  public boolean isAnswered();
  public boolean hasNextQuestion();
  public boolean hasPrevQuestion();
  public boolean canAnswerQuestion();

  public static class ListCtrlException extends RuntimeException {}
  public static class NoQuestionsException extends ListCtrlException {}
  public static class NotStartedYetException extends ListCtrlException {}
  public static class NotAnsweredYetException extends ListCtrlException {}
  public static class AlreadyAnsweredException extends ListCtrlException {}
  
}
