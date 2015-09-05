package ui.questions.quiz;

import java.util.Date;
import models.Answer;
import models.Database;
import models.Question;
import models.Response;
import ui.questions.QuestionListController;

public class QuizController extends QuestionListController {

  private long startTime;
  private long lastTime;
  
  public QuizController(Database db) {
    super(db);
  }
  
  public int getLastQuestionTime() {
    return (int) Math.floorDiv(lastTime, 1000000000l);
  }
  
  @Override
  public void nextQuestion() {
    try {
      super.nextQuestion();
      startTime = System.nanoTime();
    } catch (OutOfQuestionsException ex) {
      System.out.printf("LOG: No more questions\n");
    }
  }
  
  @Override
  public void previousQuestion() {
    try {
      super.previousQuestion();
      startTime = System.nanoTime();
    } catch (OutOfQuestionsException ex) {
      System.out.printf("LOG: No more questions\n");
    }
  }
  
  @Override
  public void answerQuestion(Answer answer) {
    long endTime = System.nanoTime();
    this.lastTime = endTime - startTime;
    super.answerQuestion(answer);
    Question q = this.getCurrentQuestion();
    Response r = new Response(answer, lastTime, new Date());
    q.addResponse(r);
    System.out.printf("Time: %s\n", endTime - startTime);
  }
  
}
