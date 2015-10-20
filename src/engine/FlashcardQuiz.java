package engine;

import java.util.ArrayList;
import models.Flashcard;
import models.Tag;

public class FlashcardQuiz extends ListQuizSorter<Flashcard> {

  FlashcardQuiz() {}

  @Override
  protected int getQuestionWeight(Flashcard q) {
    if (q.hasTag(Tag.HIDE)) {
      return 0;
    }
    int weight = 3;
    if (q.hasTag(Tag.STARRED)) {
      weight += 6;
    }
    if (q.hasAnswered()) {
      weight = 6 - q.getLastResponseValue().getFlashcardAnswer();
      weight *= weight;
    }
    return weight;
  }

  @Override
  protected void preprocess(ArrayList<Flashcard> list) {}
  
}
