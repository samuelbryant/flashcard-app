package ui.questions.tagger;

import engine.QuestionFilter;
import java.util.ArrayList;
import models.Question;
import models.Subject;
import models.Tag;
import ui.questions.QuestionListController;
import ui.questions.SubController;

public class TaggerController <T extends QuestionListController> extends SubController<T> {

  public TaggerController(T questionListController) {
    super(questionListController);
  }
  
  public void setQuestionTags(ArrayList<Tag> tags) {
    this.questionListController.getCurrentQuestion().setQuestionTags(tags);
  }
  
  public void setQuestionSubjects(ArrayList<Subject> subjects) {
    this.questionListController.getCurrentQuestion().setQuestionSubjects(subjects);
  }
  
  ArrayList<Subject> getQuestionSubjects() {
    return this.questionListController.getCurrentQuestion().getSubjects();
  }

  ArrayList<Tag> getQuestionTags() {
    return this.questionListController.getCurrentQuestion().getTags();
  }
  
  public class NoSubjectFilter extends QuestionFilter {
    @Override
    public boolean accept(Question q) {
      return (q.getSubjectsArray().length == 0);
    }
  }

}
