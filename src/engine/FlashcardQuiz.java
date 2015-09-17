package engine;

import models.Flashcard;

public class FlashcardQuiz extends ListQuizSorter<Flashcard> {

  FlashcardQuiz() {}

  @Override
  protected int getQuestionWeight(Flashcard q) {
    int weight = 3;
    if (q.hasAnswered()) {
      weight = 6 - q.getLastResponseValue().getFlashcardAnswer();
    }
    return weight;
  }
  
}
