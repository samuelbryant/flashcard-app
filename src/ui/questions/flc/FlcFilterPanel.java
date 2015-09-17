package ui.questions.flc;

import engine.ListFilter;
import engine.ListSorter;
import static java.awt.Component.LEFT_ALIGNMENT;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import models.Flashcard;
import models.Subject;
import models.Tag;
import ui.core.components.FAComboBox;
import ui.subcomponents.FilterPanel;

class FlcFilterPanel extends FilterPanel<Flashcard, FlcCtrl> {

  FlcFilterPanel(FlcCtrl ctrl) {
    super(ctrl);
  }
  
  @Override
  public void buildComponents() {
    super.buildComponents();    
    
    // Sorting combo boxes.
    this.sorter1Combobox = new FAComboBox(ListSorter.getAllFLCSorters().keySet().toArray());
    this.sorter2Combobox = new FAComboBox(ListSorter.getAllFLCSorters().keySet().toArray());
    this.sorter1Combobox.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ctrl.requestFocus();
      }
    });
    this.sorter2Combobox.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ctrl.requestFocus();
      }
    });
    this.sorter1Combobox.setAlignmentX(LEFT_ALIGNMENT);
    this.sorter2Combobox.setAlignmentX(LEFT_ALIGNMENT);
    this.sorter1Combobox.setSelectedItem(ListSorter.DEFAULT_1_STRING);
    this.sorter2Combobox.setSelectedItem(ListSorter.DEFAULT_2_STRING);
  }
  
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
  
  @Override
  protected ListSorter<Flashcard> generateSorter() {
    ListSorter<Flashcard> s1 = ListSorter.getAllFLCSorters().get((String) this.sorter1Combobox.getSelectedItem());
    ListSorter<Flashcard> s2 = ListSorter.getAllFLCSorters().get((String) this.sorter2Combobox.getSelectedItem());

    return ListSorter.getCompositeSorter(s1, s2);
  }
  
  @Override
  protected ListFilter<Flashcard> generateFilter() {
    ArrayList<ListFilter<Flashcard>> filters = new ArrayList<>();

    // Build subject filter.
    ArrayList<Subject> subjectFilters = new ArrayList<>();
    for (Subject subject: Subject.values()) {
      if (this.subjectCheckboxes.get(subject).isSelected()) {
        subjectFilters.add(subject);
      }
    }
    boolean noSubjectFilter = this.noSubjectCheckbox.isSelected();
    if (noSubjectFilter || !subjectFilters.isEmpty()) {
      filters.add(new ListFilter.SubjectFilter(subjectFilters, noSubjectFilter));
    }

    // Build tag filter.
    ArrayList<Tag> tagFilters = new ArrayList<>();
    for (Tag tag: Tag.values()) {
      if (this.tagCheckboxes.get(tag).isSelected()) {
        tagFilters.add(tag);
      }
    }
    if (!tagFilters.isEmpty()) {
      filters.add(new ListFilter.TagFilter(tagFilters, false));
    }

    return ListFilter.getCompositeFilter(filters);
  }
}
