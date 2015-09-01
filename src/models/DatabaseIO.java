package models;

import core.Constants;
import core.IO;
import java.io.PrintWriter;
import org.json.JSONObject;

/**
 *
 * @author author
 */
public class DatabaseIO {

  public static Database loadDatabase() {
    Database db = _readDatabaseFromFiles();
    db.validate();
    return db;
  }

  public static void writeDatabase(Database db) {
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
