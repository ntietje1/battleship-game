package cs3500.pa03.model.player;

import cs3500.pa03.controller.ManualDataGatherer;
import cs3500.pa03.model.board.Posn;
import cs3500.pa03.model.board.Ship;
import cs3500.pa03.model.board.ShipType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents a manual player for the battleship game
 */
public class ManualPlayer extends AbstractPlayer {
  ManualDataGatherer manualDataGatherer;

  /**
   * Instantiate a manual player
   */
  public ManualPlayer(ManualDataGatherer gatherer) {
    super();
    this.manualDataGatherer = gatherer;
  }

  /**
   * Get the player's name.
   *
   * @return the player's name
   */
  @Override
  public String name() {
    return "Nick's manual player";
  }

  /**
   * Returns this player's shots on the opponent's board. The number of shots returned should
   * equal the number of ships on this player's board that have not sunk.
   *
   * @return the locations of shots on the opponent's board
   */
  @Override
  public List<Posn> takeShots() {
    ArrayList<Posn> shots = new ArrayList<>();
    int shotsToFire = Integer.min(this.friendlyBoard.getAliveShipCount(),
        this.enemyBoard.countAvailableCells());

    this.manualDataGatherer.askForVolley(this.name(), shotsToFire, this.enemyBoard,
        this.friendlyBoard);

    while (shotsToFire > 0) {
      boolean successful = this.askSingleWhereToFire(shots);
      if (!successful) {
        this.manualDataGatherer.respond(
            "This coordinate has already been attacked or is out of bounds. Resetting shots...\n");
        this.resetShots(shots);
        return this.takeShots();
      } else {
        shotsToFire--;
      }
    }
    this.manualDataGatherer.respond("Firing!!\n");
    return shots;
  }

  /**
   * Prompt the user where to fire a single shot
   */
  private boolean askSingleWhereToFire(ArrayList<Posn> shots) {
    Posn posn = this.manualDataGatherer.askForPosn(this.name());
    boolean successful = this.enemyBoard.attack(posn);
    String response = successful ? "valid" : "invalid";
    response = "Attack at " + posn + " is " + response + ".\n";
    this.manualDataGatherer.respond(response);
    if (successful) {
      shots.add(posn);
    }
    return successful;
  }

  /**
   * Remove the given shots from the enemy board
   *
   * @param shots shots to be removed
   */
  private void resetShots(ArrayList<Posn> shots) {
    for (Posn posn : shots) {
      this.enemyBoard.attack(posn, false);
    }
  }
}
