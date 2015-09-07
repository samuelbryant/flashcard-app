package engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import models.Answer;
import models.DatabaseIO;
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
  
  public static ListSorter getCompositeSorter(final ListSorter s1, final ListSorter s2) {
    return new ListSorter() {
      @Override
      public void sort(ArrayList<Question> list) {
        s2.sort(list);
        s1.sort(list);
      }
    };
  }
  
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
      if (d1 == null && d2 == null) {
        return 0;
      } else if (d1 == null) {
        return -1;
      } else if (d2 == null) {
        return +1;
      } else {
        return d1.compareTo(d2);
      }
    }
  });
  
  /**
   * ListSorter instance which sorts questions based on fraction of times wrong.
   */
  public static final ListSorter WRONG_PERCENTAGE_SORTER = new CompareSorter(new Comparator<Question>() {
    @Override
    public int compare(Question o1, Question o2) {
      ArrayList<Boolean> responses1 = o1.getGradedResponses();
      ArrayList<Boolean> responses2 = o2.getGradedResponses();
      int right1 = 0;
      int right2 = 0;
      
      for (Boolean b : responses1) {
        if (b) right1++;
      }
      
      for (Boolean b : responses2) {
        if (b) right2++;
      }
      
      if (responses2.isEmpty()) {
        if (right1 == responses1.size()) {
          return -1;
        } else {
          return +1;
        }
      }
      
      if (responses1.isEmpty()) {
        if (right2 == responses2.size()) {
          return +1;
        } else {
          return -1;
        }
      }
      
      return (int) ((((double) right2) / responses2.size()) - (((double) right1) / responses1.size()));
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
  
  public static final Map<String, ListSorter> ALL_SORTERS = new HashMap<>();
  static {
    ALL_SORTERS.put("By ids", ListSorter.ID_SORTER);
    ALL_SORTERS.put("Last answered", ListSorter.LAST_ANSWERED);
    ALL_SORTERS.put("Random", RANDOM_SORTER);
    ALL_SORTERS.put("None", NULL_SORTER);
    ALL_SORTERS.put("Wrong %", WRONG_PERCENTAGE_SORTER);
  }
  public static final String DEFAULT_1_STRING = "By ids";
  public static final String DEFAULT_2_STRING = "None";
  
  public static void main(String[] args) {
    ListSorter s1 = ListSorter.LAST_ANSWERED;
    ListSorter s2 = ListSorter.NULL_SORTER;
    ListSorter s = ListSorter.getCompositeSorter(s1, s2);
    
    ArrayList<Question> list = DatabaseIO.getDatabase().getQuestions(ListFilter.NULL_FILTER, s);
    for (Question q: list) {
      System.out.println(q);
    }
  }
}
