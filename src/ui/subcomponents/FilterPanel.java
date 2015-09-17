package ui.subcomponents;

import engine.ListFilter;
import engine.ListSorter;
import static java.awt.Component.LEFT_ALIGNMENT;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import models.Source;
import models.Subject;
import models.Tag;
import java.util.Observable;
import models.AbstractQuestion;
import models.Question;
import ui.core.SubPanel;
import ui.core.components.FAButton;
import ui.core.components.FACheckbox;
import ui.core.components.FAComboBox;
import ui.core.components.FALabel;
import ui.questions.AppCtrlImpl;

public abstract class FilterPanel
<Q_TYPE extends AbstractQuestion,CTRL_TYPE extends AppCtrlImpl<Q_TYPE>>
extends SubPanel<Q_TYPE, CTRL_TYPE>
implements ActionListener {

  protected FAButton applyFiltersButton;
  protected FALabel topLabel;
  protected FALabel subjectsLabel;
  protected FALabel tagsLabel;
  protected FACheckbox noSubjectCheckbox;
  protected final Map<Subject, FACheckbox> subjectCheckboxes;
  protected final Map<Tag, FACheckbox> tagCheckboxes;
  protected boolean hideBeforeAnswering;
  protected FALabel sorter1Label;
  protected FAComboBox sorter1Combobox;
  protected FALabel sorter2Label;
  protected FAComboBox sorter2Combobox;

  protected static final String NO_SOURCE_FILTER = "None";

  public FilterPanel(CTRL_TYPE controller) {
    super(controller);
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
    Subject subjects[] = Subject.values();
    Tag tags[] = Tag.values();
    
    // Build labels.
    topLabel = new FALabel("Apply List Filters", FALabel.SECTION_LABEL);
    subjectsLabel = new FALabel("Subjects", FALabel.SUBSECTION_LABEL);
    tagsLabel = new FALabel("Tags", FALabel.SUBSECTION_LABEL);
    sorter1Label = new FALabel("Sort by 1", FALabel.SUBSECTION_LABEL);
    sorter2Label = new FALabel("Sort by 2", FALabel.SUBSECTION_LABEL);

    // Build checkbox components.
    this.noSubjectCheckbox = new FACheckbox("No subject");
    for (Subject subject: subjects) {
      subjectCheckboxes.put(subject, new FACheckbox(subject.name()));
    }
    for (Tag tag: tags) {
      tagCheckboxes.put(tag, new FACheckbox(tag.name()));
    }


    // Build filter button.
    this.applyFiltersButton = new FAButton("Apply Filters");
    this.applyFiltersButton.addActionListener(this);
  }

  /**
   *
   * @param totalSize
   */
  @Override
  public void layoutComponents(Dimension totalSize) {
    Subject subjects[] = Subject.values();
    Tag tags[] = Tag.values();

    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    this.add(this.topLabel);
    this.add(this.subjectsLabel);
    this.add(this.noSubjectCheckbox);
    for (Subject subject: subjects) {
      this.add(subjectCheckboxes.get(subject));
    }
    this.add(this.tagsLabel);
    for (Tag tag: tags) {
      this.add(tagCheckboxes.get(tag));
    }
    this.add(this.sorter1Label);
    this.add(this.sorter1Combobox);
    this.add(this.sorter2Label);
    this.add(this.sorter2Combobox);
    
    this.add(applyFiltersButton);

    this.sizeComponent(this, totalSize);
  }

  protected abstract ListSorter<Q_TYPE> generateSorter();

  protected abstract ListFilter<Q_TYPE> generateFilter();
  
  @Override
  public void actionPerformed(ActionEvent ev) {
    ListFilter<Q_TYPE> filter = this.generateFilter();
    ListSorter<Q_TYPE> sorter = this.generateSorter();
    this.ctrl.setList(filter, sorter);
  }

  @Override
  public void update(Observable o, Object args) {
    if (this.hideBeforeAnswering) {
      this.setVisible(!ctrl.isStarted() || ctrl.isAnswered());
    }
  }

}
