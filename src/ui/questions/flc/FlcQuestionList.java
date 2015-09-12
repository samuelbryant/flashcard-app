/*
 * File Overview: TODO
 */
package ui.questions.flc;

import models.Flashcard;
import models.QType;
import ui.questions.QuestionList;

public class FlcQuestionList extends QuestionList<
    FlcQuestionList,
    Flashcard,
    FlcQuestionState> {
  
  FlcQuestionList() {
    super(new engine.ListFilter.NullFilter<Flashcard>(),
         new engine.ListSorter.IdSorter<Flashcard>());
  }

  @Override
  protected QType getType() {
    return QType.FLASHCARD;
  }

  
}
