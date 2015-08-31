/*
 * IDEAS/TODO:
 * Separate metadata (number of times answered correctly, etc.) from the quesiton data itself.
 * This way the database doesn't have to update and serialization is stable.
 */
package flashcard;

import static flashcard.Constants.OUTPUT_DIR;
import flashcard.question.Question;
import core.IO;

/**
 *
 * @author author
 */
public class Constants {
  
  public static final String OUTPUT_DIR = "output";
  public static final String QUESTION_DATA_DIR = "output/questions";
  public static final String QUESTION_IMAGE_DIR = "output/question_images";
  public static final String QUESTION_DATABASE_MAIN_FILE = "output/questions/database.txt";
  public static final String QUESTION_DATABASE_DATA_FILE = "output/questions/serialized.bin";
  
  // Ensure that output file hierarchy exists.
  public final static void setupProjectDirectories() {
    core.IO.createDirOrDie(OUTPUT_DIR);
    core.IO.createDirOrDie(QUESTION_DATA_DIR);
    IO.createDirOrDie(QUESTION_IMAGE_DIR);
  }
  
  public final static String getQuestionImageFilename(Question q) {
    if (q.getId() == null) {
      throw new IllegalArgumentException("Cannot generate filename for question without id");
    }
    
    String fname = QUESTION_IMAGE_DIR + "/";
    if (q.source != null) {
      fname += q.source.getOuputName();
      if (q.questionNumber != null) {
        fname += " Q-" + q.questionNumber;
      }
    }
    fname += " ID-" + q.getId() + ".png";
    return fname;
  }
  
  public final static String getQuestionDataFilename(Question q) {
    if (q.getId() == null) {
      throw new IllegalArgumentException("Cannot generate filename for question without id");
    }
    String fname = QUESTION_DATA_DIR + "/";
    fname += " ID-" + q.getId() + ".txt";
    return fname;
  }
  
  public static void main(String[] args) {
    setupProjectDirectories();
  }
  
}
