package models;

import core.Constants;
import core.IO;
import java.io.PrintWriter;
import org.json.JSONObject;

public abstract class DatabaseIO <K extends AbstractQuestion> {
  
  public static DatabaseIO getDatabaseIO(QType t) {
    if (t == QType.FLASHCARD) {
      return getFlashcardDatabaseIO();
    } else {
      return getQuestionDatabaseIO();
    }
  }
  
  public static final DatabaseIO<Flashcard> getFlashcardDatabaseIO() {
    return DatabaseIO.FLCDBIO.SINGLETON;
  }
  
  public static final DatabaseIO<Question> getQuestionDatabaseIO() {
    return DatabaseIO.GREDBIO.SINGLETON;
  }
  
  protected Database<K> database;
  
  private DatabaseIO() {
    this.database = null; 
  }
  
  public Database<K> get() {
    return this.getDatabase();
  }
  
  public void save() {
    this.saveDatabase();
  }
  
  protected abstract QuestionIO<K> getQuestionIO();
  
  protected abstract QType getType();
  
  private Database<K> getDatabase() {
    Database<K> db = this.database;
    if (db == null) {
      this.loadDatabase();
    }
    return this.database;
  }
  
  private void loadDatabase() {
    if (database != null) {
      throw new IllegalStateException(this.getType() + " database has already been loaded");
    } else {
      String databaseFilename = Constants.getDatabaseFile(this.getType());
      Database<K> db;
      if (IO.fileExists(databaseFilename)) {
        String jsonStr = IO.readEntireFileOrDie(databaseFilename);
        db = this.parseDatabaseJSON(jsonStr);
        db.isPersistent = true;
      } else {  
        db = Database.<K>getFreshDatabase();
        db.isPersistent = false;
      } 
      db.validate();
      this.database = db;
    }
  }
  
  private Database<K> parseDatabaseJSON(String jsonStr) {
    JSONObject obj = new JSONObject(jsonStr);
    Database<K> db = new Database<>();
    db.questionNumber = obj.getInt("questionNumber");
    db.revisionNumber = obj.getInt("revisionNumber");
    db.nextQuestionId = obj.getInt("nextQuestionId");
    for (Object idObj: obj.getJSONArray("questions")) {
      Integer id = (Integer) idObj;
      K question = this.getQuestionIO().get(id);
      db.questions.put(id, question);
    }
    return db;
  }
  
  private void saveDatabase() {
    if (database == null) {
      throw new IllegalStateException(this.getType() + " database has not yet been loaded");
    } else {
      try {
        Constants.backupDatabaseOrDie();
      } catch(Exception ex) {
        System.err.printf("ERROR: %s Database Backup Failed!\n", this.getType().toString());
      } 
      database.validate();
      database.revisionNumber++;
      this.writeDatabaseFile();
      this.writeQuestionFiles();
      database.isPersistent = true;
    }
  }

  //***** DATABASE WRITING METHODS *****/
  private void writeQuestionFiles() {
    for (K q: database.questions.values()) {
      this.getQuestionIO().save(q);
    }
  }

  private void writeDatabaseFile() {
    String dataFilename = Constants.getDatabaseFile(this.getType());
    IO.backupAndRecreateOrDie(dataFilename);
    
    PrintWriter pw = IO.getPrintWriterOrDie(dataFilename);
    String jsonDatabase = this.databaseToJSON(database);
    pw.write(jsonDatabase);
    IO.closeOrLive(pw);
  }

  private String databaseToJSON(Database db) {
    JSONObject obj = new JSONObject();
    obj.put("questionNumber", db.questionNumber);
    obj.put("revisionNumber", db.revisionNumber);
    obj.put("nextQuestionId", db.nextQuestionId);
    obj.put("questions", db.questions.keySet());
    return obj.toString(2);
  }
  
  public static class FLCDBIO extends DatabaseIO<Flashcard> {
    private static final FLCDBIO SINGLETON = new FLCDBIO();
    
    @Override
    protected QuestionIO<Flashcard> getQuestionIO() {
      return QuestionIO.getFlashcardIO();
    }
    @Override
    protected QType getType() {
      return QType.FLASHCARD;
    }
  }
  public static class GREDBIO extends DatabaseIO<Question> {
    private static final GREDBIO SINGLETON = new GREDBIO();
    
    @Override
    protected QuestionIO<Question> getQuestionIO() {
      return QuestionIO.getGREIO();
    }
    
    @Override
    protected QType getType() {
      return QType.GRE;
    }
  }
}
