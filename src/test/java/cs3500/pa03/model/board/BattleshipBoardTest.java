package cs3500.pa03.model.board;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cs3500.pa03.TestUtils;
import org.junit.jupiter.api.Test;

/**
 * represents tests for the BattleshipBoard class
 */
class BattleshipBoardTest {

  /**
   * tests that the place ship method placed a ship, that it was attacked, and that it sunk
   */
  @Test
  void testAttackAndSink() {
    BattleshipBoard board = new BattleshipBoard(6, 6);
    board.placeShip(new Ship(ShipType.SUBMARINE, new Posn(0, 0), new Posn(0, 2)));
    board.attack(new Posn(0, 0));
    board.attack(new Posn(0, 1));
    board.attack(new Posn(0, 2));
    assertEquals(0, board.getAliveShipCount());
  }

  /**
   * tests the placeShip method for a ship that's too large
   */
  @Test
  void testPlaceShipOutOfBounds() {
    BattleshipBoard board = new BattleshipBoard(6, 6);
    assertFalse(board.placeShip(new Ship(ShipType.SUBMARINE, new Posn(6, 6), new Posn(6, 8))));
  }

  /**
   * tests placing a ship with the placeShip method
   */
  @Test
  void testPlaceShip() {
    BattleshipBoard board = new BattleshipBoard(6, 6);
    assertTrue(board.placeShip(new Ship(ShipType.SUBMARINE, new Posn(0, 0), new Posn(0, 2))));
  }

  /**
   * tests that you can't place a ship on top of another ship
   */
  @Test
  void testPlaceShipOnTop() {
    BattleshipBoard board = new BattleshipBoard(6, 6);
    board.placeShip(new Ship(ShipType.SUBMARINE, new Posn(0, 0), new Posn(2, 0)));
    assertFalse(board.placeShip(new Ship(ShipType.SUBMARINE, new Posn(0, 0), new Posn(0, 2))));
  }

  /**
   * tests that you can't place a ship out of bounds
   */
  @Test
  void testPlaceShipOutOfBoundsNegative() {
    BattleshipBoard board = new BattleshipBoard(6, 6);
    board.placeShip(new Ship(ShipType.SUBMARINE, new Posn(0, 0), new Posn(2, 0)));
    assertFalse(board.placeShip(new Ship(ShipType.SUBMARINE, new Posn(-2, 0), new Posn(-2, 2))));
  }

  /**
   * tests that you can't place a ship out of bounds (other orientation)
   */
  @Test
  void testPlaceShipOutOfBoundsNegativeVertical() {
    BattleshipBoard board = new BattleshipBoard(6, 6);
    board.placeShip(new Ship(ShipType.SUBMARINE, new Posn(0, 0), new Posn(2, 0)));
    assertFalse(board.placeShip(new Ship(ShipType.SUBMARINE, new Posn(0, -2), new Posn(0, 0))));
  }

  /**
   * tests the getAliveShipCount method
   */
  @Test
  void testGetAliveShipCount() {
    BattleshipBoard board = new BattleshipBoard(6, 6);
    board.placeShip(new Ship(ShipType.SUBMARINE, new Posn(0, 0), new Posn(0, 2)));
    board.attack(new Posn(0, 0));
    board.attack(new Posn(0, 1));
    assertEquals(1, board.getAliveShipCount());
  }

  /**
   * tests the getShips method when it returns a list of ships with ships in it
   */
  @Test
  void testGetShips() {
    BattleshipBoard board = new BattleshipBoard(6, 6);
    board.placeShip(new Ship(ShipType.SUBMARINE, new Posn(0, 0), new Posn(0, 2)));
    assertEquals(1, board.getShips().size());
  }

  /**
   * tests the getShips method when it returns a list of ships with NO ships in it
   */
  @Test
  void testGetShipsWithNoShips() {
    BattleshipBoard board = new BattleshipBoard(6, 6);
    assertEquals(0, board.getShips().size());
  }

  /**
   * tests the getHeight method
   */
  @Test
  void testGetHeight() {
    BattleshipBoard board = new BattleshipBoard(6, 6);
    assertEquals(6, board.getHeight());
  }

  /**
   * tests the getWidth method
   */
  @Test
  void testGetWidth() {
    BattleshipBoard board = new BattleshipBoard(6, 6);
    assertEquals(6, board.getWidth());
  }

  /**
   * tests the attack method to make sure you can't attack the same place twice
   */
  @Test
  void testAttack() {
    BattleshipBoard board = new BattleshipBoard(6, 6);
    board.placeShip(new Ship(ShipType.SUBMARINE, new Posn(0, 0), new Posn(0, 2)));
    board.attack(new Posn(0, 0));
    board.attack(new Posn(0, 1));
    board.attack(new Posn(0, 2));
    assertFalse(board.attack(new Posn(0, 0)));
  }

