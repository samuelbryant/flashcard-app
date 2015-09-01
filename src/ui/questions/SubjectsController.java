package ui.questions;

import engine.QuestionFilter;
import models.Database;
import models.Question;
import java.util.List;
import java.util.ArrayList;

public class SubjectsController extends QuestionListController {

  public class NoSubjectFilter extends QuestionFilter {
    @Override
    public boolean accept(Question q) {
      return (q.getSubjects().length == 0);
    }
  }

  protected Integer index;
  protected List<Question> fullQuestionList;
  protected List<Question> currentQuestionList;
  protected boolean onlyShowNoSubject = false;
  protected final NoSubjectFilter noSubjectFilter = new NoSubjectFilter();

  public SubjectsController(Database db) {
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
