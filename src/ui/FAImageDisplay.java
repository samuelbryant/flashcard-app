/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author author
 */
public abstract class FAImageDisplay extends JPanel {
    
  public abstract BufferedImage generateDisplayImage();
  
  public void paintComponent(Graphics gr) {
    int w = this.getWidth();
    int h = this.getHeight();
    
    gr.setColor(Color.WHITE);
    gr.fillRect(0, 0, w, h);
    
    BufferedImage img = generateDisplayImage();
    int width = img.getWidth();
    int height = img.getHeight();
    if (width < w && height < h) {
      gr.drawImage(img, 0, 0, null);
    } 
    
    else {
      double heightRatio = ((double) height) / h;
      double widthRatio = ((double) width) / w;
      double maxRatio = Math.max(widthRatio, heightRatio);
      
      width = (int) (width / maxRatio);
      height = (int) (height / maxRatio);
      gr.drawImage(img, 0, 0, width, height, null);
    }
    
    System.out.printf("Image details: %d, %d\n", img.getHeight(), img.getWidth());
  }
  
}
