package cs3500.pa03.model.board;

/**
 * Represents a ship in a game of battleship
 */
public class Ship {
  private final Posn start;
  private final Posn end;
  private ShipType shipType;

  private Cell[] cells;

  /**
   * Instantiate a Ship object
   *
   * @param start start position of the ship
   * @param end   end position of the ship
   */
  public Ship(ShipType shipType, Posn start, Posn end) {
    this.start = start;
    this.end = end;
    if (shipType.size() == this.start.distance(this.end)) {
      this.shipType = shipType;
      cells = new Cell[this.shipType.size()];
    } else {
      throw new IllegalArgumentException("Invalid placement for ship of size: " + shipType.size());
    }
  }

  /**
   * gets the length of this ship
   *
   * @return the length of this ship
   */
  public int getShipLength() {
    return this.shipType.size();
  }

  /**
   * gets the starting coordinate of this ship
   *
   * @return the starting coordinate of this ship
   */
  public Posn start() {
    return this.start;
  }

  /**
   * gets the ending coordinate of this ship
   *
   * @return the ending coordinate of this ship
   */
  public Posn end() {
    return this.end;
  }

  /**
   * Add a cell to this ship's cell array
   *
   * @param cell cell to be added
   */
  public void addCell(Cell cell) {
    if (!cell.isOccupied()) {
      cell.setShip(this);
    }

    for (int i = 0; i < this.cells.length; i++) {
      if (this.cells[i] == cell) {
        return; // Cell already exists in the array
      }
    }

    for (int i = 0; i < this.cells.length; i++) {
      if (this.cells[i] == null) {
        this.cells[i] = cell;
        return;
      }
    }

    throw new IllegalStateException("Attempted to add a cell to a ship with a full cell array");
  }

  /**
   * Is this ship destroyed?
   * false if any cells in this ship's cell array are not attacked
   *
   * @return boolean true if destroyed, false if not
   */
  public boolean isDestroyed() {
    for (Cell c : this.cells) {
      if (!c.isAttacked()) {
        return false;
      }
    }
    return true;
  }
}
