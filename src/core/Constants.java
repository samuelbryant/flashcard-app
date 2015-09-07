/*
 * IDEAS/TODO:
 * Separate metadata (number of times answered correctly, etc.) from the quesiton data itself.
 * This way the database doesn't have to update and serialization is stable.
 */
package core;

import java.awt.Color;
import java.awt.Font;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author author
 */
public class Constants {

  public static final Font BASIC_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
  public static final Font SUBSECTION_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 14);
  public static final Font SECTION_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 16);
  public static final Color BACKGROUND_COLOR = Color.LIGHT_GRAY;
  public static final Color BACKGROUND_COLOR2 = Color.GRAY;
  public static final Color BUTTON_COLOR = Color.DARK_GRAY;
  
  public static final String OUTPUT_DIR = "database";
  public static final String QUESTION_DATA_DIR = "database/question_data";
  public static final String QUESTION_IMAGE_DIR = "database/question_images";
  public static final String QUESTION_DATABASE_MAIN_FILE = "database/database.txt";
  public static final String QUESTION_DATABASE_BACKUP_DIR = "database_backups";
  
  public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yy-MM-dd-HH-mm-ss");

  // Ensure that output file hierarchy exists.
  public final static void setupProjectDirectories() {
    core.IO.createDirOrDie(OUTPUT_DIR);
    core.IO.createDirOrDie(QUESTION_DATA_DIR);
    core.IO.createDirOrDie(QUESTION_IMAGE_DIR);
    core.IO.createDirOrDie(QUESTION_DATABASE_BACKUP_DIR);
  }
  
  public final static String getDatabaseBackupDirName(Date date) {
    String dateStr = DATE_FORMAT.format(date);
    String dirName = QUESTION_DATABASE_BACKUP_DIR + "/" + dateStr;
    return dirName;
  }
  public final static String getDatabaseBackupDirName() {
    return getDatabaseBackupDirName(new Date());
  }

  public final static String getQuestionImageFilename(int id) {
    return QUESTION_IMAGE_DIR + "/" + "ID-" + id + ".png";
  }

  public final static String getQuestionDataFilename(int id) {
    return QUESTION_DATA_DIR + "/" + "ID-" + id + ".txt";
  }

  public static void main(String[] args) {
    setupProjectDirectories();
    System.out.println(getDatabaseBackupDirName());
  }

}
