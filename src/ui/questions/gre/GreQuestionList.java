/*
 * File Overview: TODO
 */
package ui.questions.gre;

import models.Question;
import models.QType;
import ui.questions.QuestionList;

public class GreQuestionList extends QuestionList<
    GreQuestionList,
    Question,
    GreQuestionState> {
  
  GreQuestionList() {
    super(new engine.ListFilter.NullFilter<Question>(),
         new engine.ListSorter.IdSorter<Question>());
  }

  @Override
  protected QType getType() {
    return QType.GRE;
  }

  
}
