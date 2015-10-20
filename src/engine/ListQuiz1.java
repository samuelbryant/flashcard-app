package engine;

import java.util.ArrayList;
import models.Question;
import models.Response;
import models.Tag;

/**
 *
 * @author sambryant
 */
public class ListQuiz1 extends ListQuizSorter<Question> {

  /**
   * Weighting scheme
   * Base value: 5
   * has never answered = +8
   * has answered, only correctly = -4
   * has answered, sometimes correctly = +2
   * has answered, only wrong = +4
   * Tag weights (Only gets maximum value)
   * tagged hard = +4
   * tagged time = +5
   * other tags = +3
   * @param q
   * @return 
   */
  @Override
  protected int getQuestionWeight(Question q) {
    int weight = this.getTagWeight(q) + this.getAnswerWeight(q);
    return weight;
  }

  /**
   *
   * @param q
   * @return
   */
  public int getTagWeight(Question q) {
    ArrayList<Tag> tags = q.getTags();
    if (tags.isEmpty()) {
      return 0;
    } else if (tags.contains(Tag.TIME) || tags.contains(Tag.WRONG)) {
      return +5;
    } else if (tags.contains(Tag.HARD)) {
      return +4;
    } else {
      return +3;
    }
  }

  /**
   *
   * @param q
   * @return
   */
  public int getAnswerWeight(Question q) {
    ArrayList<Response> responses = q.getResponses();
    if (responses.isEmpty()) {
      return +8;
    }
    int timesRightWrong[] = q.getTimesRightWrong();
    int timesRight = timesRightWrong[0];
    int timesWrong = timesRightWrong[1];
    if (timesWrong == 0) {
      return -4;
    } else if (timesRight == 0) {
      return +4;
    } else {
      return +2;
    }
  }

  @Override
  protected void preprocess(ArrayList<Question> list) {
  }



}
