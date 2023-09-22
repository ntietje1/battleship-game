package cs3500.pa03.model.board;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cs3500.pa03.TestUtils;
import org.junit.jupiter.api.Test;

/**
 * represents tests for the cell class
 */
class CellTest {

  /**
   * tests the toString method for a new cell
   */
  @Test
  void testToString() {
    Cell cell = new Cell();
    assertEquals("~", TestUtils.removeColors(cell.toString()));
  }

  /**
   * tests the toString method for an attacked cell
   */
  @Test
  void testToStringAttacked() {
    Cell cell = new Cell();
    cell.attack(true);
    assertEquals("X", TestUtils.removeColors(cell.toString()));
  }

  /**
   * tests the toString method for a cell with a ship
   */
  @Test
  void testToStringShip() {
    Cell cell = new Cell();
    Ship ship = new Ship(ShipType.SUBMARINE, new Posn(0, 0), new Posn(0, 2));
    cell.setShip(ship);
    assertEquals("#", TestUtils.removeColors(cell.toString()));
  }

  /**
   * tests the toString method for a cell with a ship that was attacked
   */
  @Test
  void testToStringHit() {
    Cell cell = new Cell();
    Ship ship = new Ship(ShipType.SUBMARINE, new Posn(0, 0), new Posn(0, 2));
    cell.setShip(ship);
    cell.attack(true);
    assertEquals("X", TestUtils.removeColors(cell.toString()));
  }


  /**
   * tests that a cell is not occupied when it is created
   */
  @Test
  void testIsOccupied() {
    Cell cell = new Cell();
    assertFalse(cell.isOccupied());
  }

  /**
   * tests that a cell is not occupied when the ship on it is attacked
   */
  @Test
  void testIsOccupiedAttacked() {
    Cell cell = new Cell();
    cell.attack(true);
    assertFalse(cell.isOccupied());
  }

  /**
   * tests that a cell is occupied when a ship is on it
   */
  @Test
  void testIsOccupiedShip() {
    Cell cell = new Cell();
    Ship ship = new Ship(ShipType.SUBMARINE, new Posn(0, 0), new Posn(0, 2));
    cell.setShip(ship);
    assertTrue(cell.isOccupied());
  }

  /**
   * tests that a cell is not attacked when it has not been attacked
   */
  @Test
  void testIsAttacked() {
    Cell cell = new Cell();
    assertFalse(cell.isAttacked());
  }

  /**
   * tests that a cell was attacked after being attacked
   */
  @Test
  void testIsAttacked2() {
    Cell cell = new Cell();
    cell.attack(true);
    assertTrue(cell.isAttacked());
  }

  /**
   * tests that a cell was attacked even if there was a ship on it
   */
  @Test
  void testIsAttacked3() {
    Cell cell = new Cell();
    Ship ship = new Ship(ShipType.SUBMARINE, new Posn(0, 0), new Posn(0, 2));
    cell.setShip(ship);
    cell.attack(true);
    assertTrue(cell.isAttacked());
  }

  /**
   * tests that you can't attack a ship more than once
   */
  @Test
  void testIsAttackedRepeat() {
    Cell cell = new Cell();
    Ship ship = new Ship(ShipType.SUBMARINE, new Posn(0, 0), new Posn(0, 2));
    cell.setShip(ship);
    cell.attack(true);
    assertThrows(IllegalStateException.class, () -> cell.attack(true));
  }

  /**
   * tests that a ship is destroyed after all the locations that it is on were attacked
   */
  @Test
  void testIsDestroyed() {
    Cell cell1 = new Cell();
    Cell cell2 = new Cell();
    Cell cell3 = new Cell();
    Ship ship = new Ship(ShipType.SUBMARINE, new Posn(0, 0), new Posn(0, 2));
    cell1.setShip(ship);
    cell2.setShip(ship);
    cell3.setShip(ship);
    cell1.attack(true);
    cell2.attack(true);
    cell3.attack(true);
    assertTrue(cell1.isDestroyed());
    assertTrue(cell2.isDestroyed());
    assertTrue(cell3.isDestroyed());
  }

  /**
   * tests that a ship not destroyed if only some locations on it were hit
   */
  @Test
  void isDestroyed2() {
    Cell cell1 = new Cell();
    Cell cell2 = new Cell();
    Cell cell3 = new Cell();
    Ship ship = new Ship(ShipType.SUBMARINE, new Posn(0, 0), new Posn(0, 2));
    cell1.setShip(ship);
    cell2.setShip(ship);
    cell3.setShip(ship);
    cell1.attack(true);
    cell3.attack(true);
    assertFalse(cell1.isDestroyed());
    assertFalse(cell2.isDestroyed());
    assertFalse(cell3.isDestroyed());
  }
}