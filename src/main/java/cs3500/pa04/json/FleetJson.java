package cs3500.pa04.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa03.model.board.Ship;
import java.util.List;

/**
 * represents a way to communicate a fleet of ships
 *
 * @param ships the ships in the fleet of ships
 */
public record FleetJson(
    @JsonProperty("fleet") List<ShipAdapter> ships) {
}
