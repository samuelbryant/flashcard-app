package models;

import java.util.HashMap;

/**
 *
 * @author sambryant
 */
public enum Tag {

  /**
   *
   */
  WRONG,

  /**
   *
   */
  HARD,

  /**
   *
   */
  TIME,

  /**
   *
   */
  UNSURE,

  /**
   *
   */
  IMPORTANT,

  /**
   *
   */
  NEED_FORMULA,
  
  DUMB_MISTAKE,
  
  STARRED,
  
  COMPUTATION,
  HIDE;

  /**
   *
   * @return
   */
  public static HashMap<Tag, Boolean> getMap() {
    HashMap<Tag, Boolean> map = new HashMap<>();
    for (Tag src: Tag.values()) {
      map.put(src, false);
    }
    return map;
  }
}
