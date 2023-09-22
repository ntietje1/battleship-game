package cs3500.pa03.model.player;

import cs3500.pa03.model.board.BattleshipBoard;
import cs3500.pa03.model.board.Posn;
import cs3500.pa03.model.board.Ship;
import cs3500.pa03.model.board.ShipType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * represents a player
 */
public abstract class AbstractPlayer implements Player {
  protected BattleshipBoard friendlyBoard;
  protected BattleshipBoard enemyBoard;
  protected Map<ShipType, Integer> specifications;
  protected List<Posn> lastRecievedVolley;
  protected List<Posn> lastVolleyFired;
  protected List<Posn> lastSuccessfulHits;

  /**
   * constructor for players
   */
  public AbstractPlayer() {
    this.lastVolleyFired = new ArrayList<>();
    this.lastSuccessfulHits = new ArrayList<>();
    this.lastRecievedVolley = new ArrayList<>();
  }

  /**
   * Get the player's name.
   *
   * @return the player's name
   */
  public abstract String name();

  /**
   * Returns this player's shots on the opponent's board. The number of shots returned should
   * equal the number of ships on this player's board that have not sunk.
   *
   * @return the locations of shots on the opponent's board
   */
  public abstract List<Posn> takeShots();

  /**
   * Given the specifications for a BattleSalvo board, return a list of ships with their locations
   * on the board.
   *
   * @param height         the height of the board, range: [6, 15] inclusive
   * @param width          the width of the board, range: [6, 15] inclusive
   * @param specifications a map of ship type to the number of occurrences each ship should
   *                       appear on the board
   * @return the placements of each ship on the board
   */
  public List<Ship> setup(int height, int width, Map<ShipType, Integer> specifications) {
    this.friendlyBoard = new BattleshipBoard(width, height);
    this.enemyBoard = new BattleshipBoard(width, height);
    this.specifications = specifications;
    this.placeAllShips(specifications);
    return this.friendlyBoard.getShips();
  }

  /**
   * Given the list of shots the opponent has fired on this player's board, report which
   * shots hit a ship on this player's board.
   *
   * @param opponentShotsOnBoard the opponent's shots on this player's board
   * @return a filtered list of the given shots that contain all locations of shots that hit a
   *         ship on this board
   */
  public List<Posn> reportDamage(List<Posn> opponentShotsOnBoard) {
    this.lastRecievedVolley = opponentShotsOnBoard;
    ArrayList<Posn> hits = new ArrayList<>();
    for (Posn posn : opponentShotsOnBoard) {
      this.friendlyBoard.attack(posn);
      boolean hit = this.friendlyBoard.checkHit(posn);
      if (hit) {
        hits.add(posn);
      }
    }
    return hits;
  }

  /**
   * Reports to this player what shots in their previous volley returned from takeShots()
   * successfully hit an opponent's ship.
   *
   * @param shotsThatHitOpponentShips the list of shots that successfully hit the opponent's ships
   */
  public void successfulHits(List<Posn> shotsThatHitOpponentShips) {
    this.lastSuccessfulHits = shotsThatHitOpponentShips;
    for (Posn posn : shotsThatHitOpponentShips) {
      boolean success = this.enemyBoard.placeShip(new Ship(ShipType.PLACEHOLDER, posn, posn));
      if (!success) {
        System.out.println(this.name() + " failed to fire at " + posn);
        throw new IllegalStateException("Shot previously hit here!");
      }
    }
  }

  /**
   * Notifies the player that the game is over.
   * Win, lose, and draw should all be supported
   *
   * @param result if the player has won, lost, or forced a draw
   * @param reason the reason for the game ending
   */
  public void endGame(GameResult result, String reason) {
    System.out.println(this.name() + " has ended the game with " + result.name());
    System.out.println(reason);

  }

  /**
   * Place all ships onto the board
   * Prioritizes ship placements that are away from other ships
   *
   * @param specifications map of ship types to be placed
   */
  private void placeAllShips(Map<ShipType, Integer> specifications) {
    for (ShipType shipType : specifications.keySet()) {
      for (int i = 0; i < specifications.get(shipType); i++) {
        this.placeRandomShip(shipType);
      }
    }
  }

  /**
   * Place a random ship on the board.
   * Prioritizes ship placements that are away from other ships
   *
   * @param shipType to be placed
   */
  private void placeRandomShip(ShipType shipType) {
    int maxX = this.friendlyBoard.getWidth();
    int maxY = this.friendlyBoard.getHeight();
    int spacing = Math.max(maxX, maxY);
    int attempts = 1;

    boolean successful = false;
    while (!successful) {
      if (attempts % (maxX * maxY) == 0) {
        spacing--;
      }
      Posn posn1 = Posn.randomPosn(maxX, maxY);
      Posn posn2;
      try {
        posn2 = Posn.randomPosnFrom(posn1, shipType.size(), maxX, maxY);
      } catch (IllegalArgumentException e) {
        continue;
      }
      boolean nearOtherShip = false;
      for (Posn adjacentPosn : getSurroundingPosns(posn1, posn2, spacing)) {
        if (friendlyBoard.checkOccupied(adjacentPosn)) {
          nearOtherShip = true;
        }
      }
      successful = !nearOtherShip && this.friendlyBoard.placeShip(new Ship(shipType, posn1, posn2));
      attempts++;
    }
  }

  /**
   * Get all posns within the given spacing of the two posns
   *
   * @param start   posn of a hypothetical ship position
   * @param end     posn of a hypothetical ship position
   * @param spacing how far out to look from the given positions
   * @return list of all posns around the given positions
   */
  private List<Posn> getSurroundingPosns(Posn start, Posn end, int spacing) {
    List<Posn> output = new ArrayList<>();
    int minX = Math.min(start.getX(), end.getX()) - spacing;
    int maxX = Math.max(start.getX(), end.getX()) + spacing;
    int minY = Math.min(start.getY(), end.getY()) - spacing;
    int maxY = Math.max(start.getY(), end.getY()) + spacing;
    for (int x = minX; x <= maxX; x++) {
      for (int y = minY; y <= maxY; y++) {
        Posn targetPosn = new Posn(x, y);
        if (friendlyBoard.isValidPosn(targetPosn)) {
          output.add(targetPosn);
        }
      }
    }
    return output;
  }
}
