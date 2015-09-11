/*
 * File Overview: TODO
 */
package imports;

import core.IO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import models.Database;
import models.DatabaseIO;
import models.Flashcard;

/**
 *
 * @author author
 */
public class FlashcardMaker extends JPanel implements MouseListener {
  
  Dimension forcedDim = new Dimension(800, 800);
  double resizeRatio;
  
  // Dimension dim = new Dimension(5100, 6600);
  BufferedImage img;
  Integer saveIndex = 0;
  File[] images;
  Integer imageIndex = -1;
  
  int verticalSplit = -1;
  ArrayList<Integer> horizontalSplits = new ArrayList<>();
  
  public FlashcardMaker(File[] images) {
    this.images = images;
    this.setFocusable(true);
    this.addMouseListener(this);
    this.nextImage();
  }
  
  private void nextImage() {
    this.imageIndex++;
    String fileName = this.images[imageIndex].getAbsolutePath();
    img = IO.loadImageOrDie(fileName);
    // Dimension dim = new Dimension(img.getWidth(), img.getHeight());
    Dimension dim = forcedDim;
    this.setPreferredSize(dim);
    this.setSize(dim);
    resizeRatio = ((double) forcedDim.width) / img.getWidth();
    this.verticalSplit = -1;
    this.horizontalSplits.clear();
    this.repaint();
  }
  
  @Override
  public void paintComponent(Graphics gr) {
    int w = this.getWidth();
    int h = this.getHeight();

    gr.setColor(Color.WHITE);
    gr.fillRect(0, 0, w, h);

    int width = img.getWidth();
    int height = img.getHeight();

    gr.drawImage(img, 0, 0, 
        (int) (width*resizeRatio), (int) (height*resizeRatio), null);
    // gr.drawImage(img, 0, 0, null);
    
    if (this.verticalSplit != -1) {
      gr.setColor(Color.RED);
      int x = (int) (this.verticalSplit*resizeRatio);
      gr.drawLine(x, 0, x, h);
    }
    for (Integer i : this.horizontalSplits) {
      int y = (int) (i * resizeRatio);
      gr.drawLine(0, y, w, y);
    }
    
    
  }
  
  @Override
  public void mouseClicked(MouseEvent e) {
    int x = e.getX();
    int y = e.getY();
    
    //double ratio = ((double) img.getWidth()) / forcedDim.width;
    //x = (int) (resizeRatio*x);
    //y = (int) (atio*y);
    
    x = (int) (x/resizeRatio);
    y = (int) (y/resizeRatio);
    
    System.out.printf("Clicked: (%d, %d) (%d, %d)\n", x, y, this.getWidth(), this.getHeight());
    if (this.verticalSplit == -1) {
      verticalSplit = x;
    } else {
      if (!this.horizontalSplits.isEmpty()) {
        int last = this.horizontalSplits.get(this.horizontalSplits.size() - 1);
        int diff = y - last;
        if (diff < 5) {
          System.err.printf("Ignoring click within 5 of last click\n");
        }
        
      }
      this.horizontalSplits.add(y);
    }
    this.repaint();
  }

  @Override
  public void mousePressed(MouseEvent e) {
  }

  @Override
  public void mouseReleased(MouseEvent e) {}

  @Override
  public void mouseEntered(MouseEvent e) {}

  @Override
  public void mouseExited(MouseEvent e) {}
  
  public void saveResults() throws IOException {
    int fcNum = this.horizontalSplits.size();
    // this.horizontalSplits.add(img.getHeight());
    
    DatabaseIO<Flashcard> io = DatabaseIO.getFlashcardDatabaseIO();
    Database<Flashcard> db = io.get();
    
    int mid = this.verticalSplit;
    int right = img.getWidth();
    int top = this.horizontalSplits.get(0);
    for (int i=1; i<this.horizontalSplits.size(); i++) {
      int bot = this.horizontalSplits.get(i);
      
      BufferedImage qImg = img.getSubimage(0, top, mid, bot-top);
      BufferedImage aImg = img.getSubimage(mid, top, right-mid, bot-top);
      
      db.addQuestionToSession(new Flashcard(qImg, aImg));
//      
//      
//      String name = "imports/flashcards/images";
//      String qName = String.format("%s/%d-Q.png", name, saveIndex);
//      String aName = String.format("%s/%d-A.png", name, saveIndex);
//      
//      saveIndex++;
//      
//      
//      ImageIO.write(qImg, "png", new File(qName));
//      ImageIO.write(aImg, "png", new File(aName));
//      
//      top = bot;
//     // img.getSubimage(i, i, i, i)
    }
    
    io.save();
    FlashcardMaker.this.nextImage();
  }
  
  class Window extends JFrame {
    public Window() {
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      JButton saveButton = new JButton("Save");
      saveButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          try {
            FlashcardMaker.this.saveResults();
          } catch (IOException ex) {
            Logger.getLogger(FlashcardMaker.class.getName()).log(Level.SEVERE, null, ex);
          }
        }
      });
      
      //JScrollPane pane = new JScrollPane(FlashcardMaker.this);
      //pane.setPreferredSize(new Dimension(800, 800));
      
      
      this.getContentPane().add(FlashcardMaker.this, BorderLayout.CENTER);
      this.getContentPane().add(saveButton, BorderLayout.SOUTH);
      
      this.pack();
      this.setVisible(true);
    }
  }
  
  void display() {
    new Window();
  }
  
  public static void main(String[] args) {
    File srcDir = new File("imports/flashcards/real");
    File[] images = srcDir.listFiles(new FilenameFilter() {
      @Override
      public boolean accept(File dir, String name) {
        return name.endsWith(".png");
      }
    });

    FlashcardMaker fm = new FlashcardMaker(images);
    fm.display();
    fm.repaint();
    
  }
  
}
