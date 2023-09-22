package cs3500.pa04.json;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * represents a message in response to a join request
 *
 * @param username the player's username
 * @param gameType the type of game the user wants to play
 */
public record JoinJsonResponse(
    @JsonProperty("name") String username,
    @JsonProperty("game-type") GameType gameType) {
}
