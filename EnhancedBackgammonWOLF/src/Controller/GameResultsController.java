package Controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import Model.GameHistoryWrapper;
import Model.GameSummary;

public class GameResultsController {
    private static final String RESULTS_FILE_PATH = "src/gamehistory.json"; // Path to JSON file
    private final ObjectMapper objectMapper;

    // Constructor
    public GameResultsController() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.findAndRegisterModules(); // Register JavaTimeModule for LocalDateTime
    }

    // Load game history
    public List<GameSummary> loadGameHistory() {
        try {
            File file = new File(RESULTS_FILE_PATH);
            if (!file.exists()) {
                System.out.println("Game history file not found. Initializing new history.");
                return new ArrayList<>(); // Return an empty list if the file doesn't exist
            }

            GameHistoryWrapper wrapper = objectMapper.readValue(file, GameHistoryWrapper.class);
            return wrapper.getGames();
        } catch (IOException e) {
            System.out.println("Failed to load game history: " + e.getMessage());
            System.out.println("Attempting to reset game history.");
            resetGameHistory();
            return new ArrayList<>();
        }
    }

    // Save game history
    public void saveGameHistory(List<GameSummary> gameHistory) {
        try {
            GameHistoryWrapper wrapper = new GameHistoryWrapper(gameHistory);
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(RESULTS_FILE_PATH), wrapper);
            System.out.println("Game history saved successfully!");
        } catch (IOException e) {
            System.out.println("Failed to save game history: " + e.getMessage());
        }
    }

    // Add a game result to the history
    public void addGameResult(GameSummary newResult) {
        List<GameSummary> gameHistory = loadGameHistory();
        gameHistory.add(newResult);
        saveGameHistory(gameHistory);
    }

    // Display game history
    public void displayGameHistory() {
        List<GameSummary> gameHistory = loadGameHistory();
        if (gameHistory.isEmpty()) {
            System.out.println("No game history available.");
        } else {
            System.out.println("\n=== Game History ===");
            gameHistory.forEach(System.out::println);
        }
    }

    // Reset game history in case of errors
    private void resetGameHistory() {
        try {
            GameHistoryWrapper emptyWrapper = new GameHistoryWrapper(new ArrayList<>());
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(RESULTS_FILE_PATH), emptyWrapper);
            System.out.println("Game history reset to an empty state.");
        } catch (IOException e) {
            System.out.println("Failed to reset game history: " + e.getMessage());
        }
    }
}
