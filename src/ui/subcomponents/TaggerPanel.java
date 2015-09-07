package ui.subcomponents;

import static java.awt.Component.TOP_ALIGNMENT;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import models.Question;
import models.Subject;
import models.Tag;
import core.Constants;
import ui.core.SubController;
import ui.components.FAButton;
import ui.components.FACheckbox;
import ui.questions.QuestionListController;
import ui.core.SubPanel;

public class TaggerPanel extends SubPanel<QuestionListController, SubController<QuestionListController>>{
  
  protected FAButton onlyShowUnchecked;
  protected JLabel topLabel;
  protected JLabel subjectsLabel;
  protected JLabel tagsLabel;
  protected final Map<Subject, FACheckbox> subjectCheckboxes;
  protected final Map<Tag, FACheckbox> tagCheckboxes;
  protected boolean hideBeforeAnswering;
  
  public TaggerPanel(QuestionListController ctrl) {
    super(new SubController(ctrl));
    subjectCheckboxes = new TreeMap<>();
    tagCheckboxes = new TreeMap<>();
    this.hideBeforeAnswering = false;
  }
  
  public void setHideBeforeAnswering(boolean value) {
    this.hideBeforeAnswering = value;
  }
  
  public boolean getHideBeforeAnswering() {
    return this.hideBeforeAnswering;
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
    
    for (final Subject subject: subjects) {
      final FACheckbox cb = new FACheckbox(subject.name());
      cb.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          questionList.getCurrentQuestion().setSubject(subject, cb.isSelected());
        } 
      });
      subjectCheckboxes.put(subject, cb);
    }
    
    for (final Tag tag: tags) {
      final FACheckbox cb = new FACheckbox(tag.name());
      cb.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          questionList.getCurrentQuestion().setTag(tag, cb.isSelected());
        } 
      });
      tagCheckboxes.put(tag, cb);
    }
  }
  
  @Override
  public void layoutComponents(Dimension totalSize) {
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
    
    this.sizeComponent(this, totalSize);
  }

  @Override
  protected void observeListChange() {
    for (Subject subject: Subject.values()) {
      this.subjectCheckboxes.get(subject).setEnabled(this.questionList.isStarted());
      this.subjectCheckboxes.get(subject).setSelected(false);
    }
    for (Tag tag: Tag.values()) {
      this.tagCheckboxes.get(tag).setEnabled(this.questionList.isStarted());
      this.tagCheckboxes.get(tag).setSelected(false);
    }
    
    if (this.questionList.isStarted()) {
      Question q = this.questionList.getCurrentQuestion();
      ArrayList<Subject> subjects = q.getSubjects();
      ArrayList<Tag> tags = q.getTags();
      for (Subject subject: subjects) {
        this.subjectCheckboxes.get(subject).setSelected(true);
      }
      for (Tag tag: tags) {
        this.tagCheckboxes.get(tag).setSelected(true);
      }
    }
  }

  @Override
  protected void observeQuestionChange() {
    if (this.hideBeforeAnswering) {
      this.setVisible(this.questionList.isStarted() && this.questionState.isAnswered());
    }
  }

}
