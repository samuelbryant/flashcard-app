package engine;

import models.Question;
import java.util.ArrayList;
import java.util.List;

public abstract class QuestionFilter {

  public abstract boolean accept(Question q);

  public ArrayList<Question> filterList(List<Question> questions) {
    ArrayList<Question> newList = new ArrayList<Question>();
    for (Question q: questions) {
      if (this.accept(q)) {
        newList.add(q);
      }
    }
    return newList;
  }

}
