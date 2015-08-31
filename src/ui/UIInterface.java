/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import ui.questions.QuestionListController;
import ui.questions.QuestionListDisplay;
import flashcard.question.Question;
import java.util.Iterator;
import java.util.List;
import ui.questions.QuestionIterator;
import ui.questions.QuestionListIterator;

/**
 * Class that serves as entry point for test code to use UI stuff.
 * Essentially the glue for all the various UI components.
 * @author author
 */
public class UIInterface {
  
  public static void displayQuestions(List<Question> questions) {
    MainWindow window = new MainWindow(500, 500);
    QuestionIterator iter = new QuestionListIterator(questions);
    QuestionListController ctrl = new QuestionListController(iter);
    QuestionListDisplay display = new QuestionListDisplay(ctrl, 500, 500);
    window.setDisplay(display);
    
    window.setVisible(true);
    
    display.setFocusable(true);
  }
  
}
