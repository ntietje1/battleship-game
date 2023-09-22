package cs3500.pa03.controller;

import cs3500.pa03.model.board.Posn;
import cs3500.pa03.model.board.ShipType;
import cs3500.pa03.model.player.GameResult;
import cs3500.pa03.model.player.Player;
import cs3500.pa03.view.ConsoleView;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is responsible for handling the input from the user
 * as well as interactions between the players
 */
public class GameController implements Controller {
  private final ConsoleView view;
  Player player1;
  Player player2;
  ManualDataGatherer manualDataGatherer;
  int height;
  int width;
  int fleetSize;
  Map<ShipType, Integer> specifications;

  /**
   * Creates a new GameController
   *
   * @param view where to display information to the users or manual players
   */
  public GameController(ConsoleView view, Player player1, Player player2) {
    this.view = view;
    this.manualDataGatherer = this.view.getManualDataGatherer();
    this.player1 = player1;
    this.player2 = player2;
    this.specifications = new HashMap<>();
  }

  /**
   * Ask the user for a board size Initializes the friendlyBattleshipBoard
   * height range: [6, 15] inclusive
   * width range: [6, 15] inclusive
   * re-prompts the user if an invalid input is detected
   */
  private void askSize() {
    Posn size = this.manualDataGatherer.askForBoardSize();
    this.height = size.getY();
    this.width = size.getX();
    this.fleetSize = Integer.min(this.height, this.width);
    if (this.height < 6 || this.height > 15 || this.width < 6 || this.width > 15) {
      this.view.showErrorMessage(
          "One or more of these dimensions is invalid. Dimensions must be in the range [6, 15].\n");
      this.askSize();
    }
  }

  /**
   * Ask the user for their fleet selection
   * re-prompts the user if an invalid input is detected
   */
  private void askFleetSelection() {
    StringBuilder builder = new StringBuilder();
    builder.append("Please enter your fleet in the order [ ");
    for (ShipType shipType : ShipType.values()) {
      if (shipType == ShipType.PLACEHOLDER) {
        continue;
      }
      builder.append(shipType.name()).append(" ");
    }
    builder.append("].\nRemember, your fleet may not exceed size: ").append(this.fleetSize)
        .append("\n");
    this.view.promptUserForInput(builder.toString());
    int numOfShips;
    int sum = 0;
    for (ShipType shipType : ShipType.values()) {
      if (shipType == ShipType.PLACEHOLDER) {
        continue;
      }
      numOfShips = this.manualDataGatherer.askForNumber(shipType.name());
      sum += numOfShips;

      if (numOfShips < 1 || sum > this.fleetSize) {
        this.view.showErrorMessage(
            "This list of ship types if invalid.\n"
                + "You must use at least one of each ship type and "
                + "not exceed the maximum fleet size.\n");
        this.askFleetSelection();
        return;
      } else {
        this.specifications.put(shipType, numOfShips);
      }
    }
  }

  /**
   * Gather the two salvos from the players and fire them
   *
   * @return the result of the round
   */
  private GameResult runRound() {
    List<Posn> p1shots = this.player1.takeShots();
    List<Posn> p2shots = this.player2.takeShots();

    GameResult result;

    if (p1shots.size() == 0 && p2shots.size() == 0) {
      result = GameResult.DRAW;
    } else if (p2shots.size() == 0) {
      result = GameResult.WIN;
    } else if (p1shots.size() == 0) {
      result = GameResult.LOSE;
    } else {
      result = null;
    }

    if (result == null) {
      List<Posn> p1successfulShots = this.player2.reportDamage(p1shots);
      List<Posn> p2successfulShots = this.player1.reportDamage(p2shots);

      this.player1.successfulHits(p1successfulShots);
      this.player2.successfulHits(p2successfulShots);
    }

    return result;
  }

  /**
   * runs the game of battleSalvo
   */
  public void run() {
    this.askSize();
    this.askFleetSelection();
    this.player1.setup(this.height, this.width, this.specifications);
    this.player2.setup(this.height, this.width, this.specifications);
    GameResult result = null;
    while (result == null) {
      result = this.runRound();
    }
    this.player1.endGame(result, "game over");
    this.player2.endGame(result, "game over");
  }
}
