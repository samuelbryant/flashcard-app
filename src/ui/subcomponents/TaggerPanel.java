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
import models.Subject;
import models.Tag;
import core.Constants;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import models.AbstractQuestion;
import ui.core.components.FAButton;
import ui.core.components.FACheckbox;
import ui.questions.QuestionListController;
import ui.core.SubPanel;
import ui.questions.QuestionList;
import ui.questions.QuestionState;

public class TaggerPanel<
    Q_TYPE extends AbstractQuestion,
    STATE_TYPE extends QuestionState<STATE_TYPE, Q_TYPE, LIST_TYPE>,
    LIST_TYPE extends QuestionList<LIST_TYPE, Q_TYPE, STATE_TYPE>,
    CTRL_TYPE extends QuestionListController<Q_TYPE, STATE_TYPE, LIST_TYPE>>
    extends SubPanel<Q_TYPE, STATE_TYPE, LIST_TYPE, CTRL_TYPE>{

  protected FAButton onlyShowUnchecked;
  protected JLabel topLabel;
  protected JLabel subjectsLabel;
  protected JLabel tagsLabel;
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
    
    this.notesArea = new JTextArea();
    this.notesAreaButton = new FAButton("Done");
    this.notesAreaButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (questionList.isStarted()) {
          questionList.getCurrentQuestion().setNote(notesArea.getText());
        }
        TaggerPanel.this.questionListController.requestFocus();
      }
    });
    
    this.notesArea.setAlignmentX(LEFT_ALIGNMENT);
    this.notesPane = new JScrollPane(this.notesArea);
  }

  /**
   *
   * @param totalSize
   */
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

  /**
   *
   */
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
      Q_TYPE q = this.questionList.getCurrentQuestion();
      ArrayList<Subject> subjects = q.getSubjects();
      ArrayList<Tag> tags = q.getTags();
      for (Subject subject: subjects) {
        this.subjectCheckboxes.get(subject).setSelected(true);
      }
      for (Tag tag: tags) {
        this.tagCheckboxes.get(tag).setSelected(true);
      }
      
      
      this.notesArea.setText(this.questionList.getCurrentQuestion().getNote());
    }
  }

  /**
   *
   */
  @Override
  protected void observeQuestionChange() {
    if (this.hideBeforeAnswering) {
      this.setVisible(this.questionList.isStarted() && this.questionState.isAnswered());
    }
    this.notesArea.setVisible(this.questionList.isStarted() && this.questionState.isAnswered());
  }

}
