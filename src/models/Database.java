package models;

import core.FatalError;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import engine.QuestionFilter;

public final class Database {

  static final int _ID_START = 1001;

  // Package private fields.
  Integer revisionNumber = 0;
  Integer questionNumber = 0;
  Integer nextQuestionId = 0;
  Map<Integer, Question> questions = new HashMap<>();

  // Private fields.
  boolean isPersistent = false;

  Database() {}

  static Database getFreshDatabase() {
    Database db = new Database();
    db.revisionNumber = 0;
    db.questionNumber = 0;
    db.nextQuestionId = _ID_START;
    db.questions = new HashMap<>();
    db.isPersistent = false;
    return db;
  }

  public int getNumberOfQuestions() {
    return this.questions.values().size();
  }

  public boolean containsQuestion(Source source, Integer questionNumber) {
    for (Question q: this.questions.values()) {
      if (q.source == source && q.questionNumber == questionNumber) {
        return true;
      }
    }
    return false;
  }

  public Question findQuestion(Source source, Integer questionNumber) {
    for (Question q: this.questions.values()) {
      if (q.source == source && q.questionNumber == questionNumber) {
        return q;
      }
    }
    return null;
  }

  public List<Question> getQuestionList() {
    List<Question> questionsList = new ArrayList<>();
    Iterator<Question> iter = this.getDatabaseIterator();
    while (iter.hasNext()) {
      questionsList.add(iter.next());
    }
    return questionsList;
  }

  public List<Question> getQuestionList(QuestionFilter filter) {
    List<Question> questionsList = new ArrayList<>();
    Iterator<Question> iter = this.getDatabaseIterator();
    while (iter.hasNext()) {
      Question q = iter.next();
      if (filter.accept(q)) {
        questionsList.add(q);
      }
    }
    return questionsList;
  }

  public Iterator<Question> getDatabaseIterator() {
    return this.questions.values().iterator();
  }

  public Question findQuestionCopy(Source source, Integer questionNumber) {
    for (Question q: this.questions.values()) {
      if (q.source == source && q.questionNumber == questionNumber) {
        return core.Utilities.makeDeepCopy(q);
      }
    }
    return null;
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

  public void addQuestionsToSession(List<Question> questions) {
    for (Question q: questions) {
      this.addQuestionToSession(q);
    }
  }

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

  public boolean isValid() {
    System.out.printf("LOG: database isValid NIY\n");
    for (Question q: this.questions.values()) {
      if (!q.isValid()) {
        return false;
      }
    }
    return true;
  }

  public void validate() {
    if (!this.isValid()) {
      throw new FatalError("Database failed validation: " + this);
    }
  }

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
