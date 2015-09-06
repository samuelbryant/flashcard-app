package engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import models.Answer;
import models.Question;
import models.Response;
import models.Source;

/**
 * Class used to sort lists of Questions in-place.
 */
public abstract class ListSorter {

  /**
   * Sorts given array list in-place.
   * @param list An array list of objects to be sorted.
   */
  public abstract void sort(ArrayList<Question> list);
  
  /**
   * ListSorter class based on Comparator object.
   */
  public static class CompareSorter extends ListSorter {

    protected final Comparator<Question> compare;
    
    public CompareSorter(Comparator<Question> compare) {
      this.compare = compare;
    }
    
    @Override
    public void sort(ArrayList<Question> list) {
      Collections.sort(list, compare);
    }
  }
  
  /**
   * ListSorter instance which sorts question IDs from smallest to largest.
   */
  public static final ListSorter ID_SORTER = new CompareSorter(new Comparator<Question>() {
    @Override
    public int compare(Question o1, Question o2) {
      return o1.getId() - o2.getId();
    }
  });
  
  /**
   * ListSorter instance which sorts questions from least recently answered to most recently answered.
   */
  public static final ListSorter LAST_ANSWERED = new CompareSorter(new Comparator<Question>() {
    @Override
    public int compare(Question o1, Question o2) {
      Date d1 = o1.getLastResponseTime();
      Date d2 = o2.getLastResponseTime();
      if (d1 == null) {
        return +1;
      } else if (d2 == null) {
        return -1;
      } else {
        return d1.compareTo(d2);
      }
    }
  });
  
  /**
   * ListSorter instance which sorts questions randomly.
   */
  public static final ListSorter RANDOM_SORTER = new ListSorter() {
    @Override
    public void sort(ArrayList<Question> list) {
      Collections.shuffle(list);
    }
  };
  
  /**
   * ListSorter instance which does not sort or change the order of questions.
   */
  public static final ListSorter NULL_SORTER = new ListSorter() {
    @Override
    public void sort(ArrayList<Question> list) {}
  };
  
  static void printList(ArrayList<Integer> list) {
    for (Integer i: list) {
      System.out.println(i);
    }
  }
  
  public static void main(String[] args) {
    Date before = new Date(2012, 1, 1);
    Date after = new Date(2015, 12, 25);
    
    Question q1 = new Question(Source.CUSTOM, 1, Answer.A, "somewhere");
    Question q2 = new Question(Source.CUSTOM, 2, Answer.A, "somewhere");
    q1.addResponse(new Response(null, null, before));
    q2.addResponse(new Response(null, null, after));
    
    ArrayList<Question> list = new ArrayList<>();
    list.add(q1);
    list.add(q2);
    
    for (Question q: list) {
      System.out.println(q);
    }
    
    LAST_ANSWERED.sort(list);
    
    for (Question q: list) {
      System.out.println(q);
    }
  }
  
}
