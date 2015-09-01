/*
 * IDEAS/TODO:
 * Separate metadata (number of times answered correctly, etc.) from the quesiton data itself.
 * This way the database doesn't have to update and serialization is stable.
 */
package core;

/**
 *
 * @author author
 */
public class Constants {

  public static final String OUTPUT_DIR = "database";
  public static final String QUESTION_DATA_DIR = "database/question_data";
  public static final String QUESTION_IMAGE_DIR = "database/question_images";
  public static final String QUESTION_DATABASE_MAIN_FILE = "database/database.txt";

  // Ensure that output file hierarchy exists.
  public final static void setupProjectDirectories() {
    core.IO.createDirOrDie(OUTPUT_DIR);
    core.IO.createDirOrDie(QUESTION_DATA_DIR);
    core.IO.createDirOrDie(QUESTION_IMAGE_DIR);
  }

  public final static String getQuestionImageFilename(int id) {
    return QUESTION_IMAGE_DIR + "/" + "ID-" + id + ".png";
  }

  public final static String getQuestionDataFilename(int id) {
    return QUESTION_DATA_DIR + "/" + "ID-" + id + ".txt";
  }

  public static void main(String[] args) {
    setupProjectDirectories();
  }

}
