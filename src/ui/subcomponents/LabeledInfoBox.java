package ui.subcomponents;

import java.awt.Dimension;
import javax.swing.BoxLayout;
import ui.core.components.FABuildable;
import ui.core.components.FALabel;
import ui.core.components.FAPanel;

public class LabeledInfoBox extends FAPanel implements FABuildable {

  public static final int SPACING_SIZE = 20;
  
  protected FALabel labelLabel;
  protected FALabel valueLabel;
  protected final String labelText;
  protected final int maxWidth;
  protected final int maxHeight;
  protected final TextGenerator textGenerator;
  
  public LabeledInfoBox(TextGenerator valueTextGenerator, String maxString) {
    super();
    this.labelText = null;
    this.labelLabel = null;
    this.valueLabel = new FALabel(maxString, FALabel.NORMAL_LABEL);
    this.setFont(core.Constants.BASIC_FONT);
    this.maxWidth = SPACING_SIZE + this.valueLabel.getPreferredSize().width;
    this.maxHeight = this.valueLabel.getPreferredSize().height;
    this.textGenerator = valueTextGenerator;
  }
  
  public LabeledInfoBox(TextGenerator valueTextGenerator, String labelText, String maxString) {
    super();
    this.labelText = labelText;
    this.labelLabel = new FALabel(labelText, FALabel.NORMAL_LABEL);
    this.valueLabel = new FALabel(maxString, FALabel.NORMAL_LABEL);
    this.setFont(core.Constants.BASIC_FONT);
    this.textGenerator = valueTextGenerator;
    this.maxWidth = SPACING_SIZE + Math.max(
      this.valueLabel.getPreferredSize().width,
      this.labelLabel.getPreferredSize().width);
    this.maxHeight =
      this.valueLabel.getPreferredSize().height +
      this.labelLabel.getPreferredSize().height;
  }

  @Override
  public void buildComponents() {}
  
  public Dimension getComponentSize() {
    return new Dimension(this.maxWidth, this.maxHeight);
  }

  @Override
  public void layoutComponents(Dimension totalSize) {
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    
    if (this.labelLabel != null) {
      this.labelLabel.setAlignmentX(CENTER_ALIGNMENT);
      this.add(this.labelLabel);
    }
    
    this.valueLabel.setAlignmentX(CENTER_ALIGNMENT);
    this.add(this.valueLabel);
    
    this.sizeComponent(this, totalSize);
  }
  
  public void update() {
    this.valueLabel.setText(this.textGenerator.generateLabelText());
  }
  
  public static interface TextGenerator {
    public String generateLabelText();
  }
  
}
