package ui.questions.infobar;

import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import ui.questions.QuestionListController;
import ui.questions.quiz.QuizController;

public class QuizInfobarPanel extends InfobarPanel <QuizController> {

  protected JLabel timerLabel;
  
  public QuizInfobarPanel(InfobarController componentController) {
    super(componentController);
  }
  
  @Override
  public void buildComponents() {
    super.buildComponents();
    this.timerLabel = new JLabel("Time: -");
  }

  @Override
  public void layoutComponents() {
    super.layoutComponents();
    this.add(this.timerLabel);
  }

  @Override
  public void sizeComponents(Dimension totalDimension) {
    super.sizeComponents(totalDimension);
    Dimension labelSize = new Dimension(200, 20);
    this.sizeComponent(this.timerLabel, labelSize);
  }

  @Override
  protected void syncToController() {}

  @Override
  protected void syncFromController() {
    super.syncFromController();
    QuestionListController ctrl = this.questionListController;
    if (this.questionListController.isInProgress()) {
      if (this.questionListController.isAnswered()) {
        this.timerLabel.setText("Time: " + this.questionListController.getLastQuestionTime());
      } else {
        this.timerLabel.setText("Time: -");
      }
    }
  }
  
}
