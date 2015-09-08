package imports;

import core.Constants;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;
import models.Answer;
import models.Database;
import models.DatabaseIO;
import models.Question;
import models.Source;
import models.Tag;

/**
 *
 * @author sambryant
 */
public class EX2001Import {

  static String SRC_DIR = "imports/2001";

  /**
   *
   * @param args
   * @throws FileNotFoundException
   */
  public static void main(String[] args) throws FileNotFoundException {
    Constants.setupProjectDirectories();

    Database d = DatabaseIO.loadDatabase();

    Source source = Source.GRE_2001;

    Scanner scan = new Scanner(new File(SRC_DIR + "/answers.txt"));
    while (scan.hasNextLine()) {
      String parts[] = scan.nextLine().split(" +");
      int qNumber = Integer.parseInt(parts[0]);
      System.out.printf("|%s|%s|%s|\n", parts[0], parts[1], parts[2]);
      Answer correctAnswer = Answer.valueOf(parts[1]);
      Answer myAnswer = null;
      try {
        myAnswer = Answer.valueOf(parts[2]);
      } catch (Exception e) {}


      String qFile = SRC_DIR + "/2001 " + qNumber + ".png";
      Question q = new Question(source, qNumber, correctAnswer, qFile);
      if (myAnswer != correctAnswer) {
        q.addTag(Tag.WRONG);
      }
      d.addQuestionToSession(q);
    }

    DatabaseIO.writeDatabase(d);
  }

}
