package ui.questions;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import models.Answer;
import models.Database;
import models.DatabaseIO;
import ui.DisplayWindow;
import ui.Display;
import ui.ImageDisplay;
import ui.components.FAActionButton;
import ui.components.FAPanel;

public class QuestionListDisplay extends Display<QuestionListController> {

  protected final ImageDisplay questionPanel;
  protected final ActionButtonPanel actionPanel;

  public QuestionListDisplay(final QuestionListController ctrl, int totalWidth, int totalHeight) {
    super(ctrl, totalWidth, totalHeight);
    this.actionPanel = new ActionButtonPanel();
    this.questionPanel = new ImageDisplay(true) {
      @Override
      public BufferedImage generateDisplayImage() {
        if (QuestionListDisplay.this.ctrl.getState() == QuestionListController.State.NOT_STARTED) {
          BufferedImage img = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
          Graphics2D gr = img.createGraphics();
          gr.setColor(Color.BLACK);
          gr.drawString("Questions List Not Started", 40, 40);
          return img;
        } else {
          return ctrl.getCurrentQuestion().getImage();
        }
      }
    };
  }

  @Override
  protected void setupMenuBar() {
    JMenu file = new JMenu("File");
    JMenuItem menuItem = new JMenuItem("Save");
    menuItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent ev) {
        QuestionListDisplay.this.ctrl.saveToDatabase();
      }
      });
    file.add(menuItem);
    this.menuBar.add(file);
  }

  @Override
  protected void setupGUI() {
    Dimension questionImageSize = new Dimension(totalWidth, totalHeight - 100);
    Dimension actionPanelSize = new Dimension(totalWidth, 100);

    sizeComponent(questionPanel, questionImageSize);
    sizeComponent(actionPanel, actionPanelSize);

    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    this.add(questionPanel);
    this.add(actionPanel);

    this.repaint();
  }

  public class ActionButtonPanel extends FAPanel {
    
    public class AnswerButton extends FAActionButton implements Observer {

      private final Answer _answer;

      public AnswerButton(Answer answer) {
        super(answer.name());
        this._answer = answer;
        QuestionListDisplay.this.ctrl.addObserver(this);
      }

      @Override
      public void actionPerformed(ActionEvent ev) {
        QuestionListDisplay.this.ctrl.answerQuestion(this._answer);
      }

      @Override
      public void update(Observable o, Object arg) {
        this.setBackground(Color.WHITE);

        if (QuestionListDisplay.this.ctrl.isAnswered()) {
          
          System.out.printf("Answered: %s, %s\n", 
              QuestionListDisplay.this.ctrl.getSelectedAnswer(),
              QuestionListDisplay.this.ctrl.getCorrectAnswer());
          boolean isSelected = 
              this._answer == QuestionListDisplay.this.ctrl.getSelectedAnswer();
          boolean isCorrect = 
              this._answer == QuestionListDisplay.this.ctrl.getCorrectAnswer();

          if (isSelected && !isCorrect) {
            this.setBackground(Color.RED);
          } else if (isCorrect) {
            this.setBackground(Color.GREEN);
          }
        }
      }

    }

    private ActionButtonPanel() {
      super();
      AnswerButton answerA = new AnswerButton(Answer.A);
      AnswerButton answerB = new AnswerButton(Answer.B);
      AnswerButton answerC = new AnswerButton(Answer.C);
      AnswerButton answerD = new AnswerButton(Answer.D);
      AnswerButton answerE = new AnswerButton(Answer.E);
      
      FAActionButton nextB = new FAActionButton("Next") {
        @Override
        public void actionPerformed(ActionEvent e) {
          QuestionListDisplay.this.ctrl.nextClick();
        }
      };
      FAActionButton prevB = new FAActionButton("Prev") {
        @Override
        public void actionPerformed(ActionEvent e) {
          QuestionListDisplay.this.ctrl.previousClick();
        }
      };
      FAActionButton shufB = new FAActionButton("Shuffle") {
        @Override
        public void actionPerformed(ActionEvent e) {
          QuestionListDisplay.this.ctrl.shuffleClick();
        }
      };

      this.setLayout(new FlowLayout());
      
      this.add(answerA);
      this.add(answerB);
      this.add(answerC);
      this.add(answerD);
      this.add(answerE);
      this.add(nextB);
      this.add(prevB);
      this.add(shufB);

      // Add key shortcuts for button presses.
      QuestionListDisplay.this.ctrl.addKeyAction(KeyEvent.VK_1, answerA);
      QuestionListDisplay.this.ctrl.addKeyAction(KeyEvent.VK_2, answerB);
      QuestionListDisplay.this.ctrl.addKeyAction(KeyEvent.VK_3, answerC);
      QuestionListDisplay.this.ctrl.addKeyAction(KeyEvent.VK_4, answerD);
      QuestionListDisplay.this.ctrl.addKeyAction(KeyEvent.VK_5, answerE);
      QuestionListDisplay.this.ctrl.addKeyAction(37, prevB);
      QuestionListDisplay.this.ctrl.addKeyAction(39, nextB);
    }
  }

  public static void main(String[] args) {
    // Load/initialize models.
    Database db = DatabaseIO.loadDatabase();

    // Load/initialize controller/display.
    QuestionListController ctrl = new QuestionListController(db, db.getQuestionList());
    QuestionListDisplay display = new QuestionListDisplay(ctrl, 700, 600);

    // Bring it all home.
    DisplayWindow window = new DisplayWindow();
    window.showDisplay(display);
  }
}
