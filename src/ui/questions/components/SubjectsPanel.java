/**
 * Question panel element designed to display/change subjects for each question.
 * Will be laid out vertically as checkboxes.
 * Key shortcuts for each checkbox.
 */
package ui.questions.components;

import javax.swing.BoxLayout;
import java.awt.event.ActionEvent;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JPanel;
import javax.swing.JLabel;
import models.Subject;
import ui.components.FACheckbox;
import ui.questions.SubjectsController;

public class SubjectsPanel extends JPanel {

  public class ToggleShownCheckbox extends FACheckbox implements Observer {

    private final SubjectsController _ctrl;

    public ToggleShownCheckbox(String label) {
      super(label);
      this._ctrl = SubjectsPanel.this._ctrl;
      this._ctrl.addObserver(this);
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

  public class InfoLabel extends JLabel implements Observer {
    public InfoLabel() {
      super();
      SubjectsPanel.this._ctrl.addObserver(this);
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

  private final SubjectsController _ctrl;
  private final SubjectCheckbox[] _subjectCheckboxes;

  public SubjectsPanel(SubjectsController ctrl) {
    this._ctrl = ctrl;

    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    InfoLabel label = new InfoLabel();
    this.add(label);

    ToggleShownCheckbox toggle = new ToggleShownCheckbox("Only show no subjects");
    this.add(toggle);

    Subject[] subjects = Subject.values();
    _subjectCheckboxes = new SubjectCheckbox[subjects.length];

    for (int i = 0; i < subjects.length; i++) {
      _subjectCheckboxes[i] = new SubjectCheckbox(subjects[i]);
      this.add(_subjectCheckboxes[i]);
    }

    this.setFocusable(false);
  }

}
