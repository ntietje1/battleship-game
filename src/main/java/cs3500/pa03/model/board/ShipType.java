package cs3500.pa03.model.board;


/**
 * represents a type of ship and its size
 */
public enum ShipType {
  CARRIER(6), BATTLESHIP(5), DESTROYER(4), SUBMARINE(3), PLACEHOLDER(1);

  private final int size;

  /**
   * constructs a ship type
   *
   * @param size number of spaces a ship takes up on the board
   */
  ShipType(int size) {
    this.size = size;
  }

  /**
   * returns the size of this ship type
   *
   * @return size of this ship type
   */
  public int size() {
    return this.size;
  }
}
