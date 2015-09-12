/*
 * File Overview: TODO
 */
package ui.questions.flc;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import models.Answer;
import models.Flashcard;
import ui.core.components.FAButton;
import ui.subcomponents.ActionPanel;

/**
 *
 * @author author
 */
public class FlcActionPanel
extends ActionPanel<Flashcard, FlcQuestionState, FlcQuestionList, FlcCtrl> {
  
  protected FAButton showButton;
  
  public FlcActionPanel(FlcCtrl controller) {
    super(controller);
  }

  @Override
  public void buildComponents() {
    super.buildComponents();
    showButton = new FAButton("Show");
    showButton.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent e) {
        FlcActionPanel.this.questionListController.reveal();
      }
    });
    
    for (Answer answer: Answer.values()) {
      FAButton button = this.answerButtons.get(answer);
      button.setText(answer.getFlashcardAnswer().toString());
    }
  }
  
  @Override
  public void layoutComponents(Dimension totalSize) {
    super.layoutComponents(totalSize);
    this.add(this.showButton);
  }
  
  @Override
  protected void observeQuestionChange() {
    super.observeQuestionChange();

    boolean isStarted = this.questionList.isStarted();
    boolean isAnswered = this.questionList.isStarted() && this.questionState.isAnswered();
    boolean isRevealed = this.questionList.isStarted() && this.questionState.isRevealed();
    
    this.showButton.setEnabled(isStarted && !isRevealed);
    
    for (Answer answer: Answer.values()) {
      FAButton button = this.answerButtons.get(answer);

      button.setEnabled(isStarted && isRevealed);
      if (isAnswered && answer == questionState.getSelectedAnswer()) {
        button.setBackground(Color.GREEN);
      } else {
        button.setDefaultBackground();
      }
    }
  }
  
}
