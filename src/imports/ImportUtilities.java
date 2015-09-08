package imports;

import core.IO;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import models.Answer;

public class ImportUtilities {

  static Map<Integer, Answer> readAnswerFile(String filename) {
    Map<Integer, Answer> map = new HashMap<>();

    Scanner scan = IO.getScannerOrDie(filename);
    String line;
    while (scan.hasNextLine()) {
      line = scan.nextLine().trim();
      if (line.compareTo("") != 0) {
        String[] parts = line.split(" ");
        System.out.printf("Parts: |%s| |%s|\n", parts[0], parts[1]);
        map.put(Integer.parseInt(parts[0]), Answer.valueOf(parts[1]));        
      }
    }
    return map;
  }

}
