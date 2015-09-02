package test;

public class MainTest {

  public static void badUsage() {
    System.err.printf("Bad Usage\nUsage: <import1|display|database>\n");
    System.exit(1);
  }

  public static void main(String[] args) {
    if (args.length != 1) {
      badUsage();
    } else if (args[0].compareTo("import1") == 0) {
      imports.TestImport1.main(args);
    } else if (args[0].compareTo("display") == 0) {
      ui.questions.QuestionListDisplay.main(args);
    } else if (args[0].compareTo("quiz") == 0) {
      ui.questions.quiz.QuizDisplay.main(args);
    } else if (args[0].compareTo("subjects") == 0) {
      ui.questions.TaggingModeDisplay.main(args);
    } else if (args[0].compareTo("database") == 0) {
      models.Database.main(args);
    } else {
      badUsage();
    }
  }

}
