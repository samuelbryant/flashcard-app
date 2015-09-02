package ui.questions.quiz;

import java.util.List;
import models.Answer;
import models.Database;
import models.Question;
import models.Response;
import ui.questions.QuestionListController;

public class QuizController extends QuestionListController {

  private long startTime;
  
  public QuizController(Database db, List<Question> questionList) {
    super(db, questionList);
    this._state = QuestionListController.State.NOT_STARTED;
  }
  
  @Override
  public void nextClick() {
    super.nextClick();
    startTime = System.nanoTime();
    System.out.printf("Next click time: %s\n", startTime);
  }
  
  @Override
  public void previousClick() {
    System.out.printf("CANNOT GO BACK\n");
  }
  
  @Override
  public void shuffleClick() {
    System.out.printf("CANNOT SHUFFLE\n");
  }
  
  @Override
  public void answerQuestion(Answer answer) {
    long endTime = System.nanoTime();
    super.answerQuestion(answer);
    Question q = this.getCurrentQuestion();
    Response r = new Response(answer, endTime - startTime);
    q.addResponse(r);
    System.out.printf("Time: %s\n", endTime - startTime);
  }
  
}
