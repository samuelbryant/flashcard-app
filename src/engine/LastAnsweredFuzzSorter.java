/*
 * File Overview: TODO
 */
package engine;

import java.util.ArrayList;
import models.AbstractQuestion;

/**
 * @param <K>
 */
public class LastAnsweredFuzzSorter<K extends AbstractQuestion> extends ListSorter.LastAnsweredSorter<K> {
  
  private static final double SWAP_CHANCE = 1.0;
  private static final double MAX_SWAP_DISTANCE_FRACTION = 0.1;
  
  @Override
  public void sort(ArrayList<K> list) {
    // First sort by last answered date.
    super.sort(list);  
    // Then we "fuzz" the sorting ordering based on a parameter.
    fuzzList(list);
  }
  
  private static <T> void fuzzList(ArrayList<T> l) {
    int max_swap_distance = (int) (l.size() * MAX_SWAP_DISTANCE_FRACTION);
    
    int totalSwap = 0;
    
    for (int i=0; i < l.size() - 1; i++) {
      double rand = Math.pow(Math.random(), 2);
      if (rand < SWAP_CHANCE) {
        int swap = (int) ((rand / SWAP_CHANCE) * max_swap_distance);
        totalSwap += swap;
        if (swap > 0) {
          int newPos = Math.min(l.size() - 1, i + swap);
          swap(l, i, newPos);
        }
      }
    }
  }
  
  private static <T> void swap(ArrayList<T> l, int i, int j) {
    T temp = l.get(j);
    l.set(j, l.get(i));
    l.set(i, temp);
  }
  
  public static void main(String[] args) {
    ArrayList<Integer> testList = new ArrayList<>();
    for (int i=0; i<800; i++) {
      testList.add(i);
    }
    
    fuzzList(testList);
    
    int totalSwap = 0;
    for (int i=0; i<800; i++) {
      int value = testList.get(i);
      int swap = -i + testList.get(i);
      System.out.printf("%2d  (%+d)\n", value, swap);
      totalSwap += Math.abs(swap);
    }
    
    System.out.printf("Average final swap: %f\n", ((float) totalSwap) / testList.size());
  }
}