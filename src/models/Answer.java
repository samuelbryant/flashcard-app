package models;

/**
 *
 * @author sambryant
 */
public enum Answer {
  A, B, C, D, E, X;
  
  /**
   * Returns the flashcard version of the answer (the confidence level)
   * @return An int from 0-5 representing how confident an answer is.
   */
  public int getFlashcardAnswer() {
    return 5 - this.ordinal();
  }
}
