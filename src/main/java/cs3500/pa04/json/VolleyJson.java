package cs3500.pa04.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa03.model.board.Posn;
import java.util.List;

/**
 * represents a way to communicate a volley of shots
 *
 * @param shots the shots in the volley
 */
public record VolleyJson(
    @JsonProperty("coordinates") List<Posn> shots) {
}
