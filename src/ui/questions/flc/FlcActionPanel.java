/*
 * File Overview: TODO
 */
package ui.questions.flc;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import models.Answer;
import models.Flashcard;
import ui.core.components.FAButton;
import ui.subcomponents.ActionPanel;


public class FlcActionPanel extends ActionPanel<Flashcard, FlcCtrl> {
  
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
        FlcActionPanel.this.ctrl.toggleVisibility();
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
  public void update(Observable o, Object args) {
    super.update(o, args);

    boolean isStarted = this.ctrl.isStarted();
    boolean isAnswered = this.ctrl.isStarted() && this.ctrl.isAnswered();
    boolean isRevealed = this.ctrl.isStarted() && this.ctrl.isRevealed();
    
    this.showButton.setText(isRevealed ? "Hide" : "Show");
    
    for (Answer answer: Answer.values()) {
      FAButton button = this.answerButtons.get(answer);

      button.setEnabled(isStarted && isRevealed);
      if (isAnswered && answer == ctrl.getSelectedAnswer()) {
        button.setBackground(Color.GREEN);
      } else {
        button.setDefaultBackground();
      }
    }
  }
  
}
