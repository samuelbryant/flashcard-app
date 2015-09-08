package ui.questions;

import models.Database;
import models.DatabaseIO;
import ui.core.DisplayWindow;
import static ui.questions.QuestionListDisplay.TOTAL_HEIGHT;
import static ui.questions.QuestionListDisplay.TOTAL_WIDTH;

public class QuestionQuizDisplay extends QuestionListDisplay {

  public QuestionQuizDisplay(QuestionListController ctrl) {
    super(ctrl);
  }

  @Override
  public void buildComponents() {
    super.buildComponents();
    this.filterPanel.setHideBeforeAnswering(false);
    // this.taggerPanel.setHideBeforeAnswering(true);
    this.ctrl.setRecordAnswers(true);
  }

  public static void main(String[] args) {
    // Load/initialize models.
    Database db = DatabaseIO.loadDatabase();

    // Load/initialize controller/display.
    QuestionListController ctrl = new QuestionListController();
    QuestionQuizDisplay display = new QuestionQuizDisplay(ctrl);

    // Bring it all home.
    DisplayWindow window = new DisplayWindow(TOTAL_WIDTH, TOTAL_HEIGHT);
    window.showDisplay(display);
  }
}
