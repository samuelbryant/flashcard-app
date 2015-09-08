package models;

import java.util.HashMap;

/**
 *
 * @author sambryant
 */
public enum Source {

  /**
   *
   */
  GRE_1986,

  /**
   *
   */
  GRE_1991,

  /**
   *
   */
  GRE_1996,

  /**
   *
   */
  GRE_2001,

  /**
   *
   */
  GRE_2008,

  /**
   *
   */
  SAMPLE_1,

  /**
   *
   */
  SAMPLE_2,

  /**
   *
   */
  SAMPLE_3,

  /**
   *
   */
  PRACTICE_BOOK,

  /**
   *
   */
  TEXTBOOK,

  /**
   *
   */
  CUSTOM,

  /**
   *
   */
  OTHER;

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
}
