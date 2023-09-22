package cs3500.pa04.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa03.model.board.ShipType;
import java.util.Map;

/**
 * represents a message that requests the setup of a board
 *
 * @param width     the width of the board
 * @param height    the height of the board
 * @param fleetSpec the number of each shipType to place on the board
 */
public record SetupJsonRequest(
    @JsonProperty("width") int width,
    @JsonProperty("height") int height,
    @JsonProperty("fleet-spec") Map<ShipType, Integer> fleetSpec) {
}
