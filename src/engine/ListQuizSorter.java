package engine;

import java.util.ArrayList;
import java.util.LinkedList;
import models.Question;

/**
 * Class which produces a "quiz" by generating random list of questions based on weights.
 * Warning:
 * This "high-jacks" normal ListSorter behavior by changing the length of the input array.
 */
public abstract class ListQuizSorter extends ListSorter {

  ListQuizSorter() {}

  /**
   *
   * @param q
   * @return
   */
  protected abstract int getQuestionWeight(Question q);

  /**
   * Changes input array to contain a list of Questions randomly selected by weight.
   * Warning: This does not "sort" at all. In fact, it changes the length of the input array.
   * @param list 
   */
  @Override
  public void sort(ArrayList<Question> list) {
    LinkedList<Question> questionPool = new LinkedList<>();

    // Add each Question to pool the number of times equal to its weight.
    for (Question q: list) {
      int weight = getQuestionWeight(q);
      for (int i=0; i<weight; i++) {
        questionPool.add(q);
      }
    }

    int poolSize = questionPool.size();
    int totalNumber = poolSize;

    // Clear old list and set capacity.
    list.clear();
    list.ensureCapacity(totalNumber);  

    for (int i = 0; i < totalNumber; i++) {
      int choiceIndex = (int) (Math.random() * poolSize);
      Question choice = questionPool.remove(choiceIndex);
      poolSize--;

      list.add(choice);
    }
  }

}
