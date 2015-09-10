package ui.subcomponents;

import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import ui.questions.QuestionListController;
import ui.core.SubController;
import ui.core.SubPanel;

/**
 *
 * @author sambryant
 */
public class InfobarPanel extends SubPanel<QuestionListController, SubController<QuestionListController>> {

  
  
  /**
   *
   */
  public static final int BIG_LABEL_WIDTH = 120;
  public static final int LABEL_WIDTH = 120;
  
  /**
   *
   */
  protected JLabel questionListLabel;

  /**
   *
   */
  protected JLabel questionLabel;

  /**
   *
   */
  protected JLabel timerLabel;

  protected JLabel totalTimeLabel;
  protected JLabel totalStatsLabel;
  
  /**
   *
   * @param ctrl
   */
  public InfobarPanel(QuestionListController ctrl) {
    super(new SubController(ctrl));
  }

  /**
   *
   */
  @Override
  public void buildComponents() {
    this.questionLabel = new JLabel("Q: ");
    this.questionListLabel = new JLabel("List: ");
    this.timerLabel = new JLabel("Time: -");
    this.totalTimeLabel = new JLabel("Avg Time: -");
    this.totalStatsLabel = new JLabel("Stats: -/-");
  }

  /**
   *
   * @param totalSize
   */
  @Override
  public void layoutComponents(Dimension totalSize) {
    this.sizeComponent(this, totalSize);
    
    this.setLayout(new FlowLayout());
    this.add(this.questionLabel);
    this.add(this.questionListLabel);
    this.add(this.totalStatsLabel);
    this.add(this.timerLabel);
    this.add(this.totalTimeLabel);
    
    Dimension bigLabelSize = new Dimension(BIG_LABEL_WIDTH, 20);
    Dimension labelSize = new Dimension(LABEL_WIDTH, 20);
    this.sizeComponent(this.questionLabel, bigLabelSize);
    this.sizeComponent(this.questionListLabel, labelSize);
    this.sizeComponent(this.totalStatsLabel, labelSize);
    this.sizeComponent(this.timerLabel, labelSize);
    this.sizeComponent(this.totalTimeLabel, labelSize);
  }

  /**
   *
   */
  @Override
  protected void observeListChange() {

  }

  /**
   *
   */
  @Override
  protected void observeQuestionChange() {
    if (this.questionList.isStarted()) {
      int currentIndex = this.questionList.getCurrentIndex();
      int totalNumber = this.questionList.getNumberOfQuestions();
      this.questionListLabel.setText(String.format("List: %d/%d", currentIndex, totalNumber));

      String source = this.questionList.getCurrentQuestion().getSource().toString();
      String number = this.questionList.getCurrentQuestion().getQuestionNumber().toString();
      this.questionLabel.setText(String.format("Q: %s - %s", source, number));

      if (this.questionState.isAnswered()) {
        int time = this.questionState.getLastResponseTime();
        int targetDiff = time - core.Constants.TARGET_TIME;
        String text = String.format("Time: %d (%+d)", time, targetDiff);
        this.timerLabel.setText(text);
      }
      
      int numCorrect = this.questionListController.getNumberCorrect();
      int numAnswered = this.questionListController.getNumberAnswered();
      int totalTime = this.questionListController.getTotalQuestionTime();
      if (numAnswered != 0) {
        this.totalStatsLabel.setText(String.format("Stats: %d/%d", numCorrect, numAnswered));
        this.totalTimeLabel.setText(String.format("Avg Time: %d", (int) ((double) totalTime)/numAnswered));
      } else {
        this.totalStatsLabel.setText("Stats: -/-");
        this.totalTimeLabel.setText("Avg Time: -");
      } 
      
    }
  }

}
