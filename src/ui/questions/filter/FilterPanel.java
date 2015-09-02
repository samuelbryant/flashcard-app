package ui.questions.filter;

import java.awt.Component;
import java.awt.Dimension;
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

public class FilterPanel <T extends QuestionListController> extends SubPanel<T, FilterController<T>>  {
  
  protected FAButton applyFiltersButton;
  protected JLabel topLabel;
  protected JLabel subjectsLabel;
  protected JLabel tagsLabel;
  protected final Map<Subject, JCheckBox> subjectCheckboxes;
  protected final Map<Tag, JCheckBox> tagCheckboxes;
  
  public FilterPanel(FilterController<T> filterController) {
    super(filterController);
    subjectCheckboxes = new TreeMap<>();
    tagCheckboxes = new TreeMap<>();
  }

  @Override
  public void buildComponents() {
    Subject subjects[] = Subject.values();
    Tag tags[] = Tag.values();
    
    // Build labels.
    topLabel = new JLabel("Apply List Filters");
    topLabel.setFont(Constants.SECTION_FONT);
    
    subjectsLabel = new JLabel("Subjects");
    subjectsLabel.setFont(Constants.SUBSECTION_FONT);
    
    tagsLabel = new JLabel("Tags");
    tagsLabel.setFont(Constants.SUBSECTION_FONT);
    
    // Build checkbox components.
    for (Subject subject: subjects) {
      subjectCheckboxes.put(subject, new JCheckBox(subject.name()));
    }
    for (Tag tag: tags) {
      tagCheckboxes.put(tag, new JCheckBox(tag.name()));
    }

    // Build filter button.
    this.applyFiltersButton = new FAButton("Apply Filters");
    this.applyFiltersButton.addActionListener(this.updateControllerListener);
  }

  @Override
  public void layoutComponents() {
    Subject subjects[] = Subject.values();
    Tag tags[] = Tag.values();
    
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    this.add(this.topLabel);
    this.add(this.subjectsLabel);
    for (Subject subject: subjects) {
      this.add(subjectCheckboxes.get(subject));
    }
    this.add(this.tagsLabel);
    for (Tag tag: tags) {
      this.add(tagCheckboxes.get(tag));
    }
    this.add(applyFiltersButton);
    
    this.setAlignmentY(Component.TOP_ALIGNMENT);
  }

  @Override
  public void sizeComponents(Dimension totalDimension) {
    this.setSize(totalDimension);
    this.setPreferredSize(totalDimension);
  }
  
  @Override
  protected void syncToController() {
    for (Subject subject: Subject.values()) {
      this.componentController.subjectFilters.put(subject, this.subjectCheckboxes.get(subject).isSelected());
    }
    for (Tag tag: Tag.values()) {
      this.componentController.tagFilters.put(tag, this.tagCheckboxes.get(tag).isSelected());
    }
    this.componentController.applyFilters();
  }
  
  @Override
  protected void syncFromController() {
    for (Subject subject: Subject.values()) {
      this.subjectCheckboxes.get(subject).setSelected(this.componentController.subjectFilters.get(subject));
    }
    for (Tag tag: Tag.values()) {
      this.tagCheckboxes.get(tag).setSelected(this.componentController.tagFilters.get(tag));
    }
  }

}
