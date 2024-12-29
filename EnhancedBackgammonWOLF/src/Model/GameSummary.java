package Model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GameSummary {
    @JsonProperty("player1")
    private String player1;

    @JsonProperty("player2")
    private String player2;

    @JsonProperty("difficulty")
    private String difficulty;

    @JsonProperty("winnerName")
    private String winnerName;

    @JsonProperty("durationInSeconds")
    private long durationInSeconds;

    @JsonProperty("gameStartTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gameStartTime;

    @JsonProperty("gameEndTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gameEndTime;

    // No-argument constructor (required for Jackson deserialization)
    public GameSummary() {}

    // Constructor
    public GameSummary(String player1, String player2, String difficulty, String winnerName,
                       long durationInSeconds, LocalDateTime gameStartTime, LocalDateTime gameEndTime) {
        this.player1 = player1;
        this.player2 = player2;
        this.difficulty = difficulty;
        this.winnerName = winnerName;
        this.durationInSeconds = durationInSeconds;
        this.gameStartTime = gameStartTime;
        this.gameEndTime = gameEndTime;
    }

    // Getters and setters
    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getWinnerName() {
        return winnerName;
    }

    public void setWinnerName(String winnerName) {
        this.winnerName = winnerName;
    }

    public long getDurationInSeconds() {
        return durationInSeconds;
    }

    public void setDurationInSeconds(long durationInSeconds) {
        this.durationInSeconds = durationInSeconds;
    }

    public LocalDateTime getGameStartTime() {
        return gameStartTime;
    }

    public void setGameStartTime(LocalDateTime gameStartTime) {
        this.gameStartTime = gameStartTime;
    }

    public LocalDateTime getGameEndTime() {
        return gameEndTime;
    }

    public void setGameEndTime(LocalDateTime gameEndTime) {
        this.gameEndTime = gameEndTime;
    }

    @Override
    public String toString() {
        return "GameSummary{" +
                "player1='" + player1 + '\'' +
                ", player2='" + player2 + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", winnerName='" + winnerName + '\'' +
                ", durationInSeconds=" + durationInSeconds +
                ", gameStartTime=" + gameStartTime +
                ", gameEndTime=" + gameEndTime +
                '}';
    }
}
