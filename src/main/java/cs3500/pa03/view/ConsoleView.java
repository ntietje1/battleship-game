package cs3500.pa03.view;

import cs3500.pa03.controller.ManualDataGatherer;
import cs3500.pa03.model.board.BattleshipBoard;
import java.io.IOException;
import java.util.Scanner;

/**
 * represents a way to view the program in the console
 */
public class ConsoleView implements View {
  private final Appendable appendable;
  private final Readable readable;
  private Scanner scanner;
  private ManualDataGatherer manualDataGatherer;

  /**
   * constructor for a consoleView
   *
   * @param appendable to write to
   * @param readable   to read from
   */
  public ConsoleView(Appendable appendable, Readable readable) {
    this.appendable = appendable;
    this.readable = readable;
    this.scanner = new Scanner(this.readable);
    this.manualDataGatherer = new ManualDataGatherer(this, this.scanner);
  }

  /**
   * gets this manualDataGatherer
   *
   * @return this manualDataGatherer
   */
  public ManualDataGatherer getManualDataGatherer() {
    return this.manualDataGatherer;
  }

  /**
   * displays the given string
   *
   * @param content the string to display
   */
  private void display(String content) {
    try {
      appendable.append(content);
    } catch (IOException e) {
      System.err.println("Error writing " + content + " to console" + e.getMessage());
    }
  }

  /**
   * displays the given prompt for the user to input something
   *
   * @param prompt prompt for the user to input something
   */
  public void promptUserForInput(String prompt) {
    this.display(prompt);
  }

  /**
   * displays the given error message
   *
   * @param response the error message to display
   */
  public void showErrorMessage(String response) {
    this.display(response);
  }

  /**
   * displays both given boards and labels them with the given labels
   *
   * @param board1 first board to display
   * @param label1 label for the first board
   * @param board2 second board to display
   * @param label2 label for the second board
   */
  public void displayBoardstate(BattleshipBoard board1, String label1, BattleshipBoard board2,
                                String label2) {
    label1 = "[" + label1 + "]";
    label2 = "[" + label2 + "]";
    int label1SpaceCount = (board1.getWidth() * 3) / 2 - label1.length() / 2 + 1;
    int label2SpaceCount = (board1.getWidth() * 3) / 2 - label2.length() / 2 + 1;
    display("\n" + " ".repeat(label1SpaceCount) + label1 + "\n" + board1 + "\n"
        + " ".repeat(label2SpaceCount) + label2 + "\n" + board2 + "\n");
  }
}
