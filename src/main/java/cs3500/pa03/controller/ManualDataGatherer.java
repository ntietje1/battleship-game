package cs3500.pa03.controller;

import cs3500.pa03.model.board.BattleshipBoard;
import cs3500.pa03.model.board.Posn;
import cs3500.pa03.view.ConsoleView;
import cs3500.pa03.view.View;
import java.util.Scanner;


/**
 * represents a way to gather data from a user
 */
public class ManualDataGatherer {
  View view;
  Scanner scanner;

  /**
   * constructor for a ManualDataGatherer
   *
   * @param view    a way for the user to view the program
   * @param scanner a way to gather input from the user
   */
  public ManualDataGatherer(View view, Scanner scanner) {
    this.view = view;
    this.scanner = scanner;
    //this.view.setManualDataGatherer(this);
  }

  /**
   * asks the user for a number by displaying the given label
   *
   * @param label the label to display
   * @return the number that the user inputs
   */
  public int askForNumber(String label) {
    this.view.promptUserForInput(label + " > ");
    return this.scanner.nextInt();
  }

  /**
   * Prompt the user for a coordinate
   *
   * @return coordinate entered by the user
   */
  public Posn askForPosn(String label) {
    this.view.promptUserForInput(label + " > ");
    int x = this.scanner.nextInt();
    int y = this.scanner.nextInt();
    return new Posn(x, y);
  }

  /**
   * displays a response to the user
   *
   * @param response the response to display the user
   */
  public void respond(String response) {
    this.view.showErrorMessage(response);
  }

  /**
   * prompts the user to enter a volley
   *
   * @param name        name of player firing
   * @param shotsToFire the number of shots the user will fire
   * @param enemy       the enemy's board
   * @param friendly    your board
   */
  public void askForVolley(String name, int shotsToFire, BattleshipBoard enemy,
                           BattleshipBoard friendly) {
    this.view.displayBoardstate(enemy, "Enemy Board", friendly, "My Board");
    this.view.promptUserForInput(name + ", time to fire! You have " + shotsToFire + " shots \n"
        + "Please enter the coordinates you would like to attack in the format: x y\n");
  }

  /**
   * asks the user to input a board size
   *
   * @return the board size
   */
  public Posn askForBoardSize() {
    this.view.promptUserForInput("Please enter a valid height and width.\n");
    return this.askForPosn("Setup");
  }
}
