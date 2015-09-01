/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.questions;

import models.Database;
import models.DatabaseIO;
import ui.MainWindow;

/**
 *
 * @author author
 */
public class TestDisplayAllQuestions {

  public static void main(String[] args) {
    Database db = DatabaseIO.loadDatabase();

    MainWindow window = new MainWindow(500, 500);
    QuestionIterator iter = new QuestionListIterator(db.getQuestionListCopy());
    QuestionListController ctrl = new QuestionListController(iter);
    QuestionListDisplay display = new QuestionListDisplay(ctrl, 500, 500);
    window.setDisplay(display);

    display.setupGUI();

    window.setVisible(true);
  }

}
