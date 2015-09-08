package ui.components;

import java.awt.Dimension;

/**
 *
 * @author sambryant
 */
public interface FABuildable {

  /**
   *
   */
  public void buildComponents();

  /**
   *
   * @param totalSize
   */
  public void layoutComponents(Dimension totalSize);

}
