/*
 * Database - Contains database of questions and methods for reading/writing.
 */
package flashcard.database;

import flashcard.question.Question;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author author
 */
public final class Database {
  
  static final int _ID_START = 1001;
  
  // Package private fields.
  int questionNumber;
  int nextQuestionId;
  Map<Integer, Question> questions;
  
  // Private fields.
  private boolean hasPendingChanges = false;
  
  Database() {}
  
  public int getNumberOfQuestions() {
    return this.questions.values().size(); 
  }
  
  public List<Question> getQuestionListCopy() {
    List<Question> questionsCopy = new ArrayList<>();
    Iterator<Question> iter = this.getDatabaseCopyIterator();
    while (iter.hasNext()) {
      questionsCopy.add(iter.next());
    }
    return questionsCopy;
  }
  
  public Iterator<Question> getDatabaseCopyIterator() {
    final Iterator<Question> baseIterator = this.questions.values().iterator(); 
    return new Iterator<Question>() {
      @Override
      public boolean hasNext() {return baseIterator.hasNext();}
      @Override
      public Question next() {
        return core.Utilities.makeDeepCopy(baseIterator.next());
      }
      @Override
      public void remove() {throw new UnsupportedOperationException("Not supported yet.");}
    };
  }
  
  public void addQuestionToSession(Question q) {
    // For new questions (no id), we assign the id and add it to database map.
    if (!q.hasId()) {
      int id = this.nextQuestionId;
      q.setId(id);
      if (this.questions.get(id) != null) {
        throw new IllegalStateException("Question map already has id: " + id);
      }
      this.questions.put(id, q);
      this.hasPendingChanges = true;
      this.nextQuestionId++;
      this.questionNumber++;
    }
    // For old questions (has id), we ensure question is already in database, then set it to map.
    else {
      int id = q.getId();
      if (this.questions.get(id) == null) {
        throw new IllegalStateException("Question w/ id " + id + " does not correspond to entry");
      }
      this.questions.put(id, q);
      this.hasPendingChanges = true;
    }
  }
  
  public boolean checkIntegrity() {
    System.out.printf("LOG: _checkIntegrity NIY\n");
    return true;
  }
  
  public static void main(String[] args) {
    Database d = DatabaseIO.loadDatabase();
    System.out.println("DATABASE");
    System.out.printf("Total #  : %d\n", d.questionNumber);
    System.out.printf("Integrity: %s\n", d.checkIntegrity());
    System.out.printf("**** Questions ****\n");
    for (Question q: d.questions.values()) {
      System.out.println(q);
    }
  }

}
