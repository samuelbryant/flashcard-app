package ui.questions.quiz;

import java.awt.Dimension;
import java.awt.Insets;
import models.Database;
import models.DatabaseIO;
import ui.DisplayWindow;
import ui.questions.QuestionListController;
import ui.questions.QuestionListDisplay;
import ui.questions.tagger.TaggerController;
import ui.questions.tagger.TaggerPanel;

public class QuizDisplay extends QuestionListDisplay {

  public static final int TOTAL_WIDTH = 900;
  public static final int TOTAL_HEIGHT = 800;
  public static final int TAGGER_PANEL_WIDTH = 150;
  public static final int BORDER_SIZE = 10;
  
  protected TaggerPanel taggerPanel;

  public QuizDisplay(final QuestionListController ctrl) {
    super(ctrl);
  }
  
  @Override
  public void buildComponents() {
    super.buildComponents();
    
    final TaggerController<QuestionListController> taggerController = new TaggerController<>(ctrl);
    this.taggerPanel = new TaggerPanel(taggerController) {
      
      @Override
      protected void syncFromController() {
        super.syncFromController();
        this.setVisible(taggerController.questionListController.isAnswered());
      }
  
    };
    this.taggerPanel.buildComponents();
  }

  @Override
  public void sizeComponents(Dimension totalSize) {
    int width = totalSize.width;
    int height = totalSize.height;
    
    Dimension taggerPanelSize = new Dimension(
        TAGGER_PANEL_WIDTH, 
        height - 2 * BORDER_SIZE);
    Dimension questionPanelSize = new Dimension(
        width - (TAGGER_PANEL_WIDTH + BORDER_SIZE),
        height - (ACTION_PANEL_HEIGHT + 3 * BORDER_SIZE));
    Dimension actionPanelSize = new Dimension(
        width - (TAGGER_PANEL_WIDTH + BORDER_SIZE),
        ACTION_PANEL_HEIGHT);
   
    
    this.taggerPanel.sizeComponents(taggerPanelSize);
    this.actionPanel.sizeComponents(actionPanelSize);
    this.questionPanel.sizeComponents(questionPanelSize);
  }
  
  @Override
  public void layoutComponents() {
    this.setLayout(null);
    
    this.taggerPanel.layoutComponents();
    this.actionPanel.layoutComponents();
    this.questionPanel.layoutComponents();
    
    this.add(this.taggerPanel);
    this.add(this.actionPanel);
    this.add(this.questionPanel);
    
    Dimension size;
    
    Insets insets = this.getInsets();
    int tEdge = insets.top + BORDER_SIZE;
    int lEdge = insets.left + BORDER_SIZE;
    
    size = this.taggerPanel.getPreferredSize();
    this.taggerPanel.setBounds(lEdge, tEdge, size.width, size.height);
    lEdge += size.width + BORDER_SIZE;
    
    size = this.actionPanel.getPreferredSize();
    this.actionPanel.setBounds(lEdge, tEdge, size.width, size.height);
    tEdge += size.height + BORDER_SIZE;
    
    size = this.questionPanel.getPreferredSize();
    this.questionPanel.setBounds(lEdge, tEdge, size.width, size.height);
  }
  
  public static void main(String[] args) {
    // Load/initialize models.
    Database db = DatabaseIO.loadDatabase();

    // Load/initialize controller/display.
    QuestionListController ctrl = new QuizController(db);
    QuestionListDisplay display = new QuizDisplay(ctrl);
    
    // Bring it all home.
    DisplayWindow window = new DisplayWindow(TOTAL_WIDTH, TOTAL_HEIGHT);
    window.showDisplay(display);
  }

}
