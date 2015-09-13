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
import java.util.HashMap;
import java.util.Map;
import models.QType;

/**
 *
 * @author author
 */
public class Constants {
  
  static Color SCHEME_GRAY = new Color(124, 120, 106);
  static Color SCHEME_BLUE = new Color(141, 205, 193);
  static Color SCHEME_GREEN = new Color(211, 227, 151);
  static Color SCHEME_YELLOW = new Color(255, 245, 195);
  static Color SCHEME_RED = new Color(235, 110, 68);
  
  public static final int TARGET_TIME = 90;
  
  public static final Color BUTTON_COLOR = SCHEME_RED;
  public static final Color BACKGROUND_COLOR = SCHEME_GRAY;
  
  public static final Font BASIC_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 13);
  public static final Font SUBSECTION_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 14);
  public static final Font SECTION_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 16);
  
  public static final Color SECTION_LABEL_COLOR = SCHEME_BLUE;
  public static final Color SUBSECTION_LABEL_COLOR = SCHEME_BLUE;
  public static final Color NORMAL_LABEL_COLOR = Color.BLACK;
  
  public static final Color CORRECT_COLOR = SCHEME_BLUE;
  public static final Color INCORRECT_COLOR = Color.RED;
  public static final Color COMBO_BOX_COLOR = SCHEME_GREEN;

  public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yy-MM-dd-HH-mm-ss");

  private static final String OUTPUT_DIR = "database";
  private static final String OUTPUT_BACKUP_DIR = "database_backups";
  private static final String DATA_DIR_NAME = "data";
  private static final String IMAGE_DIR_NAME = "images";
  private static final String DB_FILE_NAME = "database.txt";
  private static final Map<QType, String> DIR_NAMES = new HashMap<>();
  static {
    DIR_NAMES.put(QType.GRE, "gre");
    DIR_NAMES.put(QType.FLASHCARD, "flc");
  }
  
  /**
   * Ensures that entire output file hierarchy exists.
   */
  public final static void setupProjectDirectories() {
    core.IO.createDirOrDie(OUTPUT_DIR);
    core.IO.createDirOrDie(OUTPUT_BACKUP_DIR);
    for (QType t: QType.values()) {
      core.IO.createDirOrDie(Constants.getDatabaseDir(t));
      core.IO.createDirOrDie(Constants.getDatabaseDataDir(t));
      core.IO.createDirOrDie(Constants.getDatabaseImageDir(t));
    }
  }
  
  public static String getQuestionDataFile(QType t, int id) {
    return String.format("%s/ID-%d.txt", getDatabaseDataDir(t), id);
  }
  
  public static String getGREQuestionImageFile(int id) {
    return String.format("%s/ID-%d.png", getDatabaseImageDir(QType.GRE), id);
  }
  
  public static String getFLCQuestionImageFile(int id) {
    return String.format("%s/ID-%d-Q.png", getDatabaseImageDir(QType.FLASHCARD), id);
  }
  
  public static String getFLCAnswerImageFile(int id) {
    return String.format("%s/ID-%d-A.png", getDatabaseImageDir(QType.FLASHCARD), id);
  }
  
  public static String getDatabaseDir(QType t) {
    return getDatabaseDir(t, Constants.OUTPUT_DIR);
  }
  
  public static String getDatabaseFile(QType t) {
    return getDatabaseFile(t, Constants.OUTPUT_DIR);
  }
  
  public static String getDatabaseDataDir(QType t) {
    return getDatabaseDataDir(t, Constants.OUTPUT_DIR);
  }
  
  public static String getDatabaseImageDir(QType t) {
    return getDatabaseImageDir(t, Constants.OUTPUT_DIR);
  }

  private static String getDatabaseDir(QType t, String topLevelDir) {
    return String.format("%s/%s", topLevelDir, Constants.DIR_NAMES.get(t));
  }
  
  private static String getDatabaseFile(QType t, String topLevelDir) {
    return String.format("%s/%s/%s", topLevelDir, Constants.DIR_NAMES.get(t), Constants.DB_FILE_NAME);
  }
  
  private static String getDatabaseDataDir(QType t, String topLevelDir) {
     return String.format("%s/%s/%s", topLevelDir, Constants.DIR_NAMES.get(t), Constants.DATA_DIR_NAME);
  }
  
  private static String getDatabaseImageDir(QType t, String topLevelDir) {
     return String.format("%s/%s/%s", topLevelDir, Constants.DIR_NAMES.get(t), Constants.IMAGE_DIR_NAME);
  }
  
  public static void backupDatabaseOrDie() {
    Date d = new Date();
    String backupDirname = String.format("%s/%s", OUTPUT_BACKUP_DIR, DATE_FORMAT.format(d));
    
    IO.createDirOrDie(backupDirname);
    
    for (QType t: QType.values()) {
      String mainFile = getDatabaseFile(t);
      String dataDir = getDatabaseDataDir(t);
      
      String dstSubDir = getDatabaseDir(t, backupDirname);
      String dstMainFile = getDatabaseFile(t, backupDirname);
      String dstDataDir = getDatabaseDataDir(t, backupDirname);
      
      IO.createDirOrDie(dstSubDir);
      IO.copyOrDie(mainFile, dstMainFile);
      IO.copyOrDie(dataDir, dstDataDir); 
    }
  }
  
  public static void main(String[] args) {
    Constants.setupProjectDirectories();
  }

}
