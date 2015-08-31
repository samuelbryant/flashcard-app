/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashcard.database;

import flashcard.Constants;
import flashcard.question.Question;
import core.IO;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.TreeMap;
import core.FatalError;
import core.Utilities;

/**
 *
 * @author author
 */
public class DatabaseIO {
  
  public static Database loadDatabase() {
    Database db = new Database();
    _readDatabaseFromFiles(db);
    db.checkIntegrity();
    return db;
  }
  
  public static void saveDatabase(Database db) {
    db.checkIntegrity();
    _writeDatabaseToFiles(db);
  }
  
  private static void _readDatabaseFromFiles(Database db) {
    BufferedReader dbReader = IO.getBufferedReaderOrLive(
          Constants.QUESTION_DATABASE_MAIN_FILE);
    
    db.questionNumber = 0;
    db.nextQuestionId = Database._ID_START;
    db.questions = new TreeMap<>();
    
    if (dbReader == null) {
      System.out.println("LOG: no database file found");
    } else {
      try{
        DatabaseIO._readDatabase(db, dbReader);
        IO.closeOrLive(dbReader);
      } catch(IOException ex) {
        throw new FatalError("Could not read question database", ex);
      }
    } 
  }
  
  static void _writeDatabaseToFiles(Database db) {
    IO.backupAndRecreateOrDie(Constants.QUESTION_DATABASE_MAIN_FILE);
    
    PrintWriter dbWriter = IO.getPrintWriterOrDie(
        Constants.QUESTION_DATABASE_MAIN_FILE);
    
    try {
      DatabaseIO._writeDatabase(db, dbWriter);
    } catch(IOException ex) {
      throw new RuntimeException("Could not write database", ex);
    } finally {
      IO.closeOrLive(dbWriter);
    }
  }
  
  private static void _writeDatabase(Database db, PrintWriter dbWriter)
      throws IOException {
    dbWriter.printf("total:%d\n", db.questionNumber);
    dbWriter.printf("nextid:%d\n", db.nextQuestionId);
    dbWriter.printf("ids:%s\n", core.Utilities.getCollectionString(db.questions.keySet()));
    for (int i = Database._ID_START; i < db.nextQuestionId; i++) {
      Question q = db.questions.get(i);
      if (q != null) {
        _writeQuestion(q);
        dbWriter.printf("%d,", i);
      }
    }
  }
  
  static void _readDatabase(Database db, BufferedReader dbReader) 
      throws IOException {
    db.questionNumber = -1;
    db.nextQuestionId = -1;
    db.questions = new TreeMap<>();
    
    // Read database metadata from database file.
    String line;
    while ((line = dbReader.readLine()) != null) {
      String[] parts = line.split(":");
      if (parts.length != 2) {
        throw new IllegalStateException("Invalid DB line:'" + line + "'");
      }
      if (parts[0].compareTo("total") == 0) {
        db.questionNumber = Integer.parseInt(parts[1]);
      } else if (parts[0].compareTo("nextid") == 0) {
        db.nextQuestionId = Integer.parseInt(parts[1]);
      } else if (parts[0].compareTo("ids") == 0) {
        String ids[] = parts[1].split(",");
        for (String str: ids) {
          Integer id = Integer.parseInt(str);
          db.questions.put(id, new Question(id));
        }
      }
    }
    if (db.questionNumber == -1 || db.nextQuestionId == -1) {
      throw new IllegalStateException("Database was missing required keys");
    }
  }
  
  static void _writeQuestion(Question q) throws IOException {
    // For objects not in database yet, we need to copy their images to proper location.
    if (q.databaseImageFilename == null || !IO.fileExists(q.databaseImageFilename)) {
      String originalImageFilename = q.originalImageFilename;
      String databaseImageFilename = flashcard.Constants.getQuestionImageFilename(q);
      
      IO.existsOrDie(originalImageFilename);
      IO.copyOrDie(originalImageFilename, databaseImageFilename);
      
      q.databaseImageFilename = databaseImageFilename;
    }
    
    String dataFilename = Constants.getQuestionDataFilename(q);
    
    IO.backupAndRecreateOrDie(dataFilename);
    PrintWriter questionWriter = IO.getPrintWriterOrDie(dataFilename);
    
    _writeQuestionFile(q, questionWriter);
    
    IO.closeOrLive(questionWriter);
  }
  
  private static void _writeQuestionFile(Question q, PrintWriter pw) {  
    pw.printf("id:%d\n", q.id);
    pw.printf("source:%s\n", q.source.toString());
    pw.printf("questionNumber:%d\n", q.questionNumber);
    pw.printf("answer:%s\n", q.answer.toString());
    pw.printf("databaseImageFilename:%s\n", q.databaseImageFilename);
    pw.printf("subjects:%s\n", Utilities.getArrayString(q.subjects));
    pw.printf("tags:%s\n", Utilities.getArrayString(q.tags));
    pw.printf("notes:%s\n", Utilities.getArrayString(q.notes));
  }
  
}
