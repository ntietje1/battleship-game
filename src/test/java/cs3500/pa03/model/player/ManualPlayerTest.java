package cs3500.pa03.model.player;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cs3500.pa03.model.board.ShipType;
import cs3500.pa03.view.ConsoleView;
import java.io.StringReader;
import java.util.HashMap;
import org.junit.jupiter.api.Test;

/**
 * represents tests for players
 */
class ManualPlayerTest {


  /**
   * tests the name method for a ManualPlayer
   */
  @Test
  void manualName() {
    Appendable output = new StringBuilder();
    Readable readable = new StringReader("test input");
    ConsoleView view = new ConsoleView(output, readable);
    Player player = new ManualPlayer(view.getManualDataGatherer());
    assertEquals("Nick's manual player", player.name());
  }

  /**
   * tests the takeShots method for a ManualPlayer
   */
  @Test
  void testManualTakeShots() {
    Appendable output = new StringBuilder();
    Readable readable = new StringReader(""
        + "0 0\n"
        + "0 5\n"
        + "1 0\n"
        + "1 4\n");
    ConsoleView view = new ConsoleView(output, readable);
    HashMap<ShipType, Integer> specifications = new HashMap<>();
    specifications.put(ShipType.CARRIER, 1);
    specifications.put(ShipType.BATTLESHIP, 1);
    specifications.put(ShipType.DESTROYER, 1);
    specifications.put(ShipType.SUBMARINE, 1);
    Player player = new ManualPlayer(view.getManualDataGatherer());
    player.setup(6, 6, specifications);
    player.takeShots();
    assertTrue(
        output.toString().contains("Nick's manual player, time to fire! You have 4 shots \n"
            + "Please enter the coordinates you would like to attack in the format: x y\n"
            + "Nick's manual player > Attack at (0, 0) is valid.\n"
            + "Nick's manual player > Attack at (0, 5) is valid.\n"
            + "Nick's manual player > Attack at (1, 0) is valid.\n"
            + "Nick's manual player > Attack at (1, 4) is valid.\n"
            + "Firing!!\n"));
  }


  /**
   * tests the takeShots method for a ManualPlayer
   */
  @Test
  void testManualTakeShots2() {
    Appendable output = new StringBuilder();
    Readable readable = new StringReader(""
        + "0 0\n"
        + "-1 -1\n"
        + "0 5\n"
        + "1 0\n"
        + "1 4\n"
        + "3 4\n");
    ConsoleView view = new ConsoleView(output, readable);
    HashMap<ShipType, Integer> specifications = new HashMap<>();
    specifications.put(ShipType.CARRIER, 1);
    specifications.put(ShipType.BATTLESHIP, 1);
    specifications.put(ShipType.DESTROYER, 1);
    specifications.put(ShipType.SUBMARINE, 1);
    Player player = new ManualPlayer(view.getManualDataGatherer());
    player.setup(6, 6, specifications);
    player.takeShots();
    assertTrue(
        output.toString().contains("Nick's manual player, time to fire! You have 4 shots \n"
            + "Please enter the coordinates you would like to attack in the format: x y\n"
            + "Nick's manual player > Attack at (0, 0) is valid.\n"
            + "Nick's manual player > Attack at (-1, -1) is invalid.\n"
            + "This coordinate has already been attacked or is out of bounds. Resetting shots...")
            && output.toString().contains("Nick's manual player, time to fire! You have 4 shots \n"
            + "Please enter the coordinates you would like to attack in the format: x y\n"
            + "Nick's manual player > Attack at (0, 5) is valid.\n"
            + "Nick's manual player > Attack at (1, 0) is valid.\n"
            + "Nick's manual player > Attack at (1, 4) is valid.\n"
            + "Nick's manual player > Attack at (3, 4) is valid.\n"
            + "Firing!!"));
  }
}