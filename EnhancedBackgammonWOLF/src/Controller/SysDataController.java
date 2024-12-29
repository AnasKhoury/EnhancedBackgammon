package Controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Game;
import Model.Question;
import Model.SysData;

public class SysDataController {

    // Load questions from JSON and add them to the Singleton instance of SysData
    public void loadQuestionsFromJson(String jsonFilePath) {
        try {
            // Initialize ObjectMapper for JSON parsing
            ObjectMapper objectMapper = new ObjectMapper();

            // Parse the JSON file into a list of questions
            QuestionsWrapper questionsWrapper = objectMapper.readValue(new File(jsonFilePath), QuestionsWrapper.class);

            // Get the Singleton instance of SysData
            SysData sysData = SysData.getInstance();

            // Add each question to the SysData instance using streams
            questionsWrapper.getQuestions().stream()
                    .forEach(sysData::addQuestion);

//            System.out.println("Questions loaded successfully.");
//            sysData.getQuestions().stream().forEach(System.out::println);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load questions from JSON.");
        }
    }
    public void loadGameHistoryFromJson(String jsonFilePath) {
        try {
            // Initialize ObjectMapper for JSON parsing
            ObjectMapper objectMapper = new ObjectMapper();

            // Parse the JSON file into a list of questions
            GameWrapper gameHistoryWrapper = objectMapper.readValue(new File(jsonFilePath), GameWrapper.class);

            // Get the Singleton instance of SysData
            SysData sysData = SysData.getInstance();

            // Add each question to the SysData instance using streams
            gameHistoryWrapper.getGames().stream()
                    .forEach(sysData::addGameHistory);

//            System.out.println("Questions loaded successfully.");
//            sysData.getQuestions().stream().forEach(System.out::println);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load questions from JSON.");
        }
    }
}

// Wrapper class for parsing the "questions" array in JSON
class QuestionsWrapper {
    private List<Question> questions; // Matches "questions" in JSON

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
class GameWrapper {
    private List<Game> games; // Matches "games" in JSON

    public List<Game> getGames() {
        return games;
    }

    public void setQuestions(List<Game> games) {
        this.games = games;
    }
}