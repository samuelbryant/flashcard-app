package engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import models.AbstractQuestion;
import models.Question;
import models.Source;
import models.Subject;
import models.Tag;

/**
 * Class used to filter lists of Questions to create new lists.
 * @param <T>
 */
public abstract class ListFilter <T extends AbstractQuestion> {

  /**
   *
   */
  public enum Relationship {

    /**
     *
     */
    AND,

    /**
     *
     */
    OR;
  }

  /**
   * Determines if given question should be filtered out of list.
   * @param q Question to filter.
   * @return Boolean indicating if question should be included.
   */
  public abstract boolean accept(T q);

  /**
   *
   * @param filters
   * @return
   */
  public static <K extends AbstractQuestion> ListFilter<K> getCompositeFilter(ArrayList<ListFilter<K>> filters) {
    return getCompositeFilter(filters, Relationship.AND);
  }


  /**
   * Creates filter that is the intersection of two filters.
   * @param filter1 First filter to consider.
   * @param filter2 Second filter to consider.
   * @return A ListFilter that requires both filter arguments to pass.
   */
  public static <K extends AbstractQuestion> ListFilter getCompositeFilter(final ListFilter<K> filter1, final ListFilter<K> filter2) {
    return getCompositeFilter(filter1, filter2, Relationship.AND);
  }

  /**
   * Creates filter that is a composite of multiple filters.
   * The resulting filter can either be the union or the intersection of the input filters.
   * @param filter1 First filter to consider.
   * @param filter2 Second filter to consider.
   * @param rel Relationship to use when creating composite filter (AND or OR).
   * @return A ListFilter that is a composite of the given filters.
   */
  public static <K extends AbstractQuestion> ListFilter getCompositeFilter(
      final ListFilter<K> filter1, final ListFilter<K> filter2, final Relationship rel) {
    return new ListFilter<K>() {
      @Override
      public boolean accept(K q) {
        if (rel == Relationship.AND) {
          return filter1.accept(q) && filter2.accept(q);
        } else {
          return filter1.accept(q) || filter2.accept(q);
        }
      }
    };
  }

  /**
   * Creates filter that is a composite of multiple filters.
   * The resulting filter can either be the union or the intersection of the input filters.
   * @param filters Set of filters to consider.
   * @param rel Relationship to use when creating composite filter (AND or OR).
   * @return A ListFilter that is a composite of the given filters.
   */
  public static <K extends AbstractQuestion> ListFilter getCompositeFilter(
      final List<ListFilter<K>> filters, final Relationship rel) {
    return new ListFilter<K>() {
      @Override
      public boolean accept(K q) {
        if (rel == Relationship.AND) {
          for (ListFilter f: filters) {
            if (!f.accept(q)) {
              return false;
            }
          }
          return true;
        } else {
          for (ListFilter f: filters) {
            if (f.accept(q)) {
              return true;
            }
          }
          return false;
        }
      }
    };
  }

  /**
   * ListFilter instance which returns all values.
   * @param <K>
   */
  public static class NullFilter <K extends AbstractQuestion> extends ListFilter<K> {
    @Override
    public boolean accept(K q) {
      return true;
    }
  };
  
  public static class WrongOrHardFIlter extends ListFilter<Question> {
    @Override
    public boolean accept(Question q) {
      return q.isWrongOrHard();
    }
  }

  /**
   * ListFilter class which filters Question lists by the Source value of the Question instances.
   */
  public static class SourceFilter extends ListFilter<Question> {

    private final Map<Source, Boolean> sources;
    private final boolean includeNoSources;

    /**
     * Constructor for Question list SourceFilter.
     * @param src Source value required by this filter.
     */
    public SourceFilter(Source src) {
      this.sources = Source.getMap();
      this.sources.put(src, true);
      this.includeNoSources = false;
    }

    /**
     * Constructor for Question list SourceFilter.
     * @param sources Set of Source values allowed by this filter.
     * @param includeNoSources Whether or not filter accepts Question instances with no Source.
     */
    public SourceFilter(List<Source> sources, boolean includeNoSources) {
      this.sources = Source.getMap();
      if (sources != null) {
        for (Source src: sources) {
          this.sources.put(src, true);
        }
      }
      this.includeNoSources = includeNoSources;
    }

    @Override
    public boolean accept(Question q) {
      Source source = q.getSource();
      if (source == null) {
        return includeNoSources;
      } else {
        return sources.get(source);
      }
    }
  }

  /**
   * ListFilter class which filters Question lists by the Tag values of the Question instances.
   */
  public static class TagFilter<K extends AbstractQuestion> extends ListFilter<K> {

    private final Map<Tag, Boolean> tags;
    private final boolean includeNoTags;

    /**
     * Constructor for Question list TagFilter.
     * @param tag Tag value required by this filter.
     */
    public TagFilter(Tag tag) {
      this.tags = Tag.getMap();
      this.tags.put(tag, true);
      this.includeNoTags = false;
    }

    /**
     * Constructor for Question list TagFilter.
     * @param tags Set of Tag values allowed by this filter.
     * @param includeNoTags Whether or not filter accepts Question instances with no Tag values.
     */
    public TagFilter(List<Tag> tags, boolean includeNoTags) {
      this.tags = Tag.getMap();
      if (tags != null) {
        for (Tag src: tags) {
          this.tags.put(src, true);
        }
      }
      this.includeNoTags = includeNoTags;
    }

    @Override
    public boolean accept(K q) {
      ArrayList<Tag> questionTags = q.getTags();
      if (questionTags.isEmpty()) {
        return includeNoTags;
      } else {
        return _arrayInBoolMap(this.tags, questionTags);
      }
    }
  }

  /**
   * ListFilter class which filters Question lists by the Subject values of the Question instances.
   */
  public static class SubjectFilter<K extends AbstractQuestion> extends ListFilter<K> {

    private final Map<Subject, Boolean> subjects;
    private final boolean includeNoSubjects;

    /**
     * Constructor for Question list SubjectFilter.
     * @param subject Subject value required by this filter.
     */
    public SubjectFilter(Subject subject) {
      this.subjects = Subject.getMap();
      this.subjects.put(subject, true);
      this.includeNoSubjects = false;
    }

    /**
     * Constructor for Question list SubjectFilter.
     * @param subjects Set of Subject values allowed by this filter.
     * @param includeNoSubjects Whether or not filter accepts Question instances with no Subject values.
     */
    public SubjectFilter(List<Subject> subjects, boolean includeNoSubjects) {
      this.subjects = Subject.getMap();
      if (subjects != null) {
        for (Subject src: subjects) {
          this.subjects.put(src, true);
        }
      }
      this.includeNoSubjects = includeNoSubjects;
    }

    @Override
    public boolean accept(K q) {
      ArrayList<Subject> questionSubjects = q.getSubjects();
      if (questionSubjects.isEmpty()) {
        return includeNoSubjects;
      } else {
        return _arrayInBoolMap(this.subjects, questionSubjects);
      }
    }
  }

  /**
   * Checks if any member of an ArrayList is contained in a boolean map. 
   * This is a helper method used in ListFilters.
   * @param <T> The type of objects in the ArrayList/Map.
   * @param map A map from <T> to boolean values to check.
   * @param search A list of <T> values to check if any members are in {@map}. 
   * @return True if any member of {search} is true in map {map}.
   */
  private static <T> boolean _arrayInBoolMap(Map<T, Boolean> map, ArrayList<T> search) {
    for (T t: search) {
      if (map.get(t)) {
        return true;
      }
    }
    return false;
  }

}
