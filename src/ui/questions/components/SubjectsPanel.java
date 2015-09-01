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
import models.Subject;
import ui.components.FACheckbox;
import ui.questions.QuestionListController;

public class SubjectsPanel extends JPanel {

  public class SubjectCheckbox extends FACheckbox implements Observer {

    private final QuestionListController _ctrl;
    private final Subject _subject;

    public SubjectCheckbox(Subject subject) {
      super(subject.name());
      this._subject = subject;
      this._ctrl = SubjectsPanel.this._ctrl;
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

  private final QuestionListController _ctrl;
  private final SubjectCheckbox[] _subjectCheckboxes;

  public SubjectsPanel(QuestionListController ctrl) {
    this._ctrl = ctrl;

    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    Subject[] subjects = Subject.values();
    _subjectCheckboxes = new SubjectCheckbox[subjects.length];

    for (int i = 0; i < subjects.length; i++) {
      _subjectCheckboxes[i] = new SubjectCheckbox(subjects[i]);
      this._ctrl.addObserver(_subjectCheckboxes[i]);
      this.add(_subjectCheckboxes[i]);
    }

    this.setFocusable(false);
  }

}
