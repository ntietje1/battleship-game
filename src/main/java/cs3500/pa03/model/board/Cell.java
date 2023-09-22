package cs3500.pa03.model.board;

/**
 * Represents a single cell on a battleship board
 */
public class Cell {
  boolean attacked;
  Ship ship;

  /**
   * constructor for a cell
   */
  public Cell() {
    this.attacked = false;
    this.ship = null;
  }

  /**
   * Generate a string representation of this cell
   *
   * @return single character string representation of this cell
   */
  public String toString() {
    if (this.isOccupied() && !this.isAttacked()) { // non-attacked ship
      return ConsoleColors.YELLOW + "#" + ConsoleColors.RESET;
    } else if (this.isOccupied() && this.isAttacked()) { // hit ship
      return ConsoleColors.GREEN + "X" + ConsoleColors.RESET;
    } else if (!this.isOccupied() && this.isAttacked()) { // missed shot
      return ConsoleColors.RED + "X" + ConsoleColors.RESET;
    } else { // Unattacked cell
      return ConsoleColors.BLUE + "~" + ConsoleColors.RESET;
    }
  }

  /**
   * Set this cell's ship reference
   *
   * @param s ship to be set
   */
  public void setShip(Ship s) {
    this.ship = s;
    this.ship.addCell(this);
  }

  /**
   * Is this cell occupied?
   *
   * @return boolean true if occupied, false if not
   */
  public boolean isOccupied() {
    return ship != null;
  }

  /**
   * Is this cell attacked?
   *
   * @return boolean true if attacked, false if not
   */
  public boolean isAttacked() {
    return attacked;
  }

  /**
   * Is this cell's ship destroyed?
   *
   * @return boolean true if destroyed, false if not
   */
  public boolean isDestroyed() {
    return this.ship.isDestroyed();
  }

  /**
   * Attempt to attack this cell or reset it's attack value
   *
   */
  public void attack(boolean value) {
    if (this.attacked == value) {
      throw new IllegalStateException("Attempted an illegal update to attack value of cell");
    } else {
      this.attacked = value;
    }
  }

  /**
   * Class to hold all the colors used in the console
   */
  private static class ConsoleColors {
    // Reset
    private static final String RESET = "\033[0m";  // Text Reset

    // Regular Colors
    private static final String BLACK = "\033[0;30m";   // BLACK
    private static final String RED = "\033[0;31m";     // RED
    private static final String GREEN = "\033[0;32m";   // GREEN
    private static final String YELLOW = "\033[0;33m";  // YELLOW
    private static final String BLUE = "\033[0;34m";    // BLUE
    private static final String PURPLE = "\033[0;35m";  // PURPLE
    private static final String CYAN = "\033[0;36m";    // CYAN
    private static final String WHITE = "\033[0;37m";   // WHITE
  }
}
