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
import javax.swing.JLabel;
import models.Source;
import models.Subject;
import models.Tag;
import core.Constants;
import java.util.Observable;
import models.AbstractQuestion;
import models.Question;
import ui.core.SubPanel;
import ui.core.components.FAButton;
import ui.core.components.FACheckbox;
import ui.questions.AppCtrlImpl;

public class FilterPanel
<Q_TYPE extends AbstractQuestion,CTRL_TYPE extends AppCtrlImpl<Q_TYPE>>
extends SubPanel<Q_TYPE, CTRL_TYPE>
implements ActionListener {

  protected FAButton applyFiltersButton;
  protected JLabel topLabel;
  protected JLabel subjectsLabel;
  protected JLabel tagsLabel;
  protected JLabel sourceLabel;
  protected FACheckbox noSubjectCheckbox;
  protected final Map<Subject, FACheckbox> subjectCheckboxes;
  protected final Map<Tag, FACheckbox> tagCheckboxes;
  protected JComboBox sourceCombobox;
  protected boolean hideBeforeAnswering;
  protected JLabel sorter1Label;
  protected JComboBox sorter1Combobox;
  protected JLabel sorter2Label;
  protected JComboBox sorter2Combobox;

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
    Source sources[] = Source.values();

    // Build labels.
    topLabel = new JLabel("Apply List Filters");
    topLabel.setFont(Constants.SECTION_FONT);

    subjectsLabel = new JLabel("Subjects");
    subjectsLabel.setFont(Constants.SUBSECTION_FONT);

    tagsLabel = new JLabel("Tags");
    tagsLabel.setFont(Constants.SUBSECTION_FONT);

    sourceLabel = new JLabel("Source");
    sourceLabel.setFont(Constants.SUBSECTION_FONT);

    sorter1Label = new JLabel("Sort by 1");
    sorter1Label.setFont(Constants.SUBSECTION_FONT);

    sorter2Label = new JLabel("Sort by 2");
    sorter2Label.setFont(Constants.SUBSECTION_FONT);

    // Build checkbox components.
    this.noSubjectCheckbox = new FACheckbox("No subject");
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
        ctrl.requestFocus();
      }
    });
    this.sourceCombobox.setAlignmentX(LEFT_ALIGNMENT);

    // Sorting combo boxes.
    this.sorter1Combobox = new JComboBox(ListSorter.getAllGRESorters().keySet().toArray());
    this.sorter2Combobox = new JComboBox(ListSorter.getAllGRESorters().keySet().toArray());
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
    this.add(this.sorter1Label);
    this.add(this.sorter1Combobox);
    this.add(this.sorter2Label);
    this.add(this.sorter2Combobox);


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
    this.add(this.sourceLabel);
    this.add(this.sourceCombobox);
    this.add(applyFiltersButton);

    // this.setAlignmentY(Component.TOP_ALIGNMENT);

    this.sizeComponent(this, totalSize);
    this.sizeComponent(this.sourceCombobox, new Dimension(100, 50));
  }

  private ListSorter _generateSorter() {
    ListSorter s1 = ListSorter.getAllGRESorters().get((String) this.sorter1Combobox.getSelectedItem());
    ListSorter s2 = ListSorter.getAllGRESorters().get((String) this.sorter2Combobox.getSelectedItem());

    return ListSorter.getCompositeSorter(s1, s2);
  }

  private ListFilter _generateFilter() {
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

  @Override
  public void actionPerformed(ActionEvent ev) {
    ListFilter filter = this._generateFilter();
    ListSorter sorter = this._generateSorter();
    this.ctrl.setList(filter, sorter);
  }

  @Override
  public void update(Observable o, Object args) {
    if (this.hideBeforeAnswering) {
      this.setVisible(!ctrl.isStarted() || ctrl.isAnswered());
    }
  }

}
