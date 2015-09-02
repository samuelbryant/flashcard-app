package ui.questions.filter;

import java.awt.event.ActionEvent;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import models.Subject;
import models.Tag;
import ui.components.FAButton;
import ui.components.FAPanel;
import ui.questions.QuestionListController;

public class FilterPanel extends FAPanel {
  
  protected FilterController ctrl;
  
  private final Map<Subject, JCheckBox> subjectCheckboxes;
  private final Map<Tag, JCheckBox> tagCheckboxes;
  // private final Map<Source, JCheckBox> sourceCheckboxes;
  
  public FilterPanel(final QuestionListController qlCtrl) {
    this.ctrl = new FilterController(qlCtrl);
    
    Subject subjects[] = Subject.values();
    Tag tags[] = Tag.values();
    // Source sources[] = Source.values();
    
    subjectCheckboxes = new TreeMap<>();
    tagCheckboxes = new TreeMap<>();
    // sourceCheckboxes = new TreeMap<>();
    
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    
    this.add(new JLabel("Subjects"));
    for (Subject subject: subjects) {
      subjectCheckboxes.put(subject, new JCheckBox(subject.name()));
      this.add(subjectCheckboxes.get(subject));
    }
    
    this.add(new JLabel("Tags"));
    for (Tag tag: tags) {
      tagCheckboxes.put(tag, new JCheckBox(tag.name()));
      this.add(tagCheckboxes.get(tag));
    }
    
//    this.add(new JLabel("Sources"));
//    for (Source source: sources) {
//      sourceCheckboxes.put(source, new JCheckBox(source.name()));
//      this.add(sourceCheckboxes.get(source));
//    }
    
    this.add(new FAButton("Filter Questions") {
      @Override
      public void actionPerformed(ActionEvent ev) {
        _syncToController();
        ctrl.filter();
      }
    });
    _syncFromController();
  }
  
  private void _syncToController() {
    for (Subject subject: Subject.values()) {
      this.ctrl.subjectFilters.put(subject, this.subjectCheckboxes.get(subject).isSelected());
    }
    for (Tag tag: Tag.values()) {
      this.ctrl.tagFilters.put(tag, this.tagCheckboxes.get(tag).isSelected());
    }
//    for (Source source: Source.values()) {
//      this.ctrl.sourceFilters.put(source, this.sourceCheckboxes.get(source).isSelected());
//    }
  }
  
  private void _syncFromController() {
    for (Subject subject: Subject.values()) {
      this.subjectCheckboxes.get(subject).setSelected(this.ctrl.subjectFilters.get(subject));
    }
    for (Tag tag: Tag.values()) {
      this.tagCheckboxes.get(tag).setSelected(this.ctrl.tagFilters.get(tag));
    }
//    for (Source source: Source.values()) {
//      this.sourceCheckboxes.get(source).setSelected(this.ctrl.sourceFilters.get(source));
//    }
  }

}
