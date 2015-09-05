package ui.questions.infobar;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Map;
import javax.swing.JLabel;
import ui.questions.QuestionListController;
import ui.questions.sorters.QuestionListSorter;
import ui.questions.SubPanel;

public class InfobarPanel <T extends QuestionListController> extends SubPanel<T, InfobarController<T>> {

  public static final int LABEL_WIDTH = 300;
  
  private JLabel questionListLabel;
  private JLabel questionLabel;
  
  public InfobarPanel(InfobarController<T> componentController) {
    super(componentController);
  }

  public InfobarPanel(InfobarController<T> componentController, Map<String, QuestionListSorter> listFilters) {
    super(componentController);
  }

  @Override
  public void buildComponents() {
    this.questionLabel = new JLabel("Question: ");
    this.questionListLabel = new JLabel("List: ");
  }

  @Override
  public void layoutComponents() {
    this.setLayout(new FlowLayout());
    this.add(this.questionListLabel);
    this.add(this.questionLabel);
  }

  @Override
  public void sizeComponents(Dimension totalDimension) {
    this.setSize(totalDimension);
    this.setPreferredSize(totalDimension);
    Dimension labelSize = new Dimension(200, 20);
    this.sizeComponent(this.questionLabel, labelSize);
    this.sizeComponent(this.questionListLabel, labelSize);
  }

  @Override
  protected void syncToController() {}

  @Override
  protected void syncFromController() {
    QuestionListController ctrl = this.questionListController;
    if (this.questionListController.isInProgress()) {
      int currentIndex = ctrl.getCurrentIndex();
      int totalNumber = ctrl.getQuestionTotal();
      this.questionListLabel.setText(String.format("List: %d/%d", currentIndex, totalNumber));
      
      String source = ctrl.getCurrentQuestion().getSource().toString();
      String number = ctrl.getCurrentQuestion().getQuestionNumber().toString();
      this.questionLabel.setText(String.format("Question: %s - %s", source, number));
    } else {
      int totalNumber = ctrl.getQuestionTotal();
      this.questionListLabel.setText(String.format("List: %d", totalNumber));
      this.questionLabel.setText("Question: Not started");
    }
  }
  
}
