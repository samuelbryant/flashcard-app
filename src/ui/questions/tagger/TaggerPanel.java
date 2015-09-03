package ui.questions.tagger;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import models.Subject;
import models.Tag;
import ui.Constants;
import ui.components.FAButton;
import ui.questions.QuestionListController;
import ui.questions.SubPanel;

public class TaggerPanel<T extends QuestionListController> extends SubPanel<T, TaggerController<T>>  {
  
  protected FAButton onlyShowUnchecked;
  protected JLabel topLabel;
  protected JLabel subjectsLabel;
  protected JLabel tagsLabel;
  protected final Map<Subject, JCheckBox> subjectCheckboxes;
  protected final Map<Tag, JCheckBox> tagCheckboxes;
  
  public TaggerPanel(TaggerController<T> ctrl) {
    super(ctrl);
    subjectCheckboxes = new TreeMap<>();
    tagCheckboxes = new TreeMap<>();
  }
  
  @Override
  public void buildComponents() {
    Subject[] subjects = Subject.values();
    Tag[] tags = Tag.values();
    
    topLabel = new JLabel("Add Labels");
    topLabel.setFont(Constants.SECTION_FONT);
    
    subjectsLabel = new JLabel("Subjects");
    subjectsLabel.setFont(Constants.SUBSECTION_FONT);
    
    tagsLabel = new JLabel("Tags");
    tagsLabel.setFont(Constants.SUBSECTION_FONT);
    
    for (Subject subject: subjects) {
      JCheckBox cb = new JCheckBox(subject.name());
      cb.addActionListener(updateControllerListener);
      subjectCheckboxes.put(subject, cb);
    }
    
    for (Tag tag: tags) {
      JCheckBox cb = new JCheckBox(tag.name());
      cb.addActionListener(updateControllerListener);
      tagCheckboxes.put(tag, cb);
    }
  }
  
  @Override
  public void layoutComponents() {
    Subject[] subjects = Subject.values();
    Tag[] tags = Tag.values();
   
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    
    this.add(topLabel);
    this.add(subjectsLabel);
    for (Subject subject: subjects) {
      this.add(subjectCheckboxes.get(subject));
    }
    this.add(tagsLabel);
    for (Tag tag: tags) {
      this.add(tagCheckboxes.get(tag));
    }
   
    this.setAlignmentY(TOP_ALIGNMENT);
  }
  
  @Override
  public void sizeComponents(Dimension totalDimension) {
    this.setSize(totalDimension);
    this.setPreferredSize(totalDimension);
  }
  
  @Override
  protected void syncFromController() {
    ArrayList<Subject> subjects = this.componentController.getQuestionSubjects();
    ArrayList<Tag> tags = this.componentController.getQuestionTags();
    setSelectedCheckboxValues(this.tagCheckboxes, tags);
    setSelectedCheckboxValues(this.subjectCheckboxes, subjects);
  }
  
  @Override
  protected void syncToController() {
    ArrayList<Tag> tags = getSelectedCheckboxValues(this.tagCheckboxes);
    ArrayList<Subject> subjects = getSelectedCheckboxValues(this.subjectCheckboxes);
    this.componentController.setQuestionSubjects(subjects);
    this.componentController.setQuestionTags(tags);
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

}
