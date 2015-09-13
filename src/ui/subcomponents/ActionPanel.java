package ui.subcomponents;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.BoxLayout;
import models.Answer;
import ui.core.components.FAActionButton;
import ui.core.components.FAButton;
import ui.questions.QuestionListController;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.AbstractQuestion;
import ui.questions.QuestionList;
import ui.questions.QuestionState;
import ui.core.SubPanel;

public abstract class ActionPanel<
    Q_TYPE extends AbstractQuestion,
    STATE_TYPE extends QuestionState<STATE_TYPE, Q_TYPE, LIST_TYPE>,
    LIST_TYPE extends QuestionList<LIST_TYPE, Q_TYPE, STATE_TYPE>,
    CTRL_TYPE extends QuestionListController<Q_TYPE, STATE_TYPE, LIST_TYPE>>
    extends SubPanel<Q_TYPE, STATE_TYPE, LIST_TYPE, CTRL_TYPE> {

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

  /**
   *
   */
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
            questionListController.answer(answer);
          } catch (QuestionState.AlreadyAnsweredException | QuestionList.NotStartedYetException ex) {
            Logger.getLogger(ActionPanel.class.getName()).log(Level.SEVERE, null, ex);
          }
        }
      };
      button.addActionListener(buttonPress);
      this.answerButtons.put(answer, button);

      // Create key shortcut.
      int buttonMnemonic = ANSWER_KEYS[answer.ordinal()];
      this.questionListController.addKeyAction(buttonMnemonic, buttonPress);
    }

    backButton = new FAActionButton("Back") {  
      @Override
      public void actionPerformed(ActionEvent ev) {
        try {
          questionList.lastQuestion();
        } catch (QuestionList.OutOfQuestionsException ex) {
          System.err.println("No more questions");
        }
      }
    };

    nextButton = new FAActionButton("Next") {  
      @Override
      public void actionPerformed(ActionEvent ev) {
        try {
          questionList.nextQuestion();
        } catch (QuestionList.OutOfQuestionsException ex) {
          System.err.println("No more questions");
        }
      }
    };

    saveButton = new FAActionButton("Save") {
      @Override
      public void actionPerformed(ActionEvent ev) {
        questionListController.save();
        System.out.printf("Saved to database!\n");
      }
    };
  }

  /**
   *
   * @param totalSize
   */
  @Override
  public void layoutComponents(Dimension totalSize) {
    this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    for (Answer answer: Answer.values()) {
      this.add(this.answerButtons.get(answer));
    }
    // this.add(backButton);
    this.questionListController.addKeyAction(KeyEvent.VK_LEFT, backButton);
    // this.add(nextButton);
    this.questionListController.addKeyAction(KeyEvent.VK_RIGHT, nextButton);
    this.add(saveButton);
    this.questionListController.addKeyAction(KeyEvent.VK_S, saveButton);

    // this.add(this.filtersBox);

    this.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.setAlignmentY(Component.TOP_ALIGNMENT);

    this.sizeComponent(this, totalSize);
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
    this.backButton.setEnabled(questionList.hasLastQuestion());
    this.nextButton.setEnabled(questionList.hasNextQuestion());
  }
//    boolean isStarted = this.questionList.isStarted();
//    boolean isAnswered = this.questionList.isStarted() && this.questionState.isAnswered();
//
//    for (Answer answer: Answer.values()) {
//      FAButton button = this.answerButtons.get(answer);
//
//      button.setEnabled(isStarted);
//
//      if (isAnswered && answer == questionState.getCorrectAnswer()) {
//        button.setBackground(Color.GREEN);
//      } else if (isAnswered && answer == questionState.getSelectedAnswer()) {
//        button.setBackground(Color.RED);
//      } else {
//        button.setDefaultBackground();
//      }
//    }
//  }

}
