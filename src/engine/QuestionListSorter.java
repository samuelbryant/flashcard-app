package engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import models.Question;

public abstract class QuestionListSorter {
  
  protected ArrayList<Question> originalList;
  protected ArrayList<Question> workingList;
  public QuestionListSorter(ArrayList<Question> baseList) {
    Collections.copy(workingList, baseList);
    this.originalList = baseList;
  }
  
  public QuestionListSorter() {
    this.originalList = null;
  }
  
  public ArrayList<Question> getBaseList() {
    return this.originalList;
  }
  
  public void setBaseList(ArrayList<Question> baseList) {
    this.workingList = new ArrayList<>(baseList);
    this.originalList = baseList;
  }
  
  public abstract ArrayList<Question> getSortedArrayList();
  
  public static final QuestionListSorter DEFAULT_SORTER = new QuestionListSorter() {

    @Override
    public ArrayList<Question> getSortedArrayList() {
      return this.workingList;
    }
    
  };
  
  public static final QuestionListSorter RANDOM_SORTER = new QuestionListSorter() {

    @Override
    public ArrayList<Question> getSortedArrayList() {
      Collections.shuffle(workingList);
      return workingList;
    }
    
  };
  
  public static final Map<String, QuestionListSorter> ALL_SORTERS = new TreeMap<>();
  static {
    ALL_SORTERS.put("Default", DEFAULT_SORTER);
    ALL_SORTERS.put("Random", RANDOM_SORTER);
  }
  
}
