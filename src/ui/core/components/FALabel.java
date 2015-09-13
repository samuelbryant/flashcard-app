package ui.core.components;

import core.Constants;
import javax.swing.JLabel;

public class FALabel extends JLabel {
  public static final int SECTION_LABEL = 1;
  public static final int SUBSECTION_LABEL = 2;
  public static final int NORMAL_LABEL = 3;
  
  public FALabel(String text, int type) {
    super(text);
    
    switch (type) {
      case NORMAL_LABEL:
        this.setFont(Constants.BASIC_FONT);
        this.setForeground(Constants.NORMAL_LABEL_COLOR);
        break;
      case SUBSECTION_LABEL:
        this.setFont(Constants.SUBSECTION_FONT);
        this.setForeground(Constants.SUBSECTION_LABEL_COLOR);
        break;
      case SECTION_LABEL:
        this.setFont(Constants.SECTION_FONT);
        this.setForeground(Constants.SECTION_LABEL_COLOR);
        break;
      default:
        throw new IllegalArgumentException("Unknown fa label type: " + type);
    }
  }
  
}
