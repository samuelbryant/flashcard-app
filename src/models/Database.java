package models;

import core.FatalError;
import engine.ListFilter;
import engine.ListSorter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * @param <T> Type of question this database represents.
 */
public final class Database <T extends AbstractQuestion> {

  static final int _ID_START = 1001;

  // Package private fields.
  protected Integer revisionNumber = 0;
  protected Integer questionNumber = 0;
  protected Integer nextQuestionId = 0;
  protected Map<Integer, T> questions = new TreeMap<>();

  // Private fields.
  boolean isPersistent = false;

  Database() {}

  static <K extends AbstractQuestion> Database<K> getFreshDatabase() {
    Database<K> db = new Database();
    db.revisionNumber = 0;
    db.questionNumber = 0;
    db.nextQuestionId = _ID_START;
    db.questions = new TreeMap<>();
    db.isPersistent = false;
    return db;
  }

  /**
   *
   * @return
   */
  public int getNumberOfQuestions() {
    return this.questions.values().size();
  }
  
  public boolean containsQuestion(T q) {
    return this.questions.containsValue(q);
  }

  /**
   * Gets Question array from Database.
   * @return ArrayList with all Question instances, sorted by ID.
   */
  public ArrayList<T> getQuestions() {
    return getQuestions(new ListFilter.NullFilter<T>(), new ListSorter.IdSorter<T>());
  }

  /**
   * Gets Question array from Database filtered by ListFilter.
   * @param filter ListFilter to use to filter Question array.
   * @return ArrayList with all Question instances that match ListFilter, sorted by ID.
   */
  public ArrayList<T> getQuestions(ListFilter<T> filter) {
    return getQuestions(filter, new ListSorter.IdSorter<T>());
  }

  /**
   * Gets Question array from Database sorted by ListSorter.
   * @param sorter ListSorter to use to sort Question array.
   * @return ArrayList with all Question instances, sorted by ListSorter.
   */
  public ArrayList<T> getQuestions(ListSorter<T> sorter) {
    return getQuestions(new ListFilter.NullFilter<T>(), sorter);
  }



  /**
   * Gets Question array from Database filtered by ListFilter and sorted by ListSorter.
   * @param filter ListFilter to use to filter Question array.
   * @param sorter ListSorter to use to sort Question array.
   * @return ArrayList with all Question instances that match ListFilter, sorted by ListSorter.
   */
  public ArrayList<T> getQuestions(ListFilter<T> filter, ListSorter<T> sorter) {
    ArrayList<T> list = new ArrayList<>();
    Iterator<T> iter = this.getDatabaseIterator();
    while (iter.hasNext()) {
      T q = iter.next();
      if (filter.accept(q)) {
        list.add(q);
      }
    }
    sorter.sort(list);
    return list;
  }


  private Iterator<T> getDatabaseIterator() {
    return this.questions.values().iterator();
  }

  /**
   *
   * @param q
   */
  public void addQuestionToSession(T q) {
    // For old questions (has id), we ensure question is already in database, then set it to map.
    if (q.id != null) {
      if (this.questions.get(q.id) == null) {
        throw new IllegalStateException("Question w/ id " + q.id + " does not correspond to entry");
      }
      this.questions.put(q.id, q);
    }
    // For new questions (no id), we check if a matching question already exists in DB
    else {
      // For non-persistent questions (no id), we assign it the next available ID.
      q.id = this.nextQuestionId;

      // Validate to make sure question has all required fields.
      q.validate();

      // Ensure database doesn't already have this ID (indicates programming error).
      if (this.questions.get(q.id) != null) {
        throw new IllegalStateException("Question map already has id: " + q.id);
      }

      // Ensure database doesn't already have matching question (to avoid duplicates).
      if (this.containsQuestion(q)) {
        // Unset new id so question is unchanged.
        q.id = null;
        System.out.printf("WRN: addQuestionToSession: Ignoring duplicate question: %s\n", q);
        return;
      }

      this.questions.put(q.id, q);
      this.nextQuestionId++;
      this.questionNumber++;
    }
  }

  /**
   *
   * @return
   */
  public boolean isValid() {
    System.out.printf("LOG: database isValid NIY\n");
    for (T q: this.questions.values()) {
      if (!q.isValid()) {
        return false;
      }
    }
    return true;
  }

  /**
   *
   */
  public void validate() {
    if (!this.isValid()) {
      throw new FatalError("Database failed validation: " + this);
    }
  }

//  /**
//   *
//   * @param args
//   */
//  public static void main(String[] args) {
//    Database d = DatabaseIO.loadDatabase();
//    System.out.println("DATABASE");
//    System.out.printf("Total #  : %d\n", d.questionNumber);
//    System.out.printf("Integrity: %s\n", d.isValid());
//    System.out.printf("**** Questions ****\n");
//    for (T q: d.questions.values()) {
//      System.out.println(q);
//    }
//  }

}
