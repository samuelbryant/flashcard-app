package models;

import core.Constants;
import core.IO;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import org.json.*;

public abstract class QuestionIO<T extends AbstractQuestion> {
  
  public static final QuestionIO.FlashcardIO getFlashcardIO() {
    return QuestionIO.FlashcardIO.SINGLETON;
  }
  
  public static final QuestionIO.GREIO getGREIO() {
    return QuestionIO.GREIO.SINGLETON;
  }
  
  private QuestionIO() {}
  
  public abstract void loadQuestionImages(T q);
  
  protected abstract T getNewQuestion(int id);
  
  protected abstract Type getType();
  
  public void save(T q) {
    this.writeQuestion(q);
  }
  
  public T get(int id) {
    return this.loadQuestion(id);
  }
  
  private T loadQuestion(int id) {
    T q = this.readQuestionDataFile(id);
    q.persistent = true;
    q.finishLoading();
    return q;
  }
  
  private T readQuestionDataFile(int id) {
    String dataFilename = Constants.getQuestionDataFile(this.getType(), id);
    String jsonStr = IO.readEntireFileOrDie(dataFilename);
    return this.parseQuestionJSON(jsonStr);
  }
  
  protected abstract void parseQuestionTypeJSON(T q, JSONObject obj);
  
  private T parseQuestionJSON(String jsonStr) {
    JSONObject obj = new JSONObject(jsonStr);
    int id = obj.getInt("id");
    T q = this.getNewQuestion(id);
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
    this.parseQuestionTypeJSON(q, obj);
    return q;
  }
  
  private void writeQuestion(T q) {
    q.validate();
    writeQuestionImageFiles(q);
    writeQuestionDataFile(q);
    q.persistent = true;
  }

  protected abstract void writeQuestionImageFiles(T q);

  private void writeQuestionDataFile(T q) {
    String dataFilename = Constants.getQuestionDataFile(this.getType(), q.id);
    // IO.backupAndRecreateOrDie(dataFilename);
    PrintWriter pw = IO.getPrintWriterOrDie(dataFilename);
    
    String jsonQuestion = this.questionToJSON(q);
    pw.write(jsonQuestion);
    IO.closeOrLive(pw);
  }
  
  private String questionToJSON(T q) {
    JSONObject obj = new JSONObject();
    obj.put("id", q.id);
    this.questionTypeToJSON(q, obj);
    if (q.notes != null)
      obj.put("notes", _objectsToJSONArray(q.notes));
    if (q.tags != null)
      obj.put("tags", _objectsToJSONArray(q.tags));
    if (q.subjects != null)
      obj.put("subjects", _objectsToJSONArray(q.subjects));
    if (q.responses != null)
      obj.put("responses", _responsesToJSONArray(q.responses));
    return obj.toString(2);
  }
  
  protected abstract void questionTypeToJSON(T q, JSONObject obj);

  private static <T> JSONArray _objectsToJSONArray(ArrayList<T> objects) {
    JSONArray arr = new JSONArray();
    for (Object o: objects) {
      arr.put(o.toString());
    }
    return arr;
  }

  private static JSONArray _responsesToJSONArray(ArrayList<Response> responses) {
    JSONArray arr = new JSONArray();
    for (Response r: responses) {
      Map<String, Object> map = new HashMap<>();
      map.put("response", r.response.toString());
      map.put("responseTime", r.responseTime);
      map.put("dateString", r.dateString);
      arr.put(map);
    }
    return arr;
  }

  //***** QUESTION DATA FILE READING METHODS

  private static Response _parseResponseJSON(JSONObject obj) {
    return new Response(
        Answer.valueOf(obj.getString("response")),
        obj.getLong("responseTime"),
        obj.getString("dateString"));
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
  
  public static class GREIO extends QuestionIO<Question> {
    
    private static final GREIO SINGLETON = new GREIO();

    @Override
    public void loadQuestionImages(Question q) {
      q.databaseImageFilename = Constants.getGREQuestionImageFile(q.id);
      q.questionImage = IO.loadImageOrDie(q.databaseImageFilename);
    }

    @Override
    protected Question getNewQuestion(int id) {
      return new Question(id);
    }

    @Override
    protected Type getType() {
      return Type.GRE;
    }

    @Override
    protected void parseQuestionTypeJSON(Question q, JSONObject obj) {
      q.source = Source.valueOf(_getStringOpt(obj, "source"));
      q.questionNumber = _getIntOpt(obj, "questionNumber");
      q.databaseImageFilename = _getStringOpt(obj, "databaseImageFilename");
      q.answer = Answer.valueOf(_getStringOpt(obj, "answer"));
    }
    
    @Override
    protected void questionTypeToJSON(Question q, JSONObject obj) {
      obj.putOpt("source", q.source.toString());
      obj.putOpt("questionNumber", q.questionNumber);
      obj.putOpt("databaseImageFilename", q.databaseImageFilename);
      if (q.answer != null)
        obj.put("answer", q.answer.toString());
    }

    @Override
    protected void writeQuestionImageFiles(Question q) {
      // For objects not in database yet, we need to copy their images to proper location.
      if (q.databaseImageFilename == null || !IO.fileExists(q.databaseImageFilename)) {
        System.out.printf("DATABASE IMAGE FILENAME: %s\n", q.databaseImageFilename);
        String originalImageFilename = q.originalImageFilename;
        String databaseImageFilename = Constants.getGREQuestionImageFile(q.id);
        IO.existsOrDie(originalImageFilename);
        IO.copyOrDie(originalImageFilename, databaseImageFilename);
        q.databaseImageFilename = databaseImageFilename;
      }
    }
    
  }
  
  public static class FlashcardIO extends QuestionIO<Flashcard> {
    
    private static final FlashcardIO SINGLETON = new FlashcardIO();

    @Override
    public void loadQuestionImages(Flashcard q) {
      q.questionImage = IO.loadImageOrDie(q.databaseQuestionFilename);
      q.answerImage = IO.loadImageOrDie(q.databaseAnswerFilename);
    }

    @Override
    protected Flashcard getNewQuestion(int id) {
      return new Flashcard(id);
    }

    @Override
    protected Type getType() {
      return Type.FLASHCARD;
    }

    @Override
    protected void parseQuestionTypeJSON(Flashcard q, JSONObject obj) {
      q.databaseAnswerFilename = _getStringOpt(obj, "databaseAnswerFilename");
      q.databaseQuestionFilename = _getStringOpt(obj, "databaseQuestionFilename");
    }

    @Override
    protected void writeQuestionImageFiles(Flashcard q) {
      if (q.databaseAnswerFilename == null && q.answerImage != null) {
        String filename = Constants.getFLCAnswerImageFile(q.id);
        IO.writeImageOrDie(q.answerImage, filename);
        q.databaseAnswerFilename = filename;
      }
      
      if (q.databaseQuestionFilename == null && q.questionImage != null) {
        String filename = Constants.getFLCQuestionImageFile(q.id);
        IO.writeImageOrDie(q.questionImage, filename);
        q.databaseQuestionFilename = filename;
      }
    }

    @Override
    protected void questionTypeToJSON(Flashcard q, JSONObject obj) {
      obj.putOpt("databaseQuestionFilename", q.databaseQuestionFilename);
      obj.putOpt("databaseAnswerFilename", q.databaseAnswerFilename);
    }
    
  }

}
