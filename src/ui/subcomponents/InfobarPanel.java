package ui.subcomponents;

import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import ui.questions.QuestionListController;
import ui.core.SubController;
import ui.core.SubPanel;

public class InfobarPanel extends SubPanel<QuestionListController, SubController<QuestionListController>> {

  public static final int LABEL_WIDTH = 200;
  
  protected JLabel questionListLabel;
  protected JLabel questionLabel;
  protected JLabel timerLabel;
  
  public InfobarPanel(QuestionListController ctrl) {
    super(new SubController(ctrl));
  }

  @Override
  public void buildComponents() {
    this.questionLabel = new JLabel("Question: ");
    this.questionListLabel = new JLabel("List: ");
    this.timerLabel = new JLabel("Time: -");
  }

  @Override
  public void layoutComponents(Dimension totalSize) {
    this.setLayout(new FlowLayout());
    this.add(this.questionListLabel);
    this.add(this.questionLabel);
    this.add(this.timerLabel);
    
    this.sizeComponent(this, totalSize);
    Dimension labelSize = new Dimension(200, 20);
    this.sizeComponent(this.questionLabel, labelSize);
    this.sizeComponent(this.questionListLabel, labelSize);
    this.sizeComponent(this.timerLabel, labelSize);
  }

  @Override
  protected void observeListChange() {
    
  }

  @Override
  protected void observeQuestionChange() {
    if (this.questionList.isStarted()) {
      int currentIndex = this.questionList.getCurrentIndex();
      int totalNumber = this.questionList.getNumberOfQuestions();
      this.questionListLabel.setText(String.format("List: %d/%d", currentIndex, totalNumber));
      
      String source = this.questionList.getCurrentQuestion().getSource().toString();
      String number = this.questionList.getCurrentQuestion().getQuestionNumber().toString();
      this.questionLabel.setText(String.format("Question: %s - %s", source, number));
      
      if (this.questionState.isAnswered()) {
        this.timerLabel.setText("Time: " + this.questionState.getLastResponseTime());
      }
    }
  }
  
}
