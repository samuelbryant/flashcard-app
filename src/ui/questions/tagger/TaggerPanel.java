package ui.questions.tagger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.TreeMap;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import models.Subject;
import models.Tag;
import ui.Constants;
import ui.components.FAPanel;

public class TaggerPanel extends FAPanel implements Observer {
  
  protected TaggerController ctrl;
  
  private final Map<Subject, JCheckBox> subjectCheckboxes;
  private final Map<Tag, JCheckBox> tagCheckboxes;
  
  public TaggerPanel(final TaggerController ctrl) {
    this.ctrl = ctrl;
    this.ctrl.addObserver(this);
    
    Subject subjects[] = Subject.values();
    Tag tags[] = Tag.values();
    
    subjectCheckboxes = new TreeMap<>();
    tagCheckboxes = new TreeMap<>();
    
    // Class that listens for checkbox changes and updates controller.
    ActionListener checkboxListener = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        TaggerPanel.this._syncToController();
      }
    };
    
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    
    JLabel sectionLabel = new JLabel("Add Labels");
    sectionLabel.setFont(Constants.SECTION_FONT);
    this.add(sectionLabel);
    
    JLabel subjectLabel = new JLabel("Subjects");
    subjectLabel.setFont(Constants.SUBSECTION_FONT);
    this.add(subjectLabel);
    for (Subject subject: subjects) {
      JCheckBox cb = new JCheckBox(subject.name());
      cb.addActionListener(checkboxListener);
      subjectCheckboxes.put(subject, cb);
      this.add(cb);
    }
    
    JLabel tagLabel = new JLabel("Tags");
    tagLabel.setFont(Constants.SUBSECTION_FONT);
    this.add(tagLabel);
    for (Tag tag: tags) {
      JCheckBox cb = new JCheckBox(tag.name());
      // cb.setAlignmentX(Component.LEFT_ALIGNMENT);
      cb.addActionListener(checkboxListener);
      tagCheckboxes.put(tag, cb);
      this.add(cb);
    }
    
    _syncFromController();
  }
  
  private void _syncFromController() {
    ArrayList<Subject> subjects = this.ctrl.getQuestionSubjects();
    ArrayList<Tag> tags = this.ctrl.getQuestionTags();
    setSelectedCheckboxValues(this.tagCheckboxes, tags);
    setSelectedCheckboxValues(this.subjectCheckboxes, subjects);
  }
  
  private void _syncToController() {
    ArrayList<Tag> tags = getSelectedCheckboxValues(this.tagCheckboxes);
    ArrayList<Subject> subjects = getSelectedCheckboxValues(this.subjectCheckboxes);
    this.ctrl.setQuestionSubjects(subjects);
    this.ctrl.setQuestionTags(tags);
  }
  
  protected static <T> void setSelectedCheckboxValues(Map<T, JCheckBox> checkboxes, ArrayList<T> values) {
    for (Map.Entry<T, JCheckBox> entry: checkboxes.entrySet()) {
      entry.getValue().setSelected(false);
    }
    for (T t: values) {
      checkboxes.get(t).setSelected(true);
    }
  }
  
  private static <T> ArrayList<T> getSelectedCheckboxValues(Map<T, JCheckBox> checkboxes) {
    ArrayList<T> list = new ArrayList<>();
    for (Map.Entry<T, JCheckBox> entry : checkboxes.entrySet()) {
      if (entry.getValue().isSelected()) {
        list.add(entry.getKey());
      }
    }
    return list;
  }

  @Override
  public void update(Observable o, Object arg) {
    this._syncFromController();
  }
  
}
