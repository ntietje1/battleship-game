package cs3500.pa03.view;

import cs3500.pa03.controller.ManualDataGatherer;
import cs3500.pa03.model.board.BattleshipBoard;
import java.io.IOException;

/**
 * represents a way for the user to view the program
 */
public interface View {

  /**
   * gets this manualDataGatherer
   *
   * @return this manualDataGatherer
   */
  public ManualDataGatherer getManualDataGatherer();

  /**
   * displays the given prompt for the user to input something
   *
   * @param prompt prompt for the user to input something
   */
  public void promptUserForInput(String prompt);

  /**
   * displays the given error message
   *
   * @param response the error message to display
   */
  public void showErrorMessage(String response);

  /**
   * displays both given boards and labels them with the given labels
   *
   * @param board1 first board to display
   * @param label1 label for the first board
   * @param board2 second board to display
   * @param label2 label for the second board
   */
  public void displayBoardstate(BattleshipBoard board1, String label1, BattleshipBoard board2,
                                String label2);
}
