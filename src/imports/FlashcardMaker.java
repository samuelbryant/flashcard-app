/*
 * File Overview: TODO
 */
package imports;

import core.IO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
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
  
  static int CLOSE_THRESHOLD = 20;
  
  // Dimension dim = new Dimension(5100, 6600);
  BufferedImage img;
  Integer saveIndex = 0;
  File[] images;
  Integer imageIndex = -1;
  
  int verticalLeft = -1;
  int verticalRight = -1;
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
    this.verticalLeft = -1;
    this.verticalRight = -1;
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
    
    this.drawVertical(gr);
    
    for (Integer i : this.horizontalSplits) {
      int y = (int) (i * resizeRatio);
      gr.drawLine(0, y, w, y);
    }
    
  }
  
  private void drawVertical(Graphics gr) {
    if (this.verticalLeft != -1 && this.verticalRight != -1) {
      int left = (int) (verticalLeft*resizeRatio);
      int right = (int) (verticalRight*resizeRatio);
      gr.setColor(Color.RED);
      gr.fillRect(left, 0, right-left, this.getHeight());
    } else if (this.verticalLeft != -1) {
      gr.setColor(Color.RED);
      gr.drawLine((int) (verticalLeft*resizeRatio), 0, (int) (verticalLeft*resizeRatio), this.getHeight());
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
    
    this.addClick(x, y);
    
    this.repaint();
  }
  
  private void undoClick() {
    if (!this.horizontalSplits.isEmpty()) {
      this.horizontalSplits.remove(this.horizontalSplits.size() - 1);
    } else if (verticalRight != -1) {
      this.verticalRight = -1;
    } else {
      this.verticalRight = -1;
    }
  }
  
  private void addClick(int x, int y) {
    if (verticalLeft == -1) {
      verticalLeft = x;
    } else if (verticalRight == -1) {
      verticalRight = x;
    } else {
      if (!this.horizontalSplits.isEmpty()) {
        int last = this.horizontalSplits.get(this.horizontalSplits.size() - 1);
        int diff = y - last;
        if (diff <= CLOSE_THRESHOLD) {
          System.err.printf("Detected close clicks!\n");
        }
        
      }
      this.horizontalSplits.add(y);
      Collections.sort(this.horizontalSplits);
    }
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
    
    int midL = this.verticalLeft;
    int midR = this.verticalRight;
    int right = img.getWidth();
    int top = this.horizontalSplits.get(0);
    for (int i=1; i<this.horizontalSplits.size(); i++) {
      int bot = this.horizontalSplits.get(i);
      
      if (Math.abs(bot - top) <= CLOSE_THRESHOLD) {
        System.out.printf("Skipping close clicks (%d)\n", bot-top);
        top = bot;
        continue;
      }
      
      BufferedImage qImgPart = img.getSubimage(0, top, midL, bot-top);
      BufferedImage aImgPart = img.getSubimage(midR, top, right-midR, bot-top);
      
      int qWidth = qImgPart.getWidth();
      int qHeight = qImgPart.getHeight();
      int aWidth = aImgPart.getWidth();
      int aHeight = aImgPart.getHeight();
      
      int tWidth = Math.max(qWidth, aWidth);
      int tHeight = qHeight + aHeight;
      BufferedImage qImg = new BufferedImage(tWidth, tHeight, BufferedImage.TYPE_4BYTE_ABGR);
      BufferedImage aImg = new BufferedImage(tWidth, tHeight, BufferedImage.TYPE_4BYTE_ABGR);
      
      Graphics2D qGr = qImg.createGraphics();
      Graphics2D aGr = aImg.createGraphics();
      qGr.setColor(Color.WHITE);
      aGr.setColor(Color.WHITE);
      qGr.fillRect(0, 0, tWidth, tHeight);
      aGr.fillRect(0, 0, tWidth, tHeight);
      
      int wOff1 = (tWidth - qWidth)/2;
      int wOff2 = (tWidth - aWidth)/2;
      int hOff = (tHeight - aHeight);
      
      qGr.drawImage(qImgPart, wOff1, 0, this);
      aGr.drawImage(qImgPart, wOff1, 0, this);
      aGr.drawImage(aImgPart, wOff2, hOff, this);
      
      
      
      db.addQuestionToSession(new Flashcard(qImg, aImg));
      
      top = bot;
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
      
      JButton undoButton = new JButton("Undo");
      undoButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          undoClick();
        }
      });
      
      JPanel buttonPanel = new JPanel();
      buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
      buttonPanel.add(saveButton);
      buttonPanel.add(undoButton);
      //JScrollPane pane = new JScrollPane(FlashcardMaker.this);
      //pane.setPreferredSize(new Dimension(800, 800));
      
      
      this.getContentPane().add(FlashcardMaker.this, BorderLayout.CENTER);
      this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
      
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
