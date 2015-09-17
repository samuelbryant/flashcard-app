package engine;

import java.util.ArrayList;
import java.util.LinkedList;
import models.AbstractQuestion;

/**
 * Class which produces a "quiz" by generating random list of questions based on weights.
 * Warning:
 * This "high-jacks" normal ListSorter behavior by changing the length of the input array.
 * @param <Q_TYPE>
 */
public abstract class ListQuizSorter<Q_TYPE extends AbstractQuestion> extends ListSorter<Q_TYPE> {

  ListQuizSorter() {}

  /**
   *
   * @param q
   * @return
   */
  protected abstract int getQuestionWeight(Q_TYPE q);

  /**
   * Changes input array to contain a list of Q_TYPEs randomly selected by weight.
   * Warning: This does not "sort" at all. In fact, it changes the length of the input array.
   * @param list 
   */
  @Override
  public void sort(ArrayList<Q_TYPE> list) {
    LinkedList<Q_TYPE> questionPool = new LinkedList<>();

    // Add each Q_TYPE to pool the number of times equal to its weight.
    for (Q_TYPE q: list) {
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
      Q_TYPE choice = questionPool.remove(choiceIndex);
      poolSize--;

      list.add(choice);
    }
  }

}
