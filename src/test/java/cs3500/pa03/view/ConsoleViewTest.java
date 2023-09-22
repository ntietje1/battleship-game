package cs3500.pa03.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cs3500.pa03.TestUtils;
import cs3500.pa03.controller.ManualDataGatherer;
import cs3500.pa03.model.board.BattleshipBoard;
import java.io.InputStreamReader;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

/**
 * represents tests for the ConsoleView class
 */
class ConsoleViewTest {

  /**
   * tests the getManualDataGatherer method
   */
  @Test
  void testGetManualDataGatherer() {
    Readable readable = new InputStreamReader(System.in);
    View view = new ConsoleView(System.out, readable);
    assertEquals(view.getManualDataGatherer().getClass(), ManualDataGatherer.class);
  }


  /**
   * tests the promptUserForInput method
   */
  @Test
  void testPromptUserForInput() {
    Appendable output = new StringBuilder();
    Readable readable = new StringReader("");
    ConsoleView view = new ConsoleView(output, readable);
    view.promptUserForInput("joe");
    assertTrue(output.toString().contains("joe"));
  }

  /**
   * tests the showErrorMessage method
   */
  @Test
  void testShowErrorMessage() {
    Appendable output = new StringBuilder();
    Readable readable = new StringReader("");
    ConsoleView view = new ConsoleView(output, readable);
    view.showErrorMessage("error message :o");
    assertTrue(output.toString().contains("error message :o"));
  }

  /**
   * tests the displayBoardstate method
   */
  @Test
  void testDisplayBoardstate() {
    Appendable output = new StringBuilder();
    Readable readable = new StringReader("");
    ConsoleView view = new ConsoleView(output, readable);
    BattleshipBoard board1 = new BattleshipBoard(3, 3);
    BattleshipBoard board2 = new BattleshipBoard(3, 3);
    view.displayBoardstate(board1, "board1", board2, "board2");
    String refString = "\n" + " [board1]\n" + "   0  1  2 \n" + "0  ~  ~  ~ \n" + "1  ~  ~  ~ \n"
        + "2  ~  ~  ~ \n" + "\n" + " [board2]\n" + "   0  1  2 \n" + "0  ~  ~  ~ \n"
        + "1  ~  ~  ~ \n" + "2  ~  ~  ~ \n" + "\n";
    assertEquals(refString, TestUtils.removeColors(output.toString()));

  }
}