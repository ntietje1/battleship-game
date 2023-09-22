package cs3500.pa04;

import cs3500.pa03.controller.GameController;
import cs3500.pa03.controller.ProxyController;
import cs3500.pa03.model.player.BotPlayer;
import cs3500.pa03.model.player.ManualPlayer;
import cs3500.pa03.model.player.Player;
import cs3500.pa03.view.ConsoleView;
import cs3500.pa03.view.View;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * This is the main driver of this project.
 */
public class Driver {
  /**
   * Project entry point
   *
   * @param args - no command line args required
   */
  public static void main(String[] args) {
    try {
      if (args.length == 2) {
        // set up server
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        Socket server = new Socket(host, port);
        // local CPU vs. server CPU
        Readable readable = new InputStreamReader(System.in);
        View view = new ConsoleView(System.out, readable);
        Player botPlayer = new BotPlayer();
        ProxyController pc = new ProxyController(server, botPlayer, view);
        pc.run();

      } else if (args.length == 0) {
        // human vs. CPU game
        Readable readable = new InputStreamReader(System.in);
        ConsoleView view = new ConsoleView(System.out, readable);
        Player manualPlayer = new ManualPlayer(view.getManualDataGatherer());
        Player botPlayer = new BotPlayer();
        GameController gameController = new GameController(view, manualPlayer, botPlayer);
        gameController.run();
      } else {
        System.out.println("Expected no arguments or two arguments: `[host] [port]`.");
      }
    } catch (IOException | IllegalStateException e) {
      System.out.println("Unable to connect to the server.");
    } catch (NumberFormatException e) {
      System.out.println("Second argument should be an integer. Format: `[host] [part]`.");
    }
  }

}