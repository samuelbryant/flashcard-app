package imports;

import core.IO;
import java.util.Scanner;
import models.Database;
import models.DatabaseIO;
import models.Question;
import models.Source;
import models.Tag;

public class HardestImport {

  public static final String SRC_DIR = "imports/HARD_LISTS";

  static Source[] SOURCES = new Source[] {
    Source.GRE_1986,
    Source.GRE_1991,
    Source.SAMPLE_1,
    Source.SAMPLE_2,
    Source.SAMPLE_3
      // 2001 Already Done
      // TODO: 1996
  };

  public static void main(String[] args) {
    Database db = DatabaseIO.loadDatabase();

    for (Source src: SOURCES) {
      String hardFile = SRC_DIR + "/" + src.name() + ".txt";

      Scanner scan = IO.getScannerOrDie(hardFile);

      while (scan.hasNextInt()) {
        Integer number = scan.nextInt();
        System.out.printf("%s #%d\n", src.toString(), number);
        Question q = db.findQuestion(src, number);
        q.addTag(Tag.HARD);
      } 
    }

    DatabaseIO.writeDatabase(db);

  }



}
