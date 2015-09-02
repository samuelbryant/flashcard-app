/**
 * Question panel element designed to display/change subjects for each question.
 * Will be laid out vertically as checkboxes.
 * Key shortcuts for each checkbox.
 */
package ui.questions.subjects;

import javax.swing.BoxLayout;
import java.awt.event.ActionEvent;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JPanel;
import javax.swing.JLabel;
import models.Subject;
import models.Tag;
import ui.components.FACheckbox;

public class SubjectsPanel extends JPanel {

  public class ToggleShownCheckbox extends FACheckbox implements Observer {

    private final SubjectsController _ctrl;

    public ToggleShownCheckbox(String label) {
      super(label);
      this._ctrl = SubjectsPanel.this._ctrl;
      this._ctrl.addObserver(this);
      this.update(null, null);
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
      this._ctrl.setOnlyShowNoSubject(this.isSelected());
    }

    @Override
    public void update(Observable o, Object arg) {
      this.setSelected(this._ctrl.isOnlyShowingNoSubjects());
    }

  }

  public final class InfoLabel extends JLabel implements Observer {
    public InfoLabel() {
      super();
      SubjectsPanel.this._ctrl.addObserver(this);
      this.update(null, null);
    }
    @Override
    public void update(Observable o, Object arg) {
      int total = SubjectsPanel.this._ctrl.getTotalNumber();
      int index = SubjectsPanel.this._ctrl.getCurrentIndex();
      this.setText(String.format("Question %d/%d", index+1, total));
    }
  }

  public class SubjectCheckbox extends FACheckbox implements Observer {

    private final SubjectsController _ctrl;
    private final Subject _subject;

    public SubjectCheckbox(Subject subject) {
      super(subject.name());
      this._subject = subject;
      this._ctrl = SubjectsPanel.this._ctrl;
      this._ctrl.addObserver(this);
      this.update(null, null);
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
      this._ctrl.getCurrentQuestion().setSubject(this._subject, this.isSelected());
    }

    @Override
    public void update(Observable o, Object arg) {
      this.setSelected(this._ctrl.getCurrentQuestion().hasSubject(this._subject));
    }

  }
  
  public class TagCheckbox extends FACheckbox implements Observer {

    private final SubjectsController _ctrl;
    private final Tag _tag;

    public TagCheckbox(Tag tag) {
      super(tag.name());
      this._tag = tag;
      this._ctrl = SubjectsPanel.this._ctrl;
      this._ctrl.addObserver(this);
      this.update(null, null);
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
      this._ctrl.getCurrentQuestion().setTag(this._tag, this.isSelected());
    }

    @Override
    public void update(Observable o, Object arg) {
      this.setSelected(this._ctrl.getCurrentQuestion().hasTag(this._tag));
    }

  }

  private final SubjectsController _ctrl;

  public SubjectsPanel(SubjectsController ctrl) {
    this._ctrl = ctrl;

    Subject[] subjects = Subject.values();
    Tag[] tags = Tag.values();
    
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    InfoLabel label = new InfoLabel();
    this.add(label);

    this.add(new JLabel("Subjects"));
    this.add(new ToggleShownCheckbox("SHOW MISSING"));
    for (Subject subject: subjects) {
      this.add(new SubjectCheckbox(subject));
    }

    this.add(new JLabel("Tags"));
    for (Tag tag: tags) {
      this.add(new TagCheckbox(tag));
    }
    this.setFocusable(false);
  }

}
