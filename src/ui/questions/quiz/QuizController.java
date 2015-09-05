package ui.questions.quiz;

import java.util.Date;
import models.Answer;
import models.Database;
import models.Question;
import models.Response;
import ui.questions.QuestionListController;

public class QuizController extends QuestionListController {

  private long startTime;
  
  public QuizController(Database db) {
    super(db);
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
    System.out.printf("CANNOT GO BACK\n");
  }
  
  @Override
  public void answerQuestion(Answer answer) {
    long endTime = System.nanoTime();
    super.answerQuestion(answer);
    Question q = this.getCurrentQuestion();
    Response r = new Response(answer, endTime - startTime, new Date());
    q.addResponse(r);
    System.out.printf("Time: %s\n", endTime - startTime);
  }
  
}
