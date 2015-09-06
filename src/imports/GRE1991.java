/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imports;

import core.Constants;
import core.IO;
import java.util.Map;
import models.Answer;
import models.Database;
import models.DatabaseIO;
import models.Question;
import models.Source;

/**
 *
 * @author sambryant
 */
public class GRE1991 {
  
  static String SRC_DIR = "imports/GRE_1991";
  
  public static void main(String[] args) {
    Constants.setupProjectDirectories();

    Database d = DatabaseIO.loadDatabase();
    
    Map<Integer, Answer> answers = ImportUtilities.readAnswerFile(SRC_DIR + "/answers.txt");
    Source source = Source.GRE_1991;
    
    for (int i = 1; i <= 100; i++) {
      String qname = SRC_DIR + "/1991 " + i + ".png";
      if (!IO.fileExists(qname)) {
        throw new RuntimeException("File not found: " + qname);
      }
      Question q = new Question(source, i, answers.get(i), qname);
      d.addQuestionToSession(q);
    }
    
    DatabaseIO.writeDatabase(d);
  }
  
}
