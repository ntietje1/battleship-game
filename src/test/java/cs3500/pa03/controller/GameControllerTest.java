package cs3500.pa03.controller;


import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cs3500.pa03.model.player.BotPlayer;
import cs3500.pa03.model.player.ManualPlayer;
import cs3500.pa03.model.player.Player;
import cs3500.pa03.view.ConsoleView;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

/**
 * Holds test methods for game controller
 */
class GameControllerTest {

  private String generateGameInput(int width, int height) {
    StringBuilder gameInput = new StringBuilder();
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        gameInput.append(i).append(" ").append(j).append("\n");
      }
    }
    return gameInput.toString();
  }

  @Test
  void testAskSizeBadInput() {
    String gameInput
        = "0 -1\n"
        + "10 10\n"
        + "1 1 1 1\n"
        + generateGameInput(10, 10);

    Appendable output = new StringBuilder();
    Readable readable = new StringReader(gameInput);
    ConsoleView view = new ConsoleView(output, readable);
    Player player1 = new ManualPlayer(view.getManualDataGatherer());
    Player player2 = new BotPlayer();
    GameController gameController = new GameController(view, player1, player2);
    gameController.run();
    assertTrue(output.toString().contains(
        "One or more of these dimensions is invalid. Dimensions must be in the range [6, 15].\n"));
  }

  @Test
  void testAskFleetBadInput() {
    String gameInput
        = "10 10\n"
        + "10 0 1 1\n"
        + "1 1 1 1\n"
        + generateGameInput(10, 10);

    Appendable output = new StringBuilder();
    Readable readable = new StringReader(gameInput);
    ConsoleView view = new ConsoleView(output, readable);
    Player player1 = new ManualPlayer(view.getManualDataGatherer());
    Player player2 = new BotPlayer();
    GameController gameController = new GameController(view, player1, player2);
    gameController.run();
    assertTrue(output.toString().contains(
        "This list of ship types if invalid.\n"
        + "You must use at least one of each ship type "
        + "and not exceed the maximum fleet size.\n"));
  }

  @Test
  void run() {
    String gameInput
        = "6 6\n"
        + "1 1 1 1\n"
        + generateGameInput(6, 6);
    Appendable output = new StringBuilder();
    Readable readable = new StringReader(gameInput.toString());
    ConsoleView view = new ConsoleView(output, readable);
    Player player1 = new ManualPlayer(view.getManualDataGatherer());
    Player player2 = new BotPlayer();
    GameController gameController = new GameController(view, player1, player2);
    assertDoesNotThrow(gameController::run);
  }

  @Test
  void run2() {
    String gameInput
        = "6 6\n"
        + "1 1 1 1\n"
        + generateGameInput(6, 6);
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(outContent);
    System.setOut(out);

    Appendable output = new StringBuilder();
    Readable readable = new StringReader(gameInput.toString());
    ConsoleView view = new ConsoleView(output, readable);
    Player player1 = new ManualPlayer(view.getManualDataGatherer());
    Player player2 = new BotPlayer();
    GameController gameController = new GameController(view, player1, player2);
    gameController.run();
    System.setOut(System.out);
    assertTrue(outContent.toString().contains("Nick's manual player has ended the game with")
        && outContent.toString().contains("auraHerce has ended the game with"));
  }

  @Test
  void run3() {
    String gameInput
        = "15 15\n"
        + "2 2 2 2\n"
        + generateGameInput(15, 15);
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(outContent);
    System.setOut(out);

    Appendable output = new StringBuilder();
    Readable readable = new StringReader(gameInput.toString());
    ConsoleView view = new ConsoleView(output, readable);
    Player player1 = new ManualPlayer(view.getManualDataGatherer());
    Player player2 = new BotPlayer();
    GameController gameController = new GameController(view, player1, player2);
    gameController.run();
    System.setOut(System.out);
    assertTrue(outContent.toString().contains("Nick's manual player has ended the game with")
        && outContent.toString().contains("auraHerce has ended the game with"));
  }

  @Test
  void run4() {
    String gameInput
        = "15 10\n"
        + "2 2 2 2\n"
        + generateGameInput(15, 10);
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(outContent);
    System.setOut(out);

    Appendable output = new StringBuilder();
    Readable readable = new StringReader(gameInput.toString());
    ConsoleView view = new ConsoleView(output, readable);
    Player player2 = new ManualPlayer(view.getManualDataGatherer());
    Player player1 = new BotPlayer();
    GameController gameController = new GameController(view, player1, player2);
    gameController.run();
    System.setOut(System.out);
    assertTrue(outContent.toString().contains("Nick's manual player has ended the game with")
        && outContent.toString().contains("auraHerce has ended the game with"));
  }
}