  /**
   * tests that you can't attack a cell out of bounds
   */
  @Test
  void testAttackOutOfBounds() {
    BattleshipBoard board = new BattleshipBoard(6, 6);
    assertFalse(board.attack(new Posn(230, 4506)));
  }

  /**
   * tests that when you attack a cell, it marks it as hit if there's a ship
   */
  @Test
  void testCheckHit() {
    BattleshipBoard board = new BattleshipBoard(6, 6);
    board.placeShip(new Ship(ShipType.SUBMARINE, new Posn(0, 0), new Posn(0, 2)));
    board.attack(new Posn(0, 0));
    board.attack(new Posn(0, 1));
    board.attack(new Posn(0, 2));
    assertTrue(board.checkHit(new Posn(0, 0)));
    assertTrue(board.checkHit(new Posn(0, 1)));
    assertTrue(board.checkHit(new Posn(0, 2)));
  }

  /**
   * tests that a cell is not hit, if it has a ship but was not attacked
   */
  @Test
  void testCheckHitNoAttack() {
    BattleshipBoard board = new BattleshipBoard(6, 6);
    board.placeShip(new Ship(ShipType.SUBMARINE, new Posn(0, 0), new Posn(0, 2)));
    assertFalse(board.checkHit(new Posn(0, 0)));
    assertFalse(board.checkHit(new Posn(0, 1)));
    assertFalse(board.checkHit(new Posn(0, 2)));
  }

  /**
   * tests that you can attack and undo an attack
   */
  @Test
  void testCheckHitUndoAttack() {
    BattleshipBoard board = new BattleshipBoard(6, 6);
    board.placeShip(new Ship(ShipType.SUBMARINE, new Posn(0, 0), new Posn(0, 2)));
    board.attack(new Posn(0, 0));
    board.attack(new Posn(0, 1));
    board.attack(new Posn(0, 2));
    board.attack(new Posn(0, 0), false);
    board.attack(new Posn(0, 1), false);
    board.attack(new Posn(0, 2), false);
    assertFalse(board.checkHit(new Posn(0, 0)));
    assertFalse(board.checkHit(new Posn(0, 1)));
    assertFalse(board.checkHit(new Posn(0, 2)));
  }

  /**
   * tests the toString method for the board
   */
  @Test
  void testToString() {
    BattleshipBoard board = new BattleshipBoard(6, 6);
    board.placeShip(new Ship(ShipType.SUBMARINE, new Posn(0, 0), new Posn(0, 2)));
    board.attack(new Posn(0, 0));
    board.attack(new Posn(0, 1));
    board.attack(new Posn(2, 2));
    assertEquals(
        ""
            + "   0  1  2  3  4  5 \n"
            + "0  X  ~  ~  ~  ~  ~ \n"
            + "1  X  ~  ~  ~  ~  ~ \n"
            + "2  #  ~  X  ~  ~  ~ \n"
            + "3  ~  ~  ~  ~  ~  ~ \n"
            + "4  ~  ~  ~  ~  ~  ~ \n"
            + "5  ~  ~  ~  ~  ~  ~ \n", TestUtils.removeColors(board.toString()));
  }

  /**
   * tests the toString method for the board
   */
  @Test
  void testToString2() {
    BattleshipBoard board = new BattleshipBoard(10, 6);
    board.placeShip(new Ship(ShipType.CARRIER, new Posn(0, 0), new Posn(0, 5)));
    board.attack(new Posn(0, 0));
    board.attack(new Posn(0, 1));
    board.attack(new Posn(2, 2));
    board.attack(new Posn(5, 6));
    board.attack(new Posn(9, 4));
    board.attack(new Posn(3, 2));
    assertEquals(""
        + "   0  1  2  3  4  5  6  7  8  9 \n"
        + "0  X  ~  ~  ~  ~  ~  ~  ~  ~  ~ \n"
        + "1  X  ~  ~  ~  ~  ~  ~  ~  ~  ~ \n"
        + "2  #  ~  X  X  ~  ~  ~  ~  ~  ~ \n"
        + "3  #  ~  ~  ~  ~  ~  ~  ~  ~  ~ \n"
        + "4  #  ~  ~  ~  ~  ~  ~  ~  ~  X \n"
        + "5  #  ~  ~  ~  ~  ~  ~  ~  ~  ~ \n", TestUtils.removeColors(board.toString()));
  }
}