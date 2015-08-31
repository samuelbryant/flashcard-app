package imports;

import flashcard.database.Database;
import flashcard.database.DatabaseIO;
import flashcard.question.Question;

/**
 *
 * @author author
 */
public class TestImport1 {

  static String SRC_DIR = "imports/test1 B";

  static String[][] QUESTIONS = new String[][] {
    {"1", "B", "1986 1.png"},
    {"2", "A", "1986 2.png"},
    {"3", "D", "1986 3.png"},
    {"4", "E", "1986 4.png"},
    {"5", "A", "1986 5.png"},
    {"6", "D", "1986 6.png"},
    {"7", "A", "1986 7.png"},
    {"8", "D", "1986 8.png"},
    {"9", "D", "1986 9.png"},
    {"10", "C", "1986 10.png"},
    {"11", "A", "1986 11.png"},
    {"12", "E", "1986 12.png"},
    {"13", "E", "1986 13.png"},
    {"14", "A", "1986 14.png"},
    {"15", "C", "1986 15.png"}
  };

  static String[] imgList = new String[]{
    "1986 10.png", "1986 100.png", "1986 12.png", "1986 13.png", "1986 2.png", "1986 20.png",
    "1986 24.png", "1986 26.png", "1986 28.png", "1986 29.png"
  };

  public static void main(String[] args) {

    flashcard.Constants.setupProjectDirectories();

    Database d = DatabaseIO.loadDatabase();

    Question.QuestionSource source = Question.QuestionSource.GRE_1986;

    for (String[] question: QUESTIONS) {
      int number = Integer.parseInt(question[0]);
      Question.Answer answer = Question.Answer.valueOf(question[1]);
      String imgFilename = SRC_DIR + "/" + question[2];

      Question q = new Question(source, number, answer, imgFilename);

      d.addQuestionToSession(q);
      System.out.println("Added quesiton: " + q);
    }

    DatabaseIO.saveDatabase(d);
  }

}
