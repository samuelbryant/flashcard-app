package stats;

public class Stats {
  
  public static int getPercent(int part, int whole) {
    return (int) ((((double) part)/whole)*100);
  }
  
  public static String getPercentString(int part, int whole) {
    return String.format("%2d%%", getPercent(part, whole));
  }
  
  public static void main(String[] args) {
    DateStats.main(args);
    SubjectBreakdown.main(args);
    
  }
  
}
