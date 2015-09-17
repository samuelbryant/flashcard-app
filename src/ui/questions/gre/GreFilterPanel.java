/*
 * File Overview: TODO
 */
package ui.questions.gre;

import engine.ListFilter;
import engine.ListSorter;
import static java.awt.Component.LEFT_ALIGNMENT;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import models.Question;
import models.Source;
import models.Subject;
import models.Tag;
import ui.core.components.FAComboBox;
import ui.core.components.FALabel;
import ui.subcomponents.FilterPanel;

/**
 *
 * @author author
 */
public class GreFilterPanel extends FilterPanel<Question, GreCtrl>{

  protected FALabel sourceLabel;
  protected FAComboBox sourceCombobox;
  
  public GreFilterPanel(GreCtrl controller) {
    super(controller);
  }
  
  public void buildComponents() {
    super.buildComponents();
    Source sources[] = Source.values();
    sourceLabel = new FALabel("Source", FALabel.SUBSECTION_LABEL);

    
    ArrayList<String> sourceStrings = new ArrayList<>();
    sourceStrings.add(FilterPanel.NO_SOURCE_FILTER);
    for (Source source: sources) {
      sourceStrings.add(source.toString());
    }
    this.sourceCombobox = new FAComboBox(sourceStrings.toArray());
    this.sourceCombobox.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ctrl.requestFocus();
      }
    });
    this.sourceCombobox.setAlignmentX(LEFT_ALIGNMENT);
    
    
    // Sorting combo boxes.
    this.sorter1Combobox = new FAComboBox(ListSorter.getAllGRESorters().keySet().toArray());
    this.sorter2Combobox = new FAComboBox(ListSorter.getAllGRESorters().keySet().toArray());
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
    this.add(this.sourceLabel);
    this.add(this.sourceCombobox);
    
    this.add(applyFiltersButton);

    this.sizeComponent(this, totalSize);
  }
  
  @Override
  protected ListSorter<Question> generateSorter() {
    ListSorter<Question> s1 = ListSorter.getAllGRESorters().get((String) this.sorter1Combobox.getSelectedItem());
    ListSorter<Question> s2 = ListSorter.getAllGRESorters().get((String) this.sorter2Combobox.getSelectedItem());

    return ListSorter.getCompositeSorter(s1, s2);
  }
  
  @Override
  protected ListFilter<Question> generateFilter() {
    ArrayList<ListFilter<Question>> filters = new ArrayList<>();

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

    // Build source filter.
    String sourceString = (String) this.sourceCombobox.getSelectedItem();
    if (sourceString.compareTo(FilterPanel.NO_SOURCE_FILTER) != 0) {
      filters.add(new ListFilter.SourceFilter(Source.valueOf(sourceString)));
    }

    return ListFilter.getCompositeFilter(filters);
  }
  
}
