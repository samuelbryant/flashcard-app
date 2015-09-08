package models;

import core.FatalError;
import engine.ListFilter;
import engine.ListSorter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 *
 * @author sambryant
 */
public final class Database {

  static final int _ID_START = 1001;

  // Package private fields.
  Integer revisionNumber = 0;
  Integer questionNumber = 0;
  Integer nextQuestionId = 0;
  Map<Integer, Question> questions = new TreeMap<>();

  // Private fields.
  boolean isPersistent = false;

  Database() {}

  static Database getFreshDatabase() {
    Database db = new Database();
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

  /**
   *
   * @param source
   * @param questionNumber
   * @return
   */
  public boolean containsQuestion(Source source, Integer questionNumber) {
    for (Question q: this.questions.values()) {
      if (q.source == source && Objects.equals(q.questionNumber, questionNumber)) {
        return true;
      }
    }
    return false;
  }

  /**
   *
   * @param source
   * @param questionNumber
   * @return
   */
  public Question findQuestion(Source source, Integer questionNumber) {
    for (Question q: this.questions.values()) {
      if (q.source == source && Objects.equals(q.questionNumber, questionNumber)) {
        return q;
      }
    }
    return null;
  }

  /**
   * Gets Question array from Database.
   * @return ArrayList with all Question instances, sorted by ID.
   */
  public ArrayList<Question> getQuestions() {
    return getQuestions(ListFilter.NULL_FILTER, ListSorter.ID_SORTER);
  }

  /**
   * Gets Question array from Database filtered by ListFilter.
   * @param filter ListFilter to use to filter Question array.
   * @return ArrayList with all Question instances that match ListFilter, sorted by ID.
   */
  public ArrayList<Question> getQuestions(ListFilter filter) {
    return getQuestions(filter, ListSorter.ID_SORTER);
  }

  /**
   * Gets Question array from Database sorted by ListSorter.
   * @param sorter ListSorter to use to sort Question array.
   * @return ArrayList with all Question instances, sorted by ListSorter.
   */
  public ArrayList<Question> getQuestions(ListSorter sorter) {
    return getQuestions(ListFilter.NULL_FILTER, sorter);
  }



  /**
   * Gets Question array from Database filtered by ListFilter and sorted by ListSorter.
   * @param filter ListFilter to use to filter Question array.
   * @param sorter ListSorter to use to sort Question array.
   * @return ArrayList with all Question instances that match ListFilter, sorted by ListSorter.
   */
  public ArrayList<Question> getQuestions(ListFilter filter, ListSorter sorter) {
    ArrayList<Question> list = new ArrayList<>();
    Iterator<Question> iter = this.getDatabaseIterator();
    while (iter.hasNext()) {
      Question q = iter.next();
      if (filter.accept(q)) {
        list.add(q);
      }
    }
    sorter.sort(list);
    return list;
  }


  private Iterator<Question> getDatabaseIterator() {
    return this.questions.values().iterator();
  }

  /**
   *
   * @param q
   */
  public void addQuestionToSession(Question q) {
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
      if (this.containsQuestion(q.source, q.questionNumber)) {
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
    for (Question q: this.questions.values()) {
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

  /**
   *
   * @param args
   */
  public static void main(String[] args) {
    Database d = DatabaseIO.loadDatabase();
    System.out.println("DATABASE");
    System.out.printf("Total #  : %d\n", d.questionNumber);
    System.out.printf("Integrity: %s\n", d.isValid());
    System.out.printf("**** Questions ****\n");
    for (Question q: d.questions.values()) {
      System.out.println(q);
    }
  }

}
