/*
 * File Overview: TODO
 */
package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;

/**
 *
 * @author author
 */
public final class ApplicationLauncher extends JFrame implements ActionListener {
  
  protected JCheckBox hideFilterBox, hideTaggerBox, recordResponsesBox;
  protected JButton start;
  
  public ApplicationLauncher() {
    this.buildGUI();
  }
  
  public void buildGUI() {
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    
    this.recordResponsesBox = new JCheckBox("Record answers?");
    this.recordResponsesBox.setSelected(QuestionApplication.DEFAULT_RECORD_RESPONSES);
    this.hideFilterBox = new JCheckBox("Hide list filter before answered?");
    this.hideFilterBox.setSelected(QuestionApplication.DEFAULT_HIDE_FILTER_BEFORE_RESPONSE);
    this.hideTaggerBox = new JCheckBox("Hide question tagger before answered?");
    this.hideTaggerBox.setSelected(QuestionApplication.DEFAULT_HIDE_TAGGER_BEFORE_RESPONSE);

    this.start = new JButton("Launch Application");
    this.start.addActionListener(this);
    
    this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
    this.getContentPane().add(this.recordResponsesBox);
    this.getContentPane().add(this.hideFilterBox);
    this.getContentPane().add(this.hideTaggerBox);
    this.getContentPane().add(this.start);
    this.pack();
  }
  
  @Override
  public void actionPerformed(ActionEvent ev) {
    boolean recordResponses = this.recordResponsesBox.isSelected();
    boolean hideFilter = this.hideFilterBox.isSelected();
    boolean hideTagger = this.hideTaggerBox.isSelected();
    
    QuestionApplication app = new QuestionApplication(recordResponses, hideFilter, hideTagger);
    app.go();
    
    this.setVisible(false);
  }
  
  public static void main(String[] args) {
    ApplicationLauncher al = new ApplicationLauncher();
    al.setVisible(true);
  }
  
}
