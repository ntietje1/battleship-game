package cs3500.pa03.model.player;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import cs3500.pa03.model.board.Posn;
import cs3500.pa03.model.board.ShipType;
import cs3500.pa03.view.ConsoleView;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * represents tests for the BotPlayer class
 */
class BotPlayerTest {

  /**
   * tests the name method
   */
  @Test
  void testName() {
    Player player = new BotPlayer();
    assertEquals("auraHerce", player.name());
  }

  /**
   * tests the takeShots method
   */
  @Test
  void botTakeShots() {
    HashMap<ShipType, Integer> specifications = new HashMap<>();
    specifications.put(ShipType.CARRIER, 1);
    specifications.put(ShipType.BATTLESHIP, 1);
    specifications.put(ShipType.DESTROYER, 1);
    specifications.put(ShipType.SUBMARINE, 1);
    Player player = new BotPlayer();
    player.setup(6, 6, specifications);
    assertEquals(4, player.takeShots().size());
  }

  /**
   * tests the takeShots method
   */
  @Test
  void botTakeShots2() {
    HashMap<ShipType, Integer> specifications = new HashMap<>();
    Player player = new BotPlayer();
    player.setup(6, 6, specifications);
    assertEquals(0, player.takeShots().size());
  }

  /**
   * tests the takeShots method
   */
  @Test
  void botTakeShots3() {
    HashMap<ShipType, Integer> specifications = new HashMap<>();
    specifications.put(ShipType.CARRIER, 1);
    specifications.put(ShipType.BATTLESHIP, 1);
    specifications.put(ShipType.DESTROYER, 1);
    specifications.put(ShipType.SUBMARINE, 1);
    Player player = new BotPlayer();
    player.setup(6, 6, specifications);
    List<Posn> shots1 = new ArrayList<>(player.takeShots());
    List<Posn> shots2 = new ArrayList<>(player.takeShots());
    assertNotEquals(shots1, shots2);
  }
}