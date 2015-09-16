package stats;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import models.Answer;
import models.DatabaseIO;
import models.Question;
import models.Response;

public class DateStats {
  
  class DayStats {
    int questionsAnswered;
    int questionsWrong;
    int questionsRight;
    public DayStats() {
      this.questionsAnswered = 0;
      this.questionsRight = 0;
      this.questionsWrong = 0;
    }
    public void addResponse(boolean correct) {
      if (correct) questionsRight++;
      else questionsWrong++;
      questionsAnswered++;
    }
  }
  
  protected Map<Day, DayStats> stats;
  
  private void addQuestion(Question q) {
    List<Response> responses = q.getResponses();
    Answer correct = q.getAnswer();
    for (Response r: responses) {
      Day d = new Day(r.getDate());
      boolean isCorrect = correct == r.getSelectedAnswer();
      if (!this.stats.containsKey(d)) {
        this.stats.put(d, new DayStats());
      }
      this.stats.get(d).addResponse(isCorrect);
    }
  }
  
  public DateStats() {
    this.stats = new TreeMap<>();
    
    ArrayList<Question> list = DatabaseIO.getQuestionDatabaseIO().get().getQuestions();
    for (Question q: list) {
      this.addQuestion(q);
    }
  }
  
  public void printStats() {
    System.out.printf("%-10s %-10s %-10s %-10s %-10s\n", "Date", "Answered", "Right", "Wrong", "Percent");
    for (Map.Entry<Day, DayStats> entry: this.stats.entrySet()) {
      Day day = entry.getKey();
      DayStats stat = entry.getValue();
      System.out.printf("%-10s %3d%7s %3d%7s %3d%7s %4s\n",
           day,
           stat.questionsAnswered, "",
           stat.questionsRight, "",
           stat.questionsWrong, "",
           Stats.getPercentString(stat.questionsRight, stat.questionsAnswered));
    }
  }
  
  
  
  
  
  class Day extends Date {
    public Day(Date d) {
      super(d.getYear(), d.getMonth(), d.getDate());
    }
    
    @Override
    public int hashCode() {
      return getYear() * 10000 + getMonth() * 100 + getDate(); 
    }
    
    @Override
    public String toString() {
      return String.format("%-2d/%-2d", getMonth(), getDate());
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == null) {
        return false;
      }
      if (obj instanceof Date) {
        Date d = (Date) obj;
        return this.getYear() == d.getYear() && this.getMonth() == d.getMonth() && this.getDate() == d.getDate();
      }
      return false;
    }
  }
  
  public static void main(String[] args) {
    DateStats stats = new DateStats();
    stats.printStats();
  }
  
}
