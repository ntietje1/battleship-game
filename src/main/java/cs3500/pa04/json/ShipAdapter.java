package cs3500.pa04.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa03.model.board.Direction;
import cs3500.pa03.model.board.Posn;
import cs3500.pa03.model.board.Ship;

/**
 * represents a way to adapt ships
 */
public class ShipAdapter {

  Posn coord;
  int length;
  Direction direction;

  /**
   * constructs a ship from another format of a ship
   *
   * @param myShip the ship to convert
   */
  public ShipAdapter(Ship myShip) {
    int minX = Math.min(myShip.start().getX(), myShip.end().getX());
    int minY = Math.min(myShip.start().getY(), myShip.end().getY());

    this.coord = new Posn(minX, minY);

    this.length = myShip.getShipLength();
    if (myShip.start().getX() == myShip.end().getX()) {
      this.direction = Direction.VERTICAL;
    } else {
      this.direction = Direction.HORIZONTAL;
    }
  }

  /**
   * constructor for a ShipAdapter
   *
   * @param coord  the starting coordinate of the ship
   * @param length the length of the ship
   * @param dir    the direction of the ship
   */
  public ShipAdapter(
      @JsonProperty("coord") Posn coord,
      @JsonProperty("length") int length,
      @JsonProperty("direction") Direction dir) {
    this.coord = coord;
    this.length = length;
    this.direction = dir;
  }


  /**
   * returns the start coordinate of this ship
   *
   * @return the start coordinate of the ship
   */
  public Posn getCoord() {
    return this.coord;
  }

  /**
   * returns the length of this ship
   *
   * @return the length of the ship
   */
  public int getLength() {
    return this.length;
  }

  /**
   * returns the direction of this ship
   *
   * @return the direction of this ship
   */
  public Direction getDirection() {
    return this.direction;
  }

}