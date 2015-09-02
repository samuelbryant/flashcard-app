package ui.questions;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import ui.components.FAPanel;

public abstract class SubPanel <T extends QuestionListController, K extends SubController<T>> extends FAPanel implements Observer {

  protected final K componentController;
  protected final T questionListController;
  protected final ActionListener updateControllerListener;
 
  public SubPanel(K componentController) {
    super();
    this.componentController = componentController;
    this.questionListController = this.componentController.getQuestionListController();
    this.questionListController.addObserver(this);
    
    // Create action listener to update controller when necessary.
    this.updateControllerListener = new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        SubPanel.this.syncToController();
      }
    };
  }
  
  public abstract void buildComponents();
  
  public abstract void layoutComponents();
  
  public abstract void sizeComponents(Dimension totalDimension);
  
  protected abstract void syncToController();
  
  protected abstract void syncFromController();
  
  public void update(Observable o, Object args) {
    this.syncFromController();
  }
  
}
