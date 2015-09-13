package ui.subcomponents;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.Observable;
import java.util.TreeMap;
import javax.swing.BoxLayout;
import models.Answer;
import ui.core.components.FAActionButton;
import ui.core.components.FAButton;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.AbstractQuestion;
import ui.core.SubPanel;
import ui.questions.AppCtrl;
import ui.questions.AppCtrlImpl;

public abstract class ActionPanel
<Q_TYPE extends AbstractQuestion, CTRL_TYPE extends AppCtrlImpl<Q_TYPE>>
extends SubPanel<Q_TYPE, CTRL_TYPE> {
  
  public static final int PADDING = 5;

  private static final int[] ANSWER_KEYS = new int[]{
    KeyEvent.VK_1,
    KeyEvent.VK_2,
    KeyEvent.VK_3,
    KeyEvent.VK_4,
    KeyEvent.VK_5,
    KeyEvent.VK_6
  };

  protected FAActionButton backButton, nextButton, saveButton;
  protected final Map<Answer, FAButton> answerButtons;
  
  public ActionPanel(CTRL_TYPE controller) {
    super(controller);
    this.answerButtons = new TreeMap<>();
  }

  @Override
  public void buildComponents() {
    Answer[] answers = Answer.values();

    for (final Answer answer: answers) {
      // Create answer button.
      FAButton button = new FAButton(answer.name());
      ActionListener buttonPress = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          try {
            ctrl.answer(answer);
          } catch (AppCtrl.ListCtrlException ex) {
            Logger.getLogger(ActionPanel.class.getName()).log(Level.SEVERE, null, ex);
          }
        }
      };
      button.addActionListener(buttonPress);
      this.answerButtons.put(answer, button);

      // Create key shortcut.
      int buttonMnemonic = ANSWER_KEYS[answer.ordinal()];
      this.ctrl.addKeyAction(buttonMnemonic, buttonPress);
    }

    backButton = new FAActionButton("Back") {  
      @Override
      public void actionPerformed(ActionEvent ev) {
        try {
          ctrl.prevQuestion();
        } catch (AppCtrl.NotStartedYetException | AppCtrl.NoQuestionsException ex) {
          System.err.println("No more questions");
        }
      }
    };

    nextButton = new FAActionButton("Next") {  
      @Override
      public void actionPerformed(ActionEvent ev) {
        try {
          ctrl.nextQuestion();
        } catch (AppCtrl.NotStartedYetException | AppCtrl.NoQuestionsException ex) {
          System.err.println("No more questions");
        }
      }
    };

    saveButton = new FAActionButton("Save") {
      @Override
      public void actionPerformed(ActionEvent ev) {
        ctrl.save();
      }
    };
  }

  @Override
  public void layoutComponents(Dimension totalSize) {
    this.setLayout(new FlowLayout(FlowLayout.CENTER, PADDING, PADDING));
    for (Answer answer: Answer.values()) {
      this.add(this.answerButtons.get(answer));
    }
    // this.add(backButton);
    this.ctrl.addKeyAction(KeyEvent.VK_LEFT, backButton);
    // this.add(nextButton);
    this.ctrl.addKeyAction(KeyEvent.VK_RIGHT, nextButton);
    this.add(saveButton);
    this.ctrl.addKeyAction(KeyEvent.VK_S, saveButton);

    this.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.setAlignmentY(Component.TOP_ALIGNMENT);

    this.sizeComponent(this, totalSize);
  }

  @Override
  public void update(Observable o, Object args) {
    for (Answer answer: Answer.values()) {
      FAButton button = this.answerButtons.get(answer);
      button.setEnabled(ctrl.canAnswerQuestion());
      button.setDefaultBackground();
    }
  }

}
