package models;

import java.util.HashMap;

public enum Source {
  GRE_1986("1986"), 
  GRE_1991("1991"),
  GRE_1996("1996"),
  GRE_2001("2001"),
  GRE_2008("2008"),
  SAMPLE_1("PT 1"),
  SAMPLE_2("PT 2"),
  SAMPLE_3("PT 3"),
  PRACTICE_BOOK("Book"),
  TEXTBOOK("Textbook"),
  CUSTOM("Custom"),
  OTHER("Other");

  public final String displayName;
  
  Source(String displayName) {
    this.displayName = displayName;
  }
  
  /**
   *
   * @return
   */
  public static HashMap<Source, Boolean> getMap() {
    HashMap<Source, Boolean> map = new HashMap<>();
    for (Source src: Source.values()) {
      map.put(src, false);
    }
    return map;
  }
  
  public String toDisplayName() {
    return displayName;
  }
}
