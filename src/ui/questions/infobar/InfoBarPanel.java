/**
 * Left-to-write panel with basic info about the current view.
 * 
 */

package ui.questions.infobar;

import java.util.Observer;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import ui.components.FAPanel;
import ui.questions.QuestionListController;

public class InfoBarPanel <T extends QuestionListController> extends FAPanel implements Observer {
  
  protected InfoBarController ctrl;
  
  protected JLabel questionNumberLabel;
  
  public InfoBarPanel(final T questionListController) {
    super();
    questionListController.addObserver(this);
    this.ctrl = new InfoBarController<>(this);
    
    this.questionNumberLabel = new JLabel("Not started");
    
    this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    
    this.add(questionNumberLabel);
  }

  @Override
  public void addListener(InvalidationListener listener) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void removeListener(InvalidationListener listener) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void update(java.util.Observable o, Object arg) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
 
  
}
