package cs3500.pa03.controller;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cs3500.pa03.model.board.Posn;
import cs3500.pa03.model.board.Ship;
import cs3500.pa03.model.player.Player;
import cs3500.pa03.view.View;
import cs3500.pa04.json.EndGameJson;
import cs3500.pa04.json.FleetJson;
import cs3500.pa04.json.GameType;
import cs3500.pa04.json.JoinJsonResponse;
import cs3500.pa04.json.JsonUtils;
import cs3500.pa04.json.MessageJson;
import cs3500.pa04.json.SetupJsonRequest;
import cs3500.pa04.json.ShipAdapter;
import cs3500.pa04.json.VolleyJson;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * represents a proxy controller for a battleship game on a server
 */
public class ProxyController implements Controller {

  private final View view;
  Socket server;
  private final InputStream in;
  private final PrintStream out;
  Player player;
  ObjectMapper mapper;

  /**
   * constructor for a proxy controller
   *
   * @param server socket for the server
   * @param player the player that will play the game
   * @param view   a way to view the game
   */
  public ProxyController(Socket server, Player player, View view) {
    this.view = view;
    this.server = server;
    try {
      this.in = server.getInputStream();
      this.out = new PrintStream(server.getOutputStream());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    this.player = player;
    mapper = new ObjectMapper();
  }


  /**
   * runs a game of BattleSalvo
   */
  public void run() {
    try {
      JsonParser parser = this.mapper.getFactory().createParser(this.in);

      while (!this.server.isClosed()) {
        MessageJson message = parser.readValueAs(MessageJson.class);
        delegateMessage(message);
      }
    } catch (IOException e) {
      // Disconnected from server or parsing exception
    }
  }

  /**
   * Determines the type of request the server has sent and delegates to the
   * corresponding helper method with the message arguments
   *
   * @param message the MessageJSON used to determine what the server has sent
   */
  private void delegateMessage(MessageJson message) {
    String name = message.messageName();
    JsonNode arguments = message.arguments();

    if (name.equals("join")) {
      handleJoin(arguments);
    } else if (name.equals("setup")) {
      handleSetup(arguments);
    } else if (name.equals("take-shots")) {
      handleTakeShots(arguments);
    } else if (name.equals("report-damage")) {
      handleReportDamage(arguments);
    } else if (name.equals("successful-hits")) {
      handleSuccessfulHits(arguments);
    } else if (name.equals("end-game")) {
      handleEndGame(arguments);
    } else {
      throw new IllegalStateException("Invalid message name");
    }
  }

  /**
   * responds to a join request from the server
   *
   * @param args request arguments
   */
  private void handleJoin(JsonNode args) {
    GameType gameType = GameType.MULTI;
    JoinJsonResponse response = new JoinJsonResponse(this.player.name(), gameType);

    JsonNode jsonResponse = JsonUtils.serializeRecord(response);
    MessageJson message = new MessageJson("join", jsonResponse);
    this.out.println(JsonUtils.serializeRecord(message));
  }

  /**
   * responds to a setup request from the server
   *
   * @param args request arguments
   */
  private void handleSetup(JsonNode args) {
    SetupJsonRequest setupMessage = this.mapper.convertValue(args, SetupJsonRequest.class);

    List<Ship> fleet =
        this.player.setup(setupMessage.height(), setupMessage.width(), setupMessage.fleetSpec());

    List<ShipAdapter> ships = new ArrayList<ShipAdapter>();
    for (Ship ship : fleet) {
      ships.add(new ShipAdapter(ship));
    }

    FleetJson response = new FleetJson(ships);
    JsonNode jsonResponse = JsonUtils.serializeRecord(response);
    MessageJson message = new MessageJson("setup", jsonResponse);
    this.out.println(JsonUtils.serializeRecord(message));
  }

  /**
   * responds to a take-shots request from the server
   *
   * @param args request arguments
   */
  private void handleTakeShots(JsonNode args) {
    List<Posn> shots = this.player.takeShots();

    VolleyJson volley = new VolleyJson(shots);
    JsonNode jsonResponse = JsonUtils.serializeRecord(volley);
    MessageJson message = new MessageJson("take-shots", jsonResponse);
    this.out.println(JsonUtils.serializeRecord(message));
  }

  /**
   * responds to a report-damage request from the server
   *
   * @param args request arguments
   */
  private void handleReportDamage(JsonNode args) {
    VolleyJson incomingVolley = this.mapper.convertValue(args, VolleyJson.class);

    List<Posn> shots = this.player.reportDamage(incomingVolley.shots());

    VolleyJson outgoingVolley = new VolleyJson(shots);
    JsonNode jsonResponse = JsonUtils.serializeRecord(outgoingVolley);
    MessageJson message = new MessageJson("report-damage", jsonResponse);
    this.out.println(JsonUtils.serializeRecord(message));
  }

  /**
   * responds to a successful-hits request from the server
   *
   * @param args request arguments
   */
  private void handleSuccessfulHits(JsonNode args) {
    VolleyJson hits = this.mapper.convertValue(args, VolleyJson.class);
    this.player.successfulHits(hits.shots());

    JsonNode voidResponse = new ObjectMapper().createObjectNode();
    MessageJson message = new MessageJson("successful-hits", voidResponse);
    this.out.println(JsonUtils.serializeRecord(message));
  }

  /**
   * responds to an end-game request from the server
   *
   * @param args request arguments
   */
  private void handleEndGame(JsonNode args) {
    EndGameJson endGame = this.mapper.convertValue(args, EndGameJson.class);
    this.player.endGame(endGame.result(), endGame.reason());
    try {
      this.server.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    JsonNode voidResponse = new ObjectMapper().createObjectNode();
    MessageJson message = new MessageJson("end-game", voidResponse);
    this.out.println(JsonUtils.serializeRecord(message));
  }
}
