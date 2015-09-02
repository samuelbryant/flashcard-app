package ui.questions.filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import models.Database;
import models.Question;
import models.Source;
import models.Subject;
import models.Tag;
import ui.questions.QuestionListController;

public class FilterController {
  
  public enum SortType {
    LAST_ANSWERED, TIMES_ANSWERED, ID, RANDOM;
  }
  
  public class QuestionComparator implements Comparator<Question> {
    @Override
    public int compare(Question o1, Question o2) {
      int value = 0;
      if (sortType == SortType.ID) {
        value = o1.getId() - o2.getId();
      } else if (sortType == SortType.LAST_ANSWERED) {
        throw new UnsupportedOperationException("Not implemented yet");
      } else if (sortType == SortType.TIMES_ANSWERED) {
        throw new UnsupportedOperationException("Not implemented yet");
      } else {
        throw new UnsupportedOperationException("Not implemented yet");
      }
      if (!sortAscending)
        value *= -1;
      return value;
    }
  }
  
  Map<Subject, Boolean> subjectFilters;
  Map<Tag, Boolean> tagFilters;
  Map<Source, Boolean> sourceFilters;
  SortType sortType;
  Boolean sortAscending;
  
  private QuestionListController questionListController;
  
  public FilterController(QuestionListController ctrl) {
    this.questionListController = ctrl;
    
    this.subjectFilters = new TreeMap<>();
    for (Subject subject: Subject.values()) {
      this.subjectFilters.put(subject, false);
    }
    
    this.tagFilters = new TreeMap<>();
    for (Tag tag: Tag.values()) {
      this.tagFilters.put(tag, false);
    }
    
    this.sourceFilters = new TreeMap<>();
    for (Source source: Source.values()) {
      this.sourceFilters.put(source, false);
    }
    
    this.sortType = SortType.ID;
    
    this.sortAscending = true;
  }
  
  private boolean _hasSubjectFilter() {
    for (Subject subject: Subject.values()) {
      if (this.subjectFilters.get(subject)) {
        return true;
      }
    }
    return false;
  }
  
  private boolean _hasTagFilter() {
    for (Tag tag: Tag.values()) {
      if (this.tagFilters.get(tag)) {
        return true;
      }
    }
    return false;
  }
  
  private boolean _hasSourceFilter() {
    for (Source source: Source.values()) {
      if (this.sourceFilters.get(source)) {
        return true;
      }
    }
    return false;
  }
  
  public ArrayList<Question> getQuestionList(Database db) {
    ArrayList<Question> list = new ArrayList<>();
    
    Iterator<Question> iter = db.getDatabaseIterator();
    
    boolean hasTagFilters = this._hasTagFilter();
    boolean hasSubjectFilters = this._hasSubjectFilter();
    boolean hasSourceFilters = this._hasSourceFilter();
    
    while (iter.hasNext()) {
      Question q = iter.next();
      
      if (hasSourceFilters) {
      if (!this.sourceFilters.get(q.getSource())) {
        continue;
      }
      }
      if (hasSubjectFilters) {
      if (!FilterController._arrayInBoolmap(this.subjectFilters, q.getSubjects())) {
        continue;
      }
      }
      if (hasTagFilters) {
      if (!FilterController._arrayInBoolmap(this.tagFilters, q.getTags())) {
        continue;
      }
      }
      list.add(q);
    }
    Collections.sort(list, new QuestionComparator());
    
    System.out.printf("BUILD List: %d\n", list.size());
    
    return list;
  }
  
  private static <T> boolean _arrayInBoolmap(Map<T, Boolean> map, ArrayList<T> search) {
    for (T t: search) {
      if (map.get(t)) {
        return true;
      }
    }
    return false;
  }
  
  private static <T> boolean _valueInBoolmap(Map<T, Boolean> map, T value) {
    return map.get(value);
  }
  
  void filter() {
    ArrayList<Question> list = this.getQuestionList(
        this.questionListController.getDatabase());
    this.questionListController.setQuestionList(list);
  }

}
