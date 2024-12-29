package Model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GameHistoryWrapper {
    @JsonProperty("games")
    private List<GameSummary> games;

    // No-argument constructor (required for Jackson deserialization)
    public GameHistoryWrapper() {}

    // Constructor with a list of games
    public GameHistoryWrapper(List<GameSummary> games) {
        this.games = games;
    }

    // Getters and setters
    public List<GameSummary> getGames() {
        return games;
    }

    public void setGames(List<GameSummary> games) {
        this.games = games;
    }
}
