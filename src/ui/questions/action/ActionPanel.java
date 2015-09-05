package ui.questions.action;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import models.Answer;
import ui.components.FAActionButton;
import ui.components.FAButton;
import ui.questions.QuestionListController;
import ui.questions.sorters.QuestionListSorter;
import ui.questions.SubPanel;

public class ActionPanel <T extends QuestionListController> extends SubPanel<T, ActionController<T>> {

  private static final int[] ANSWER_KEYS = new int[]{
    KeyEvent.VK_1,
    KeyEvent.VK_2,
    KeyEvent.VK_3,
    KeyEvent.VK_4,
    KeyEvent.VK_5
  };
  
  protected FAActionButton backButton, nextButton, saveButton;
  protected final Map<Answer, FAButton> answerButtons;
  protected JComboBox filtersBox;
  protected final Map<String, QuestionListSorter> listFilters;
  
  public ActionPanel(ActionController<T> componentController) {
    super(componentController);
    this.answerButtons = new TreeMap<>();
    this.listFilters = null;
  }

  public ActionPanel(ActionController<T> componentController, Map<String, QuestionListSorter> listFilters) {
    super(componentController);
    this.answerButtons = new TreeMap<>();
    this.listFilters = listFilters;
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
          if (questionListController.isInProgress()) {
            ActionPanel.this.componentController.answerQuestion(answer);
          }
        }
      };
      button.addActionListener(buttonPress);
      this.answerButtons.put(answer, button);
      
      // Create key shortcut.
      int buttonMnemonic = ANSWER_KEYS[answer.ordinal()];
      this.questionListController.addKeyAction(buttonMnemonic, buttonPress);
    }
    
    // Create list filter box.
    if (this.listFilters != null) {
      this.filtersBox = new JComboBox(this.listFilters.keySet().toArray());
      this.filtersBox.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          String filterString = (String) filtersBox.getSelectedItem();
          questionListController.setQuestionListSorter(listFilters.get(filterString));
          questionListController.requestFocus();
        }
      });
    }
    
    backButton = new FAActionButton("Back") {  
      @Override
      public void actionPerformed(ActionEvent ev) {
        try {
          ActionPanel.this.questionListController.previousQuestion();
        } catch (QuestionListController.OutOfQuestionsException ex) {
          System.out.printf("LOG: No more questions\n");
        }
      }
    };
  
    nextButton = new FAActionButton("Next") {  
      @Override
      public void actionPerformed(ActionEvent ev) {
        try {
          ActionPanel.this.questionListController.nextQuestion();
        } catch (QuestionListController.OutOfQuestionsException ex) {
          System.out.printf("LOG: No more questions\n");
        }
      }
    };
    
    saveButton = new FAActionButton("Save") {
      @Override
      public void actionPerformed(ActionEvent ev) {
        ActionPanel.this.questionListController.saveToDatabase();
        System.out.printf("Saved to database!\n");
      }
    };
  }

  @Override
  public void layoutComponents() {
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
    
    this.add(this.filtersBox);
    
    this.setAlignmentX(Component.CENTER_ALIGNMENT);
    this.setAlignmentY(Component.TOP_ALIGNMENT);
  }

  @Override
  public void sizeComponents(Dimension totalDimension) {
    this.setSize(totalDimension);
    this.setPreferredSize(totalDimension);
  }

  @Override
  protected void syncToController() {}

  @Override
  protected void syncFromController() {
    if (this.questionListController.isInProgress()) {
      Answer selected = this.questionListController.getSelectedAnswer();
      Answer correct = this.questionListController.getCorrectAnswer();

      for (Answer answer: Answer.values()) {
        if (!this.questionListController.isAnswered()) {
          this.answerButtons.get(answer).setDefaultBackground();
        } else if (answer == selected && answer != correct) {
          this.answerButtons.get(answer).setBackground(Color.RED);
        } else if (answer == correct) {
          this.answerButtons.get(answer).setBackground(Color.GREEN);
        } else {
          this.answerButtons.get(answer).setDefaultBackground();
        }
      }
    }
  }
  
}
