package models;

import java.util.HashMap;

public enum Subject {
  // Major subject tags.
  MECHANICS, EM, WAVES, RELATIVITY, QM, THERMO, SPECIAL, LAB_METHODS, OTHER,

  // Sub-subject tags
  FLUIDS, ROTATIONAL, CIRCUITS, OPTICS, ATOMIC, MATH;

  public static HashMap<Subject, Boolean> getMap() {
    HashMap<Subject, Boolean> map = new HashMap<>();
    for (Subject src: Subject.values()) {
      map.put(src, false);
    }
    return map;
  }
}
