package ui.questions.quiz;

import java.awt.Dimension;
import java.awt.Insets;
import models.Database;
import models.DatabaseIO;
import ui.DisplayWindow;
import ui.components.FACheckbox;
import ui.questions.QuestionListController;
import ui.questions.QuestionListDisplay;
import ui.questions.action.ActionController;
import ui.questions.action.ActionPanel;
import ui.questions.infobar.InfobarController;
import ui.questions.infobar.InfobarPanel;
import ui.questions.infobar.QuizInfobarPanel;
import ui.questions.sorters.QuestionListSorter;
import ui.questions.tagger.TaggerController;
import ui.questions.tagger.TaggerPanel;

public class QuizDisplay extends QuestionListDisplay {

  public static final int TOTAL_WIDTH = 900;
  public static final int TOTAL_HEIGHT = 800;
  public static final int TAGGER_PANEL_WIDTH = 150;
  public static final int BORDER_SIZE = 10;
  
  protected TaggerPanel taggerPanel;
  protected FACheckbox recordTimeCheckbox;

  public QuizDisplay(final QuestionListController ctrl) {
    super(ctrl);
  }
  
  @Override
  public void buildComponents() {
    super.buildComponents();
    
    this.recordTimeCheckbox = new FACheckbox("Record Time");
    
    InfobarController infoController = new InfobarController(this.ctrl);
    this.infoPanel = new QuizInfobarPanel(infoController);
    this.infoPanel.buildComponents();
    
    ActionController actionController = new ActionController(this.ctrl);
    this.actionPanel = new ActionPanel(actionController, QuestionListSorter.ALL_SORTERS) {
      @Override
      public void layoutComponents() {
        super.layoutComponents();
        this.add(recordTimeCheckbox);
      }
    };
    this.actionPanel.buildComponents();
    
    final TaggerController<QuestionListController> taggerController = new TaggerController<>(ctrl);
    this.taggerPanel = new TaggerPanel(taggerController) {
      
      @Override
      protected void syncFromController() {
        super.syncFromController();
        if (taggerController.questionListController.isInProgress()) {
          this.setVisible(taggerController.questionListController.isAnswered());
        }
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
        height - (INFO_PANEL_HEIGHT + ACTION_PANEL_HEIGHT + 4 * BORDER_SIZE));
    Dimension actionPanelSize = new Dimension(
        width - (TAGGER_PANEL_WIDTH + BORDER_SIZE),
        ACTION_PANEL_HEIGHT);
    Dimension infoPanelSize = new Dimension(
        width - (TAGGER_PANEL_WIDTH + BORDER_SIZE), 
        INFO_PANEL_HEIGHT);
    
    this.infoPanel.sizeComponents(infoPanelSize);
    this.taggerPanel.sizeComponents(taggerPanelSize);
    this.actionPanel.sizeComponents(actionPanelSize);
    this.questionPanel.sizeComponents(questionPanelSize);
  }
  
  @Override
  public void layoutComponents() {
    this.setLayout(null);
    
    this.taggerPanel.layoutComponents();
    this.infoPanel.layoutComponents();
    this.actionPanel.layoutComponents();
    this.questionPanel.layoutComponents();
    
    this.add(this.taggerPanel);
    this.add(this.infoPanel);
    this.add(this.actionPanel);
    this.add(this.questionPanel);
    
    Dimension size;
    
    Insets insets = this.getInsets();
    int tEdge = insets.top + BORDER_SIZE;
    int lEdge = insets.left + BORDER_SIZE;
    
    size = this.taggerPanel.getPreferredSize();
    this.taggerPanel.setBounds(lEdge, tEdge, size.width, size.height);
    lEdge += size.width + BORDER_SIZE;
    
    size = this.infoPanel.getPreferredSize();
    this.infoPanel.setBounds(lEdge, tEdge, size.width, size.height);
    tEdge += size.height + BORDER_SIZE;
    
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
