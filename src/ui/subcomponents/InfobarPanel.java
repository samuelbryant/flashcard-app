package ui.subcomponents;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import models.AbstractQuestion;
import ui.questions.QuestionListController;
import ui.core.SubPanel;
import ui.questions.QuestionList;
import ui.questions.QuestionState;

public class InfobarPanel<
    Q_TYPE extends AbstractQuestion,
    STATE_TYPE extends QuestionState<STATE_TYPE, Q_TYPE, LIST_TYPE>,
    LIST_TYPE extends QuestionList<LIST_TYPE, Q_TYPE, STATE_TYPE>,
    CTRL_TYPE extends QuestionListController<Q_TYPE, STATE_TYPE, LIST_TYPE>>
    extends SubPanel<Q_TYPE, STATE_TYPE, LIST_TYPE, CTRL_TYPE> {
  
  protected LabeledInfoBox questionListLabel;
  protected LabeledInfoBox questionLabel;
  protected LabeledInfoBox timerLabel;
  protected LabeledInfoBox totalTimeLabel;
  protected ArrayList<LabeledInfoBox> labelBoxes = new ArrayList<>();

  public InfobarPanel(CTRL_TYPE ctrl) {
    super(ctrl);
  }
  
  protected LabeledInfoBox getInfoBox(
      String labelText, String maxValuePlaceholder, LabeledInfoBox.TextGenerator textGenerator) {
    return new LabeledInfoBox(textGenerator, labelText, maxValuePlaceholder);
  }

  @Override
  public void buildComponents() {
    // Question label.
    this.questionLabel = this.getInfoBox(
        "Current Question", "Not Started", new LabeledInfoBox.TextGenerator() {
      @Override
      public String generateLabelText() {
        if (questionList.isStarted()) {
          Q_TYPE q = questionList.getCurrentQuestion();
          return q.toDisplayName();
        } else {
          return "Not Started";
        }
      }
    });
    
    // Question list label.
    this.questionListLabel = this.getInfoBox("Question List", "Not Started", new LabeledInfoBox.TextGenerator() {
      @Override
      public String generateLabelText() {
        if (questionList.isStarted()) {
          int index = questionList.getCurrentIndex();
          int total = questionList.getNumberOfQuestions();
          return String.format("%d / %d", index, total);
        } else {
          return "Not Started";
        }
      }
    });
    
    // Timer label.
    this.timerLabel = this.getInfoBox("Response Time", "-", new LabeledInfoBox.TextGenerator() {
      @Override
      public String generateLabelText() {
        if (questionList.isStarted() && questionState.isAnswered()) {
          int time = questionState.getLastResponseTime();
          int targetDiff = time - core.Constants.TARGET_TIME;
          return String.format("%d (%+d)", time, targetDiff);
        } else {
          return "-";
        }
      }
    });
    
    // Timer label.
    this.totalTimeLabel = this.getInfoBox("Average Time", "-", new LabeledInfoBox.TextGenerator() {
      @Override
      public String generateLabelText() {
        if (questionList.isStarted() && questionListController.getNumberAnswered() != 0) {
          double time = questionListController.getAverageQuestionTime();
          return String.format("%.1f", time);
        } else {
          return "-";
        }
      }
    });
    
    this.labelBoxes.clear();
    
    this.addInfoBox(questionListLabel);
    this.addInfoBox(questionLabel);
    this.addInfoBox(timerLabel);
    this.addInfoBox(totalTimeLabel);
  }

  protected void addInfoBox(LabeledInfoBox lib) {
    this.labelBoxes.add(lib);
  }
  
  @Override
  public void layoutComponents(Dimension totalSize) {
    this.sizeComponent(this, totalSize);
    this.setLayout(new FlowLayout());
    
    for (LabeledInfoBox lib: labelBoxes) {
      this.add(lib);
    }
    
    for (LabeledInfoBox lib: labelBoxes) {
      lib.layoutComponents(lib.getComponentSize());
    }
  }

  @Override
  protected void observeListChange() {
  }
  
  @Override
  protected void observeQuestionChange() {
    for (LabeledInfoBox lib: labelBoxes) {
      lib.update();
    }
  }
//
//  @Override
//  protected void observeQuestionChange() {
//    if (this.questionList.isStarted()) {
//      int currentIndex = this.questionList.getCurrentIndex();
//      int totalNumber = this.questionList.getNumberOfQuestions();
//      this.questionListLabel.setText(String.format("List: %d/%d", currentIndex, totalNumber));
//
//      if (this.questionState.isAnswered()) {
//        int time = this.questionState.getLastResponseTime();
//        int targetDiff = time - core.Constants.TARGET_TIME;
//        String text = String.format("Time: %d (%+d)", time, targetDiff);
//        this.timerLabel.setText(text);
//      }
//      
//      int numAnswered = this.questionListController.getNumberAnswered();
//      int totalTime = this.questionListController.getTotalQuestionTime();
//      if (numAnswered != 0) {
//        this.totalTimeLabel.setText(String.format("Avg Time: %d", (int) ((double) totalTime)/numAnswered));
//      } else {
//        this.totalTimeLabel.setText("Avg Time: -");
//      } 
//    }
//  }

}
