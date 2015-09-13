package ui.subcomponents;

import static java.awt.Component.TOP_ALIGNMENT;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.BoxLayout;
import models.Subject;
import models.Tag;
import core.Constants;
import java.util.Observable;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import models.AbstractQuestion;
import ui.core.components.FAButton;
import ui.core.components.FACheckbox;
import ui.core.SubPanel;
import ui.core.components.FALabel;
import ui.questions.AppCtrlImpl;

public class TaggerPanel 
<Q_TYPE extends AbstractQuestion, CTRL_TYPE extends AppCtrlImpl<Q_TYPE>>
extends SubPanel<Q_TYPE, CTRL_TYPE> {

  protected FAButton onlyShowUnchecked;
  protected FALabel topLabel;
  protected FALabel subjectsLabel;
  protected FALabel tagsLabel;
  protected final Map<Subject, FACheckbox> subjectCheckboxes;
  protected final Map<Tag, FACheckbox> tagCheckboxes;
  protected boolean hideBeforeAnswering;
  
  protected JTextArea notesArea;
  protected FAButton notesAreaButton;
  protected JScrollPane notesPane;

  public TaggerPanel(CTRL_TYPE ctrl) {
    super(ctrl);
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

    topLabel = new FALabel("Add Labels", FALabel.SECTION_LABEL);
    subjectsLabel = new FALabel("Subjects", FALabel.SUBSECTION_LABEL);
    tagsLabel = new FALabel("Tags", FALabel.SUBSECTION_LABEL);

    for (final Subject subject: subjects) {
      final FACheckbox cb = new FACheckbox(subject.name());
      cb.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          ctrl.getCurrentQuestion().setSubject(subject, cb.isSelected());
        } 
      });
      subjectCheckboxes.put(subject, cb);
    }

    for (final Tag tag: tags) {
      final FACheckbox cb = new FACheckbox(tag.name());
      cb.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          ctrl.getCurrentQuestion().setTag(tag, cb.isSelected());
        } 
      });
      tagCheckboxes.put(tag, cb);
    }
    
    this.notesArea = new JTextArea();
    this.notesAreaButton = new FAButton("Done");
    this.notesAreaButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (ctrl.isStarted()) {
          ctrl.getCurrentQuestion().setNote(notesArea.getText());
        }
        TaggerPanel.this.ctrl.requestFocus();
      }
    });
    
    this.notesArea.setAlignmentX(LEFT_ALIGNMENT);
    this.notesPane = new JScrollPane(this.notesArea);
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
    
    this.notesPane.setAlignmentX(LEFT_ALIGNMENT);
    this.add(notesPane);
    this.add(notesAreaButton);

    this.setAlignmentY(TOP_ALIGNMENT);

    this.sizeComponent(this, totalSize);
  }

  @Override
  public void update(Observable o, Object args) {
    
    // Hide tagging panel before answering if set, otherwise, hide when there is no question.
    if (this.hideBeforeAnswering) {
      this.setVisible(this.ctrl.isAnswered());
    } else {
      this.setVisible(this.ctrl.isStarted());
    }
    
    // Always hide note panel before answering.
    this.notesArea.setVisible(this.ctrl.isAnswered());
    
    // Disable tagging boxes when there is no question.
    for (Subject subject: Subject.values()) {
      this.subjectCheckboxes.get(subject).setEnabled(this.ctrl.isStarted());
      this.subjectCheckboxes.get(subject).setSelected(false);
    }
    for (Tag tag: Tag.values()) {
      this.tagCheckboxes.get(tag).setEnabled(this.ctrl.isStarted());
      this.tagCheckboxes.get(tag).setSelected(false);
    }

    // When there is a question, set values of boxes from question.
    if (this.ctrl.isStarted()) {
      Q_TYPE q = this.ctrl.getCurrentQuestion();
      ArrayList<Subject> subjects = q.getSubjects();
      ArrayList<Tag> tags = q.getTags();
      for (Subject subject: subjects) {
        this.subjectCheckboxes.get(subject).setSelected(true);
      }
      for (Tag tag: tags) {
        this.tagCheckboxes.get(tag).setSelected(true);
      }
      
      this.notesArea.setText(this.ctrl.getCurrentQuestion().getNote());
    }
  }

}
