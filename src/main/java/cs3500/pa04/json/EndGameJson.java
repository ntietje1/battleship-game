package cs3500.pa04.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa03.model.player.GameResult;

/**
 * represents an endGame message
 *
 * @param result the result of the game
 * @param reason the reason for the game ending
 */
public record EndGameJson(
    @JsonProperty("result") GameResult result,
    @JsonProperty("reason") String reason) {
}
