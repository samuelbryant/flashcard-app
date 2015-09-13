/*
 * File Overview: TODO
 */
package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import models.QType;
import ui.questions.AppDisplay;
import ui.questions.flc.FlcCtrl;
import ui.questions.flc.FlcListDisplay;
import ui.questions.gre.GreCtrl;
import ui.questions.gre.GreListDisplay;

/**
 *
 * @author author
 */
public final class ApplicationLauncher extends JFrame implements ActionListener {
  
  protected JComboBox typeSelector;
  protected JCheckBox hideFilterBox, hideTaggerBox, recordResponsesBox;
  protected JButton start;
  
  public ApplicationLauncher() {
    this.buildGUI();
  }
  
  public void buildGUI() {
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    
    this.typeSelector = new JComboBox(QType.values());
    this.recordResponsesBox = new JCheckBox("Record answers?");
    this.recordResponsesBox.setSelected(AppDisplay.DEFAULT_RECORD_RESPONSES);
    this.hideFilterBox = new JCheckBox("Hide list filter before answered?");
    this.hideFilterBox.setSelected(AppDisplay.DEFAULT_HIDE_FILTER_BEFORE_RESPONSE);
    this.hideTaggerBox = new JCheckBox("Hide question tagger before answered?");
    this.hideTaggerBox.setSelected(AppDisplay.DEFAULT_HIDE_TAGGER_BEFORE_RESPONSE);

    this.start = new JButton("Launch Application");
    this.start.addActionListener(this);
    
    this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
    this.getContentPane().add(this.typeSelector);
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
    QType t = QType.valueOf(this.typeSelector.getSelectedItem().toString());
    
    if (t == QType.GRE) {
      GreCtrl ctrl = new GreCtrl();
      GreListDisplay disp = new GreListDisplay(ctrl, recordResponses, hideFilter, hideTagger);
      disp.go();
    } else {
      FlcCtrl ctrl = new FlcCtrl();
      FlcListDisplay disp = new FlcListDisplay(ctrl, recordResponses, hideFilter, hideTagger);
      disp.go();
    }

    
    this.setVisible(false);
  }
  
  public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
    JFrame.setDefaultLookAndFeelDecorated(true);
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        try {
          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception ex) {
          System.out.println("Substance Graphite failed to initialize");
        }
        ApplicationLauncher al = new ApplicationLauncher();
    al.setVisible(true);
      }
    });
  }
  
}
