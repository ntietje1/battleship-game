package cs3500.pa03.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cs3500.pa03.model.board.Posn;
import cs3500.pa03.model.board.ShipType;
import cs3500.pa03.model.player.BotPlayer;
import cs3500.pa03.model.player.GameResult;
import cs3500.pa03.view.ConsoleView;
import cs3500.pa04.json.EndGameJson;
import cs3500.pa04.json.JsonUtils;
import cs3500.pa04.json.MessageJson;
import cs3500.pa04.json.SetupJsonRequest;
import cs3500.pa04.json.VolleyJson;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * represents tests for the ProxyController class
 */
class ProxyControllerTest {
  private ByteArrayOutputStream testLog;
  private ProxyController controller;

  /**
   * Reset the test log before each test is run.
   */
  @BeforeEach
  public void setup() {
    this.testLog = new ByteArrayOutputStream(2048);
    assertEquals("", logToString());
  }

  /**
   * Converts the ByteArrayOutputStream log to a string in UTF_8 format
   *
   * @return String representing the current log buffer
   */
  private String logToString() {
    return testLog.toString(StandardCharsets.UTF_8);
  }

  /**
   * Try converting the current test log to a string of a certain class.
   *
   * @param classRef Type to try converting the current test stream to.
   * @param <T>      Type to try converting the current test stream to.
   */
  private <T> void responseToClass(@SuppressWarnings("SameParameterValue") Class<T> classRef) {
    try {
      JsonParser jsonParser = new ObjectMapper().createParser(logToString());
      jsonParser.readValueAs(classRef);
      // No error thrown when parsing to a GuessJson, test passes!
    } catch (IOException e) {
      // Could not read
      // -> exception thrown
      // -> test fails since it must have been the wrong type of response.
      fail();
    }
  }

  /**
   * Create a MessageJson for some name and arguments.
   *
   * @param messageName   name of the type of message; "hint" or "win"
   * @param messageObject object to embed in a message json
   * @return a MessageJson for the object
   */
  private JsonNode createSampleMessage(String messageName, Record messageObject) {
    MessageJson messageJson =
        new MessageJson(messageName, JsonUtils.serializeRecord(messageObject));
    return JsonUtils.serializeRecord(messageJson);
  }

  /**
   * tests the run method response to a join request
   */
  @Test
  void testRunJoinMessage() {
    JsonNode voidResponse = new ObjectMapper().createObjectNode();
    MessageJson joinRequest = new MessageJson("join", voidResponse);
    Mocket socket =
        new Mocket(this.testLog, List.of(JsonUtils.serializeRecord(joinRequest).toString()));

    String gameInput = "6 6\n" + "1 1 1 1\n";
    Appendable output = new StringBuilder();
    Readable readable = new StringReader(gameInput);
    ConsoleView view = new ConsoleView(output, readable);
    this.controller = new ProxyController(socket, new BotPlayer(), view);

    this.controller.run();
    responseToClass(MessageJson.class);
    assertTrue(logToString().contains("{\"method-name\":\"join\",\""
        + "arguments\":{\"name\":\"auraHerce\",\"game-type\":\""));
    System.out.println(logToString());
  }

  /**
   * tests the run method response to a setup request
   */
  @Test
  void testRunSetupMessage() {
    HashMap<ShipType, Integer> specifications = new HashMap<>();
    specifications.put(ShipType.CARRIER, 1);
    specifications.put(ShipType.BATTLESHIP, 1);
    specifications.put(ShipType.DESTROYER, 1);
    specifications.put(ShipType.SUBMARINE, 1);
    SetupJsonRequest setupRequest = new SetupJsonRequest(6, 8, specifications);
    JsonNode jsonNode = createSampleMessage("setup", setupRequest);

    Mocket socket = new Mocket(this.testLog, List.of(jsonNode.toString()));

    String gameInput = "6 6\n" + "1 1 1 1\n";
    Appendable output = new StringBuilder();
    Readable readable = new StringReader(gameInput);
    ConsoleView view = new ConsoleView(output, readable);
    this.controller = new ProxyController(socket, new BotPlayer(), view);

    this.controller.run();
    responseToClass(MessageJson.class);
    assertTrue(logToString().contains("{\"method-name\":\"setup\",\"arguments\":{\"fleet\":"
        + "[{\"coord\":{"));
    assertTrue(logToString().contains("\"length\":6,"));
    assertTrue(logToString().contains("\"length\":5,"));
    assertTrue(logToString().contains("\"length\":4,"));
    assertTrue(logToString().contains("\"length\":3,"));
    assertTrue(logToString().contains("\"direction\":"));
    System.out.println(logToString());
  }

