package ui.questions.filter;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import models.Source;
import models.Subject;
import models.Tag;
import ui.Constants;
import ui.components.FAButton;
import ui.components.FACheckbox;
import ui.questions.QuestionListController;
import ui.questions.SubPanel;

public class FilterPanel <T extends QuestionListController> extends SubPanel<T, FilterController<T>> {
  
  protected FAButton applyFiltersButton;
  protected JLabel topLabel;
  protected JLabel subjectsLabel;
  protected JLabel tagsLabel;
  protected JCheckBox noSubjectCheckbox;
  protected final Map<Subject, FACheckbox> subjectCheckboxes;
  protected final Map<Tag, FACheckbox> tagCheckboxes;
  protected JComboBox sourceCombobox;
  
  protected static final String NO_SOURCE_FILTER = "None";
  
  public FilterPanel(FilterController<T> filterController) {
    super(filterController);
    subjectCheckboxes = new TreeMap<>();
    tagCheckboxes = new TreeMap<>();
  }

  @Override
  public void buildComponents() {
    Subject subjects[] = Subject.values();
    Tag tags[] = Tag.values();
    Source sources[] = Source.values();
    
    // Build labels.
    topLabel = new JLabel("Apply List Filters");
    topLabel.setFont(Constants.SECTION_FONT);
    
    subjectsLabel = new JLabel("Subjects");
    subjectsLabel.setFont(Constants.SUBSECTION_FONT);
    
    tagsLabel = new JLabel("Tags");
    tagsLabel.setFont(Constants.SUBSECTION_FONT);
    
    // Build checkbox components.
    this.noSubjectCheckbox = new JCheckBox("No subject");
    for (Subject subject: subjects) {
      subjectCheckboxes.put(subject, new FACheckbox(subject.name()));
    }
    for (Tag tag: tags) {
      tagCheckboxes.put(tag, new FACheckbox(tag.name()));
    }
    
    ArrayList<String> sourceStrings = new ArrayList<>();
    sourceStrings.add(FilterPanel.NO_SOURCE_FILTER);
    for (Source source: sources) {
      sourceStrings.add(source.toString());
    }
    this.sourceCombobox = new javax.swing.JComboBox(sourceStrings.toArray());
    this.sourceCombobox.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        questionListController.requestFocus();
      }
    });
    this.sourceCombobox.setAlignmentX(LEFT_ALIGNMENT);

    // Build filter button.
    this.applyFiltersButton = new FAButton("Apply Filters");
    this.applyFiltersButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        System.out.println("Button clicked");
        syncToController();
        componentController.applyFilters();
      }
      
    });
  }

  @Override
  public void layoutComponents() {
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
    this.add(this.sourceCombobox);
    this.add(applyFiltersButton);
    
    this.setAlignmentY(Component.TOP_ALIGNMENT);
  }

  @Override
  public void sizeComponents(Dimension totalDimension) {
    this.setSize(totalDimension);
    this.setPreferredSize(totalDimension);
    this.sourceCombobox.setMaximumSize(new Dimension(100, 50));
    this.sourceCombobox.setPreferredSize(new Dimension(100, 50));
  }
  
  @Override
  protected void syncToController() {
    this.componentController.noSubjectFilter = this.noSubjectCheckbox.isSelected();
    for (Subject subject: Subject.values()) {
      this.componentController.subjectFilters.put(subject, this.subjectCheckboxes.get(subject).isSelected());
    }
    for (Tag tag: Tag.values()) {
      this.componentController.tagFilters.put(tag, this.tagCheckboxes.get(tag).isSelected());
    }
    String sourceFilter = (String) this.sourceCombobox.getSelectedItem();
    if (sourceFilter.compareTo(FilterPanel.NO_SOURCE_FILTER) == 0) {
      this.componentController.sourceFilter = null;
    } else {
      this.componentController.sourceFilter = Source.valueOf(sourceFilter);
      System.out.println("Set source to " + this.componentController.sourceFilter);
    }
  }
  
  @Override
  protected void syncFromController() {
    for (Subject subject: Subject.values()) {
      this.subjectCheckboxes.get(subject).setSelected(this.componentController.subjectFilters.get(subject));
    }
    for (Tag tag: Tag.values()) {
      this.tagCheckboxes.get(tag).setSelected(this.componentController.tagFilters.get(tag));
    }
    if (this.componentController.sourceFilter == null) {
      this.sourceCombobox.setSelectedItem(FilterPanel.NO_SOURCE_FILTER);
    } else {
      this.sourceCombobox.setSelectedItem(this.componentController.sourceFilter.toString());
    }
  }

}
