package imports;

import core.Constants;
import java.util.Map;
import models.Answer;
import models.Database;
import models.DatabaseIO;
import models.Question;
import models.Source;

public class PT2Import {

  static String SRC_DIR = "imports/pt2";

  public static void main(String[] args) {
    Constants.setupProjectDirectories();

    Database d = DatabaseIO.loadDatabase();

    Map<Integer, Answer> answers = ImportUtilities.readAnswerFile(SRC_DIR + "/answers.txt");
    Source source = Source.SAMPLE_2;

    for (int i = 1; i <= 100; i++) {
      Question q = new Question(source, i, answers.get(i), SRC_DIR + "/PT2 " + i + ".png");
      d.addQuestionToSession(q);
    }

    DatabaseIO.writeDatabase(d);
  }

}
