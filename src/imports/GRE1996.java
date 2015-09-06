package imports;

import core.Constants;
import core.IO;
import java.util.Map;
import models.Answer;
import models.Database;
import models.DatabaseIO;
import models.Question;
import models.Source;

public class GRE1996 {
  
  static String SRC_DIR = "imports/GRE_1996";
  
  public static void main(String[] args) {
    Constants.setupProjectDirectories();

    Database d = DatabaseIO.loadDatabase();
    
    Map<Integer, Answer> answers = ImportUtilities.readAnswerFile(SRC_DIR + "/answers.txt");
    Source source = Source.GRE_1996;
    
    for (int i = 1; i <= 100; i++) {
      if (i == 90) continue;
      String qname = SRC_DIR + "/1996 " + i + ".png";
      if (!IO.fileExists(qname)) {
        throw new RuntimeException("File not found: " + qname);
      }
      Question q = new Question(source, i, answers.get(i), qname);
      d.addQuestionToSession(q);
    }
    
    DatabaseIO.writeDatabase(d);
  }
  
}
