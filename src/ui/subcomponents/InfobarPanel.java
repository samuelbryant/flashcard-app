package ui.subcomponents;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Observable;
import models.AbstractQuestion;
import ui.core.SubPanel;
import ui.questions.ListCtrlImpl;

public class InfobarPanel
<Q_TYPE extends AbstractQuestion,CTRL_TYPE extends ListCtrlImpl<Q_TYPE>>
extends SubPanel<Q_TYPE, CTRL_TYPE> {
  
  protected LabeledInfoBox questionListLabel;
  protected LabeledInfoBox questionLabel;
  protected LabeledInfoBox timerLabel;
  protected LabeledInfoBox totalTimeLabel;
  protected LabeledInfoBox timesSeenLabel;
  protected LabeledInfoBox failureRateLabel;
  
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
        if (ctrl.isStarted()) {
          Q_TYPE q = ctrl.getCurrentQuestion();
          System.out.printf("Current question: %s\n", q);
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
        if (ctrl.isStarted()) {
          int index = ctrl.getCurrentIndex() + 1;
          int total = ctrl.getQuestionNumber();
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
        if (ctrl.isStarted() && ctrl.isAnswered()) {
          int time = ctrl.getLastResponseTime();
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
        if (ctrl.hasAverageQuestionTime()) {
          double time = ctrl.getAverageQuestionTime();
          return String.format("%.1f", time);
        } else {
          return "-";
        }
      }
    });
    
    // Number of times seen label.
    this.timesSeenLabel = this.getInfoBox("Times Seen", "-", new LabeledInfoBox.TextGenerator() {
      @Override
      public String generateLabelText() {
        if (ctrl.isStarted()) {
          int times = ctrl.getCurrentQuestion().getResponses().size();
          return String.format("%d", times);
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
    this.addInfoBox(timesSeenLabel);
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
  public void update(Observable o, Object args) {
    for (LabeledInfoBox lib: labelBoxes) {
      lib.update();
    }
  }

}
