package cs3500.pa03.model.board;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cs3500.pa03.model.board.Cell;
import cs3500.pa03.model.board.Posn;
import cs3500.pa03.model.board.Ship;
import cs3500.pa03.model.board.ShipType;
import org.junit.jupiter.api.Test;

/**
 * represents tests for the ship class
 */
class ShipTest {

  /**
   * tests that you can't make a ship that is bigger than the shipType allows
   */
  @Test
  void testShipConstructor() {
    assertThrows(IllegalArgumentException.class,
        () -> new Ship(ShipType.SUBMARINE, new Posn(3, 4), new Posn(3, 10)));
  }

  /**
   * tests that you can't make a ship that is smaller than the shipType allows
   */
  @Test
  void testShipConstructor2() {
    assertThrows(IllegalArgumentException.class,
        () -> new Ship(ShipType.CARRIER, new Posn(3, 4), new Posn(3, 8)));
  }

  /**
   * tests that a ship is destroyed when all of its cells are hit
   */
  @Test
  void testIsDestroyed() {
    Cell cell1 = new Cell();
    Cell cell2 = new Cell();
    Cell cell3 = new Cell();
    Ship ship = new Ship(ShipType.SUBMARINE, new Posn(0, 0), new Posn(0, 2));
    ship.addCell(cell1);
    ship.addCell(cell2);
    ship.addCell(cell3);
    cell1.attack(true);
    cell2.attack(true);
    cell3.attack(true);
    assertTrue(ship.isDestroyed());
  }

  /**
   * tests that a ship is destroyed when all of its cells are hit
   */
  @Test
  void testIsDestroyed2() {
    Cell cell1 = new Cell();
    Cell cell2 = new Cell();
    Cell cell3 = new Cell();
    Cell cell4 = new Cell();
    Ship ship = new Ship(ShipType.DESTROYER, new Posn(0, 0), new Posn(0, 3));
    ship.addCell(cell1);
    ship.addCell(cell2);
    ship.addCell(cell3);
    ship.addCell(cell4);
    cell1.attack(true);
    cell2.attack(true);
    cell3.attack(true);
    cell4.attack(true);
    assertTrue(ship.isDestroyed());
  }

  /**
   * tests that a ship is not destroyed if only some of it was hit
   */
  @Test
  void testIsDestroyed3() {
    Cell cell1 = new Cell();
    Cell cell2 = new Cell();
    Cell cell3 = new Cell();
    Ship ship = new Ship(ShipType.SUBMARINE, new Posn(0, 0), new Posn(0, 2));
    ship.addCell(cell1);
    ship.addCell(cell2);
    ship.addCell(cell3);
    cell1.attack(true);
    assertFalse(ship.isDestroyed());
  }

  /**
   * test that a cell is added to a ship
   */
  @Test
  void testAddCell() {
    Cell cell1 = new Cell();
    Ship ship = new Ship(ShipType.PLACEHOLDER, new Posn(0, 0), new Posn(0, 0));
    ship.addCell(cell1);
    cell1.attack(true);
    assertTrue(ship.isDestroyed());
  }

  /**
   * tests that you can't add the same cell to a ship twice
   */
  @Test
  void testAddCell2() {
    Ship ship = new Ship(ShipType.PLACEHOLDER, new Posn(0, 0), new Posn(0, 0));
    ship.addCell(new Cell());
    assertThrows(IllegalStateException.class, () -> ship.addCell(new Cell()));
  }
}