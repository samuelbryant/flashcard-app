/*
 * File Overview: TODO
 */
package stats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import models.DatabaseIO;
import models.Question;
import models.Subject;
import models.Tag;

public class SubjectBreakdown {
  
  static class SubjectStats {
    public final String subjectString;
    int totalNumber;
    int numberAnswered;
    int numberWrongOrHard;
    int numberRightAndOkay;
    
    public SubjectStats(Subject subj) {
      this.subjectString = subj.toString();
      totalNumber = 0;
      numberAnswered = 0;
      numberWrongOrHard = 0;
      numberRightAndOkay = 0;
    }
    
    public SubjectStats(String subjectString) {
      this.subjectString = subjectString;
      totalNumber = 0;
      numberAnswered = 0;
      numberWrongOrHard = 0;
      numberRightAndOkay = 0;
    }
    
    public static String getHeaderString() {
      return String.format("%-15s | %-15s | %-15s | %-15s", "Subject", "Answered/Total", "# Hard", "# Okay");
    }
    
    @Override
    public String toString() {
      return String.format("%-15s | %-15s | %-15s | %-15s",
          this.subjectString,
          String.format("%-3d/%-3d (%s)", 
              numberAnswered, totalNumber, Stats.getPercentString(numberAnswered, totalNumber)),
          String.format("%-3d (%s)",
              numberWrongOrHard, Stats.getPercentString(numberWrongOrHard, totalNumber)),
          String.format("%-3d (%s)",
              numberRightAndOkay, Stats.getPercentString(numberRightAndOkay, totalNumber)));
    }
    
    public void addQuestion(Question q) {
      this.totalNumber++;
      if (q.hasAnswered()) {
        numberAnswered++;
      }
      if (q.hasAnswered() && q.hasAnsweredWrong()) {
        this.numberWrongOrHard++;
      } else if (q.hasTag(Tag.HARD) || q.hasTag(Tag.WRONG)) {
        this.numberWrongOrHard++;
      } else if (q.hasAnswered()) {
        this.numberRightAndOkay++;
      }
    }
  }
  
  private Map<Subject, SubjectStats> stats = new HashMap<>();
  private SubjectStats statsOverall; 
  
  public SubjectBreakdown() {}
  
  public void compute() {
    stats = new HashMap<>();
    statsOverall = new SubjectStats("Overall");
    for (Subject subj: Subject.values()) {
      this.stats.put(subj, new SubjectStats(subj));
    }
    
    ArrayList<Question> list = DatabaseIO.getQuestionDatabaseIO().get().getQuestions();
    for (Question q: list) {
      statsOverall.addQuestion(q);
      for (Subject s: q.getSubjects()) {
        this.stats.get(s).addQuestion(q);
      }
    }
  }
  
  public void print() {
    System.out.println("Subject Statistics Breakdown\n");
    System.out.println(SubjectStats.getHeaderString());
    for (Subject s: Subject.values()) {
      System.out.println(this.stats.get(s).toString());
    }
    System.out.println(this.statsOverall.toString());
  }
  
  public static void main(String[] args) {
    SubjectBreakdown sb = new SubjectBreakdown();
    sb.compute();
    sb.print();
  }
  
}
