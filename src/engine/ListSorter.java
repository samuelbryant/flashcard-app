package engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import models.AbstractQuestion;
import models.Flashcard;
import models.Question;

/**
 * Class used to sort lists of Questions in-place.
 */
public abstract class ListSorter <T extends AbstractQuestion> {

  /**
   * Sorts given array list in-place.
   * @param list An array list of objects to be sorted.
   */
  public abstract void sort(ArrayList<T> list);

  /**
   *
   * @param <K>
   * @param s1
   * @param s2
   * @return
   */
  public static <K extends AbstractQuestion> ListSorter<K> getCompositeSorter(
      final ListSorter<K> s1, final ListSorter<K> s2) {
    return new ListSorter<K>() {
      @Override
      public void sort(ArrayList<K> list) {
        s2.sort(list);
        s1.sort(list);
      }
    };
  }

  /**
   * ListSorter class based on Comparator object.
   */
  public static class CompareSorter<K extends AbstractQuestion> extends ListSorter<K> {

    protected final Comparator<K> compare;

    public CompareSorter(Comparator<K> compare) {
      this.compare = compare;
    }

    @Override
    public void sort(ArrayList<K> list) {
      Collections.sort(list, compare);
    }
  }

  /**
   * ListSorter instance which sorts question IDs from smallest to largest.
   * @param <K> Type of Question for this IdSorter.
   */
  public static final class IdSorter<K extends AbstractQuestion> extends CompareSorter<K> {
    public IdSorter() {
      super(new Comparator<K>() {
        @Override
        public int compare(K o1, K o2) {
          return o1.getId() - o2.getId();
        }
      });
    }
  }

  /**
   * ListSorter instance which sorts questions from least recently answered to most recently answered.
   * @param <K>
   */
  public static final class LastAnsweredSorter<K extends AbstractQuestion> extends CompareSorter<K> {
    public LastAnsweredSorter() {
      super(new Comparator<K>(){
        @Override
        public int compare(K o1, K o2) {
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
    }
  }

  /**
   * ListSorter instance which sorts questions based on fraction of times wrong.
   */
  public static final ListSorter<Question> WRONG_PERCENTAGE_SORTER = new CompareSorter(new Comparator<Question>() {
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
  public static final class RandomSorter<K extends AbstractQuestion> extends ListSorter<K> {
    @Override
    public void sort(ArrayList<K> list) {
      Collections.shuffle(list);
    }
  };

  /**
   * ListSorter instance which does not sort or change the order of questions.
   */
  public static final class NullSorter<K extends AbstractQuestion> extends ListSorter<K> {
    @Override
    public void sort(ArrayList<K> list) {}
  };

  static void printList(ArrayList<Integer> list) {
    for (Integer i: list) {
      System.out.println(i);
    }
  }

  
  public static final Map<String, ListSorter<Question>> getAllGRESorters() {
    Map<String, ListSorter<Question>> sorters = new HashMap<>();
    sorters.put("By ids", new IdSorter<Question>());
    sorters.put("Last answered", new LastAnsweredSorter<Question>());
    sorters.put("Random", new RandomSorter<Question>());
    sorters.put("None", new NullSorter<Question>());
    sorters.put("Wrong %", WRONG_PERCENTAGE_SORTER);
    sorters.put("Quiz 1", new engine.ListQuiz1());
    return sorters;
  }
  
  public static final Map<String, ListSorter<Flashcard>> getAllFLCSorters() {
    Map<String, ListSorter<Flashcard>> sorters = new HashMap<>();
    sorters.put("By ids", new IdSorter<Flashcard>());
    sorters.put("Last answered", new LastAnsweredSorter<Flashcard>());
    sorters.put("Random", new RandomSorter<Flashcard>());
    sorters.put("None", new NullSorter<Flashcard>());
    return sorters;
  }
  

  /**
   *
   */
  public static final String DEFAULT_1_STRING = "By ids";

  /**
   *
   */
  public static final String DEFAULT_2_STRING = "None";

}
