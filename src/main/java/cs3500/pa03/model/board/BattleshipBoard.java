package cs3500.pa03.model.board;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a Battleship board
 */
public class BattleshipBoard {
  int height;
  int width;
  Cell[][] board;
  List<Ship> ships;

  /**
   * Instantiate a board full of empty cells
   *
   * @param height height of the board
   * @param width  width of the board
   */
  public BattleshipBoard(int width, int height) {
    this.height = height;
    this.width = width;
    board = new Cell[width][height];
    this.initializeBoard();
    this.ships = new ArrayList<>();
  }

  /**
   * Helper method for instantiating empty cells into board
   * positive X is right
   * positive Y is down
   * use board[column][row] to access a position on the board
   */
  private void initializeBoard() {
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        Cell newCell = new Cell();
        board[i][j] = newCell;
      }
    }
  }

  /**
   * Get the height of this board
   *
   * @return height of this board
   */
  public int getHeight() {
    return height;
  }

  /**
   * Get the width of this board
   *
   * @return width of this board
   */
  public int getWidth() {
    return width;
  }

  /**
   * Get the array of cells that represents the board
   *
   * @return the board
   */
  public Cell[][] getBoard() {
    return this.board;
  }

  /**
   * Generate a String representation of the board
   *
   * @return a String representation of the board
   */
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("  ");
    // print column indexes
    for (int j = 0; j < this.width; j++) {
      int digits = Integer.toString(j).length();
      sb.append(" ".repeat(2 - digits)).append(j).append(" ");
    }
    sb.append("\n");
    for (int i = 0; i < this.height; i++) {
      // print row indexes
      int digits = Integer.toString(i).length();
      sb.append(i).append(" ".repeat(2 - digits));
      for (int j = 0; j < this.width; j++) {
        sb.append(" ").append(board[j][i].toString()).append(" ");
      }
      sb.append("\n");
    }
    return sb.toString();
  }

  /**
   * Get this board's list of ships
   *
   * @return the list of ships
   */
  public List<Ship> getShips() {
    return this.ships;
  }

  /**
   * How many ships are still alive in the board
   *
   * @return number of ships still alive
   */
  public int getAliveShipCount() {
    int sum = 0;
    for (Ship s : ships) {
      if (!s.isDestroyed()) {
        sum++;
      }
    }
    return sum;
  }

  /**
   * Attack the targeted cell
   *
   * @param posn position to attack
   * @return true if successful attack, false if out of bounds or already attacked
   */
  public boolean attack(Posn posn) {
    return this.attack(posn, true);
  }

  /**
   * Attack the targeted cell
   *
   * @param posn      position to attack
   * @param attacking true if attacking the cell, false if un-doing an attack
   * @return true if successful attack, false if out of bounds or already attacked
   */
  public boolean attack(Posn posn, boolean attacking) {
    try {
      Cell targetedCell = this.board[posn.getX()][posn.getY()];
      targetedCell.attack(attacking);
      return true;
    } catch (IndexOutOfBoundsException | IllegalStateException e) {
      return false;
    }
  }

  /**
   * Count how many cells are available to attack
   *
   * @return number of cells available to attack
   */
  public int countAvailableCells() {
    int sum = 0;
    for (int i = 0; i < this.width; i++) {
      for (int j = 0; j < this.height; j++) {
        if (!this.board[i][j].isAttacked()) {
          sum++;
        }
      }
    }
    return sum;
  }

  /**
   * Is this cell a successful hit on a ship?
   *
   * @param posn position to check
   * @return boolean true if successful hit, false if not
   */
  public boolean checkHit(Posn posn) {
    return this.board[posn.getX()][posn.getY()].isOccupied()
       && this.board[posn.getX()][posn.getY()].isAttacked();
  }

  /**
   * checks if this cell on the board is attacked
   *
   * @param posn the position to check
   * @return if the cell is attacked
   */
  public boolean checkAttacked(Posn posn) {
    return this.board[posn.getX()][posn.getY()].isAttacked();
  }

  /**
   * check if this cell on the board is occupied
   *
   * @param posn the position to check
   * @return if the cell is occupied
   */
  public boolean checkOccupied(Posn posn) {
    return this.board[posn.getX()][posn.getY()].isOccupied();
  }

  /**
   * Place a ship between the given positions
   *
   * @param ship the ship to place
   * @return boolean: true if successful, false if unsuccessful
   */
  public boolean placeShip(Ship ship) {
    Posn start = ship.start();
    Posn end = ship.end();
    if (!this.isValidPlacement(start, end)) {
      return false;
    }

    // for each cell that the ship spans, add a reference to this ship to that cell
    int minX = Math.min(start.getX(), end.getX());
    int maxX = Math.max(start.getX(), end.getX());
    int minY = Math.min(start.getY(), end.getY());
    int maxY = Math.max(start.getY(), end.getY());
    for (int i = minX; i <= maxX; i++) {
      for (int j = minY; j <= maxY; j++) {
        Cell cell = this.board[i][j];
        cell.setShip(ship);
      }
    }
    this.ships.add(ship);
    return true;
  }

  /**
   * Is the given ship in a valid placement?
   * a valid placement is in bounds and does not overlap with another ship
   *
   * @return boolean true if valid, false if invalid
   */
  public boolean isValidPlacement(Posn start, Posn end) {
    boolean inBounds = this.isValidPosn(start) && this.isValidPosn(end);
    if (!inBounds) {
      return false;
    }
    boolean unoccupied = true;
    int minX = Math.min(start.getX(), end.getX());
    int maxX = Math.max(start.getX(), end.getX());
    int minY = Math.min(start.getY(), end.getY());
    int maxY = Math.max(start.getY(), end.getY());
    for (int i = minX; i <= maxX; i++) {
      for (int j = minY; j <= maxY; j++) {
        if (board[i][j].isOccupied()) {
          unoccupied = false;
        }
      }
    }
    return unoccupied;
  }

  /**
   * Is this posn out of bounds?
   *
   * @param posn coordinate to check
   * @return boolean true if valid, false if invalid
   */
  public boolean isValidPosn(Posn posn) {
    int x = posn.getX();
    int y = posn.getY();
    return (x >= 0 && x < this.width) && (y >= 0 && y < this.height);
  }
}
