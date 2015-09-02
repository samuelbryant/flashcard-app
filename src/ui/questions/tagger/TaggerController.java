package ui.questions.tagger;

import engine.QuestionFilter;
import java.util.ArrayList;
import models.Database;
import models.Question;
import java.util.List;
import models.Subject;
import models.Tag;
import ui.questions.QuestionListController;

public class TaggerController extends QuestionListController {

  public class NoSubjectFilter extends QuestionFilter {
    @Override
    public boolean accept(Question q) {
      return (q.getSubjectsArray().length == 0);
    }
  }

  protected Integer index;
  protected List<Question> fullQuestionList;
  protected List<Question> currentQuestionList;
  protected boolean onlyShowNoSubject = false;
  protected final NoSubjectFilter noSubjectFilter = new NoSubjectFilter();

  public TaggerController(Database db) {
    super(db);
    this.fullQuestionList = db.getQuestionList();
    this.currentQuestionList = fullQuestionList;
    this.index = 0;
    this._currentQuestion = new QuestionListController.QuestionDisplay(
        this.currentQuestionList.get(this.index));
  }

  public void setOnlyShowNoSubject(boolean onlyShowNoSubject) {
    if (this.onlyShowNoSubject != onlyShowNoSubject) {
      this.index = 0;
      this.onlyShowNoSubject = onlyShowNoSubject;
      if (this.onlyShowNoSubject) {
        this.currentQuestionList = noSubjectFilter.filterList(fullQuestionList);
        this._updateCurrent();
      } else {
        this.currentQuestionList = fullQuestionList;
        this._updateCurrent();
      }
    }
  }
  
  @Override
  public void setQuestionList(List<Question> list) {
    super.setQuestionList(list);
    System.out.printf("Set question list to %d\n", list.size());
    this.fullQuestionList = list;
    this.currentQuestionList = list;
    this.index = 0;
    this._state = QuestionListController.State.IN_PROGRESS;
    this._updateCurrent();
  }

  public void setQuestionTags(ArrayList<Tag> tags) {
    this.getCurrentQuestion().setQuestionTags(tags);
  }
  
  public void setQuestionSubjects(ArrayList<Subject> subjects) {
    this.getCurrentQuestion().setQuestionSubjects(subjects);
  }
  
  ArrayList<Subject> getQuestionSubjects() {
    return this.getCurrentQuestion().getSubjects();
  }

  ArrayList<Tag> getQuestionTags() {
    return this.getCurrentQuestion().getTags();
  }
  
  public boolean isOnlyShowingNoSubjects() {
    return this.onlyShowNoSubject;
  }

  public int getTotalNumber() {
    return this.currentQuestionList.size();
  }

  public int getCurrentIndex() {
    return this.index;
  }

  @Override
  public void nextClick() {
    if (this.index < this.getTotalNumber() - 1) {
      this.index++;
      this._updateCurrent();
    }
  }

  @Override
  public void previousClick() {
    if (this.index > 0) {
      this.index--;
      this._updateCurrent();
    }
  }

  @Override
  public void shuffleClick() {
    System.out.printf("LOG: Shuffle click not allowed\n");
  }


  private void _updateCurrent() {
    this._currentQuestion = new QuestionListController.QuestionDisplay(
        this.currentQuestionList.get(this.index));
    this.setChanged();
    this.update();
  }

}
