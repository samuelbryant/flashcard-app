/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import core.Constants;
import core.IO;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.*;

/**
 *
 * @author sambryant
 */
public class QuestionIO {
  
  public static Question loadQuestion(int id) {
    Question q = _readQuestionDataFile(id);
    q.persistent = true;
    // SBTAG: Maybe we want to load the image file here as well.
    return q;
  }
  
  public static void writeQuestion(Question q) {
    _writeQuestionImageFile(q);
    _writeQuestionDataFile(q);
    q.persistent = true;
  }
  
  public static void loadQuestionImage(Question q) {
    q.questionImage = IO.loadImageOrDie(q.databaseImageFilename);
  }
  
  //***** QUESTION FILE WRITING METHODS *****/
  
  private static void _writeQuestionImageFile(Question q) {
    // For objects not in database yet, we need to copy their images to proper location.
    if (q.databaseImageFilename == null || !IO.fileExists(q.databaseImageFilename)) {
      String originalImageFilename = q.originalImageFilename;
      String databaseImageFilename = Constants.getQuestionImageFilename(q.id);
      IO.existsOrDie(originalImageFilename);
      IO.copyOrDie(originalImageFilename, databaseImageFilename);
      q.databaseImageFilename = databaseImageFilename;
    }
  }
  
  private static void _writeQuestionDataFile(Question q) {
    String dataFilename = Constants.getQuestionDataFilename(q.id);
    IO.backupAndRecreateOrDie(dataFilename);
    PrintWriter pw = IO.getPrintWriterOrDie(dataFilename);
    
    String jsonQuestion = _questionToJSON(q);
    pw.write(jsonQuestion);
    IO.closeOrLive(pw);
  }
  
  private static String _questionToJSON(Question q) {
    JSONObject obj = new JSONObject(); 
    obj.put("id", q.id);
    obj.putOpt("source", q.source.toString());
    obj.putOpt("questionNumber", q.questionNumber);
    obj.putOpt("databaseImageFilename", q.databaseImageFilename);
    obj.putOpt("answer", q.answer);
    obj.putOpt("notes", q.notes);
    obj.putOpt("subjects", q.subjects);
    obj.putOpt("tags", q.tags);
    obj.putOpt("subjects", q.subjects);
    obj.putOpt("responses", _responsesToJSONArray(q.responses));
    return obj.toString(2);
  }
   
  private static JSONArray _responsesToJSONArray(ArrayList<Response> responses) {
    JSONArray arr = new JSONArray();
    for (Response r: responses) {
      Map<String, Object> map = new HashMap<>();
      map.put("response", r.response);
      map.put("responseTime", r.responseTime);
      arr.put(map);
    }
    return arr;
  }
  
  //***** QUESTION DATA FILE READING METHODS
  
  private static Question _readQuestionDataFile(int id) {
    String dataFilename = Constants.getQuestionDataFilename(id);
    String jsonStr = IO.readEntireFileOrDie(dataFilename);
    return _parseQuestionJSON(jsonStr);
  }
  
  private static Question _parseQuestionJSON(String jsonStr) {
    JSONObject obj = new JSONObject(jsonStr);
    Question q = new Question();
    q.id = obj.getInt("id");
    q.source = Source.valueOf(_getStringOpt(obj, "source"));
    q.questionNumber = _getIntOpt(obj, "questionNumber");
    q.databaseImageFilename = _getStringOpt(obj, "databaseImageFilename");
    q.answer = Answer.valueOf(_getStringOpt(obj, "answer"));
    for (Object ele: _getArrOpt(obj, "notes")) {
      q.notes.add((String) ele);
    }
    for (Object ele: _getArrOpt(obj, "subjects")) {
      q.subjects.add(Subject.valueOf((String) ele));
    }
    for (Object ele: _getArrOpt(obj, "tags")) {
      q.tags.add(Tag.valueOf((String) ele));
    }
    for (Object ele: _getArrOpt(obj, "responses")) {
      q.responses.add(_parseResponseJSON((JSONObject) ele));
    }
    return q;
  }
  
  private static Response _parseResponseJSON(JSONObject obj) {
    return new Response(
        Answer.valueOf(obj.getString("response")),
        obj.getInt("responseTime"));
  }
  
  /***** HELPER METHODS *****/
  
  private static String _getStringOpt(JSONObject obj, String key) {
    if (obj.has(key)) {
      String value = obj.getString(key);
      if (value.compareTo("") != 0) {
        return value;
      }
    }
    return null;
  }
  
  private static Integer _getIntOpt(JSONObject obj, String key) {
    return obj.has(key) ? obj.getInt(key) : null;
  }
  
  private static JSONArray _getArrOpt(JSONObject obj, String key) {
    return obj.has(key) ? obj.getJSONArray(key) : new JSONArray();
  }
  
}
