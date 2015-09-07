package models;

import core.Constants;
import core.IO;
import java.io.PrintWriter;
import org.json.JSONObject;

public class DatabaseIO {
  
  protected static Database currentDatabase = null;

  public static Database loadDatabase() {
    if (currentDatabase != null) {
      throw new IllegalStateException("Database has already been loaded");
    }
    Database db = _readDatabaseFromFiles();
    db.validate();
    currentDatabase = db;
    return db;
  }
  
  public static Database getDatabase() {
    if (currentDatabase == null) {
      loadDatabase();
    }
    return currentDatabase;
  }

  public static void saveDatabase() {
    
    if (currentDatabase == null) {
      throw new IllegalStateException("No database has been loaded");
    }
    try {
      _backupDatabase();
    } catch(Exception ex) {
      System.err.printf("ERROR: Database Backup Failed!\n");
    }
    _writeDatabase(currentDatabase);
    
  }
  
  private static void _backupDatabase() {
    String backupDirname = Constants.getDatabaseBackupDirName();

    IO.createDirOrDie(backupDirname);
    String src1 = Constants.QUESTION_DATABASE_MAIN_FILE;
    String dst1 = Constants.getDatabaseBackupMainFileName(backupDirname);
    String src2 = Constants.QUESTION_DATA_DIR;
    String dst2 = Constants.getDatabaseBackupDataDirName(backupDirname);
    
    IO.copyOrDie(src1, dst1);
    IO.copyOrDie(src2, dst2);
  }
  
  private static void _backupToFolder(String srcDirname, String srcFilename, String dstDirname) {
    String src = srcDirname + "/" + srcFilename;
    String dst = dstDirname + "/" + srcFilename;
    IO.copyOrDie(src, dst);
  }
  
  @Deprecated
  public static void writeDatabase(Database db) {
    db.validate();
    db.revisionNumber++;
    _writeDatabaseFile(db);
    _writeQuestionFiles(db);
    db.isPersistent = true;
  }
  
  private static void _writeDatabase(Database db) {
    db.validate();
    db.revisionNumber++;
    _writeDatabaseFile(db);
    _writeQuestionFiles(db);
    db.isPersistent = true;
  }

  //***** DATABASE WRITING METHODS *****/

  private static void _writeQuestionFiles(Database db) {
    for (Question q: db.questions.values()) {
      QuestionIO.writeQuestion(q);
    }
  }

  private static void _writeDatabaseFile(Database db) {
    String dataFilename = Constants.QUESTION_DATABASE_MAIN_FILE;
    IO.backupAndRecreateOrDie(dataFilename);
    PrintWriter pw = IO.getPrintWriterOrDie(dataFilename);

    String jsonDatabase = _databaseToJSON(db);
    pw.write(jsonDatabase);
    IO.closeOrLive(pw);
  }

  private static String _databaseToJSON(Database db) {
    JSONObject obj = new JSONObject();
    obj.put("questionNumber", db.questionNumber);
    obj.put("revisionNumber", db.revisionNumber);
    obj.put("nextQuestionId", db.nextQuestionId);
    obj.put("questions", db.questions.keySet());
    return obj.toString(2);
  }

  //***** DATABASE READING METHODS *****/

  private static Database _readDatabaseFromFiles() {
    String databaseFilename = Constants.QUESTION_DATABASE_MAIN_FILE;
    if (IO.fileExists(databaseFilename)) {
      String jsonStr = IO.readEntireFileOrDie(databaseFilename);
      Database db = _parseDatabaseJSON(jsonStr);
      db.isPersistent = true;
      return db;
    } else {
      Database db = Database.getFreshDatabase();
      db.isPersistent = false;
      return db;
    }
  }
  private static Database _parseDatabaseJSON(String jsonStr) {
    JSONObject obj = new JSONObject(jsonStr);
    Database db = new Database();
    db.questionNumber = obj.getInt("questionNumber");
    db.revisionNumber = obj.getInt("revisionNumber");
    db.nextQuestionId = obj.getInt("nextQuestionId");
    for (Object idObj: obj.getJSONArray("questions")) {
      Integer id = (Integer) idObj;
      Question q = QuestionIO.loadQuestion(id);
      db.questions.put(id, q);
    }
    return db;
  }
}
