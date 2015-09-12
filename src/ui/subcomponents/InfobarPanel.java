package ui.subcomponents;

import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import models.AbstractQuestion;
import ui.questions.QuestionListController;
import ui.core.SubController;
import ui.core.SubPanel;
import ui.questions.QuestionList;
import ui.questions.QuestionState;

public class InfobarPanel<
    Q_TYPE extends AbstractQuestion,
    STATE_TYPE extends QuestionState<STATE_TYPE, Q_TYPE, LIST_TYPE>,
    LIST_TYPE extends QuestionList<LIST_TYPE, Q_TYPE, STATE_TYPE>,
    CTRL_TYPE extends QuestionListController<Q_TYPE, STATE_TYPE, LIST_TYPE>>
    extends SubPanel<Q_TYPE, STATE_TYPE, LIST_TYPE, CTRL_TYPE, SubController<CTRL_TYPE>> {

  
  public static final int BIG_LABEL_WIDTH = 120;
  public static final int LABEL_WIDTH = 120;
  protected JLabel questionListLabel;
  protected JLabel questionLabel;
  protected JLabel timerLabel;
  protected JLabel totalTimeLabel;
  protected JLabel totalStatsLabel;
  
  public InfobarPanel(CTRL_TYPE ctrl) {
    super(new SubController(ctrl));
  }

  @Override
  public void buildComponents() {
    this.questionLabel = new JLabel("Q: ");
    this.questionListLabel = new JLabel("List: ");
    this.timerLabel = new JLabel("Time: -");
    this.totalTimeLabel = new JLabel("Avg Time: -");
    this.totalStatsLabel = new JLabel("Stats: -/-");
  }

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

  @Override
  protected void observeListChange() {

  }

  @Override
  protected void observeQuestionChange() {
    if (this.questionList.isStarted()) {
      int currentIndex = this.questionList.getCurrentIndex();
      int totalNumber = this.questionList.getNumberOfQuestions();
      this.questionListLabel.setText(String.format("List: %d/%d", currentIndex, totalNumber));

      if (this.questionState.isAnswered()) {
        int time = this.questionState.getLastResponseTime();
        int targetDiff = time - core.Constants.TARGET_TIME;
        String text = String.format("Time: %d (%+d)", time, targetDiff);
        this.timerLabel.setText(text);
      }
      
      int numAnswered = this.questionListController.getNumberAnswered();
      int totalTime = this.questionListController.getTotalQuestionTime();
      if (numAnswered != 0) {
        this.totalTimeLabel.setText(String.format("Avg Time: %d", (int) ((double) totalTime)/numAnswered));
      } else {
        this.totalTimeLabel.setText("Avg Time: -");
      } 
    }
  }

}