  /**
   * tests the run method response to a take-shots request
   */
  @Test
  void testRunTakeShotsMessage() {
    HashMap<ShipType, Integer> specifications = new HashMap<>();
    specifications.put(ShipType.CARRIER, 1);
    specifications.put(ShipType.BATTLESHIP, 1);
    specifications.put(ShipType.DESTROYER, 1);
    specifications.put(ShipType.SUBMARINE, 1);
    SetupJsonRequest setupRequest = new SetupJsonRequest(6, 8, specifications);
    JsonNode jsonNode = createSampleMessage("setup", setupRequest);

    JsonNode voidResponse = new ObjectMapper().createObjectNode();
    MessageJson joinRequest = new MessageJson("take-shots", voidResponse);
    Mocket socket = new Mocket(this.testLog, List.of(jsonNode.toString(),
        JsonUtils.serializeRecord(joinRequest).toString()));

    String gameInput = "6 6\n" + "1 1 1 1\n";
    Appendable output = new StringBuilder();
    Readable readable = new StringReader(gameInput);
    ConsoleView view = new ConsoleView(output, readable);
    this.controller = new ProxyController(socket, new BotPlayer(), view);

    this.controller.run();
    responseToClass(MessageJson.class);
    assertTrue(logToString().contains("\"method-name\""
        + ":\"take-shots\",\"arguments\":{\"coordinates\":"));
    System.out.println(logToString());
  }

  /**
   * tests the run method response to a report-damage request
   */
  @Test
  void testRunReportDamageMessage() {
    HashMap<ShipType, Integer> specifications = new HashMap<>();
    specifications.put(ShipType.CARRIER, 1);
    specifications.put(ShipType.BATTLESHIP, 1);
    specifications.put(ShipType.DESTROYER, 1);
    specifications.put(ShipType.SUBMARINE, 1);
    SetupJsonRequest setupRequest = new SetupJsonRequest(6, 8, specifications);
    JsonNode setup = createSampleMessage("setup", setupRequest);

    List<Posn> shots = List.of(new Posn(1, 1), new Posn(1, 2));
    VolleyJson incomingVolley = new VolleyJson(shots);
    JsonNode jsonNode = createSampleMessage("report-damage", incomingVolley);
    Mocket socket =
        new Mocket(this.testLog, List.of(setup.toString(), jsonNode.toString()));

    String gameInput = "6 6\n" + "1 1 1 1\n";
    Appendable output = new StringBuilder();
    Readable readable = new StringReader(gameInput);
    ConsoleView view = new ConsoleView(output, readable);
    this.controller = new ProxyController(socket, new BotPlayer(), view);

    this.controller.run();
    responseToClass(MessageJson.class);
    assertTrue(logToString().contains("{\"method-name\":\"report-damage\",\""
        + "arguments\":{\"coordinates\":["));
    System.out.println(logToString());
  }

  /**
   * tests the run method response to a successful-hits request
   */
  @Test
  void testRunSuccessfulHitsMessage() {
    HashMap<ShipType, Integer> specifications = new HashMap<>();
    specifications.put(ShipType.CARRIER, 1);
    specifications.put(ShipType.BATTLESHIP, 1);
    specifications.put(ShipType.DESTROYER, 1);
    specifications.put(ShipType.SUBMARINE, 1);
    SetupJsonRequest setupRequest = new SetupJsonRequest(6, 8, specifications);
    JsonNode setup = createSampleMessage("setup", setupRequest);

    List<Posn> shots = List.of(new Posn(1, 1), new Posn(1, 2));
    VolleyJson incomingVolley = new VolleyJson(shots);
    JsonNode jsonNode = createSampleMessage("successful-hits", incomingVolley);
    Mocket socket =
        new Mocket(this.testLog, List.of(setup.toString(), jsonNode.toString()));

    String gameInput = "6 6\n" + "1 1 1 1\n";
    Appendable output = new StringBuilder();
    Readable readable = new StringReader(gameInput);
    ConsoleView view = new ConsoleView(output, readable);
    this.controller = new ProxyController(socket, new BotPlayer(), view);

    this.controller.run();
    responseToClass(MessageJson.class);
    assertTrue(logToString().contains("{\"method-name\":\"successful-hits\",\"arguments\":{}}"));
  }

  /**
   * tests the run method response to an end-game request
   */
  @Test
  void testRunEndGameMessage() {
    EndGameJson endGameRequest = new EndGameJson(GameResult.WIN, "winner");
    JsonNode jsonNode = createSampleMessage("end-game", endGameRequest);
    Mocket socket =
        new Mocket(this.testLog, List.of(jsonNode.toString()));

    String gameInput = "6 6\n" + "1 1 1 1\n";
    Appendable output = new StringBuilder();
    Readable readable = new StringReader(gameInput);
    ConsoleView view = new ConsoleView(output, readable);
    this.controller = new ProxyController(socket, new BotPlayer(), view);

    this.controller.run();
    responseToClass(MessageJson.class);
    assertTrue(logToString().contains("{\"method-name\":\"end-game\",\"arguments\":{}}"));
  }

  /**
   * tests the run method response to an invalid message name
   */
  @Test
  void testRunInvalidMessage() {
    JsonNode voidResponse = new ObjectMapper().getNodeFactory().textNode("void");
    MessageJson joinRequest = new MessageJson("invalid", voidResponse);
    Mocket socket =
        new Mocket(this.testLog, List.of(JsonUtils.serializeRecord(joinRequest).toString()));

    String gameInput = "6 6\n" + "1 1 1 1\n";
    Appendable output = new StringBuilder();
    Readable readable = new StringReader(gameInput);
    ConsoleView view = new ConsoleView(output, readable);
    this.controller = new ProxyController(socket, new BotPlayer(), view);

    assertThrows(IllegalStateException.class, () -> this.controller.run());
  }
}