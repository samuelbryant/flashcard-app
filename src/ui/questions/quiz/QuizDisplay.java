package ui.questions.quiz;

import java.awt.Dimension;
import javax.swing.BoxLayout;
import models.Database;
import models.DatabaseIO;
import ui.DisplayWindow;
import ui.questions.QuestionListController;
import ui.questions.QuestionListDisplay;

public class QuizDisplay extends QuestionListDisplay {

  public static final int TOTAL_WIDTH = 900;
  public static final int TOTAL_HEIGHT = 800;

  public QuizDisplay(final QuestionListController ctrl) {
    super(ctrl, TOTAL_WIDTH, TOTAL_HEIGHT);
  }
  
 @Override
  protected void setupGUI() {
    Dimension questionImageSize = new Dimension(totalWidth, totalHeight - 100);
    Dimension actionPanelSize = new Dimension(totalWidth, 100);

    sizeComponent(questionPanel, questionImageSize);
    sizeComponent(actionPanel, actionPanelSize);

    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    this.add(questionPanel);
    this.add(actionPanel);

    this.repaint();
  }
  

  public static void main(String[] args) {
    // Load/initialize models.
    Database db = DatabaseIO.loadDatabase();

    // Load/initialize controller/display.
    QuestionListController ctrl = new QuizController(db, db.getQuestionList());
    QuestionListDisplay display = new QuizDisplay(ctrl);

    // Bring it all home.
    DisplayWindow window = new DisplayWindow();
    window.showDisplay(display);
  }

}
