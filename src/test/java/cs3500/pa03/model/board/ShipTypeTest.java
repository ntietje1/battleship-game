package cs3500.pa03.model.board;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * represents tests for the ShipType enum
 */
class ShipTypeTest {

  /**
   * tests the getSize method
   */
  @Test
  void getSize() {
    ShipType carrier = ShipType.CARRIER;
    assertEquals(6, carrier.size());
    ShipType battleship = ShipType.BATTLESHIP;
    assertEquals(5, battleship.size());
    ShipType destroyer = ShipType.DESTROYER;
    assertEquals(4, destroyer.size());
    ShipType submarine = ShipType.SUBMARINE;
    assertEquals(3, submarine.size());
  }
}