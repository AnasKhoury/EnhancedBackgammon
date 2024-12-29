package Controller;

import Model.Question;
import Model.QuestionsWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class QuestionControl {

    // JavaFX FXML Components
    @FXML
    private ListView<String> questionsListView;

    @FXML
    private Button easyCategoryBtn;

    @FXML
    private Button mediumCategoryBtn;

    @FXML
    private Button hardCategoryBtn;

    @FXML
    private Button addQuestionBtn;

    // List to store all questions
    private List<Question> questions;
    private final String jsonFilePath = "src/question.json";

    public QuestionControl() {
        questions = new ArrayList<>();
    }

    // ==================== Initialization for JavaFX ====================
    @FXML
    public void initialize() {
        loadQuestions(); // Load questions on initialization
        displayQuestionsInListView();
    }

    private void displayQuestionsInListView() {
        if (questionsListView != null) {
            questionsListView.getItems().clear();
            for (Question q : questions) {
                questionsListView.getItems().add(q.getQuestion());
            }
        }
    }

    // ==================== JavaFX Event Handlers ====================
    @FXML
    public void handleEasyCategory(ActionEvent event) {
        filterQuestionsByDifficulty("EASY");
    }

    @FXML
    public void handleMediumCategory(ActionEvent event) {
        filterQuestionsByDifficulty("MEDIUM");
    }

    @FXML
    public void handleHardCategory(ActionEvent event) {
        filterQuestionsByDifficulty("HARD");
    }

    @FXML
    public void handleAddQuestion(ActionEvent event) {
        showAlert("Add Question", "Add Question functionality will be implemented here!");
    }

    private void filterQuestionsByDifficulty(String difficulty) {
        questionsListView.getItems().clear();
        for (Question q : questions) {
            if (q.getDifficulty().equalsIgnoreCase(difficulty)) {
                questionsListView.getItems().add(q.getQuestion());
            }
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // ==================== CLI Methods ====================
    public void displayQuestions() {
        if (questions.isEmpty()) {
            System.out.println("No questions loaded.");
        } else {
            for (int i = 0; i < questions.size(); i++) {
                Question q = questions.get(i);
                System.out.println((i + 1) + ". " + q.getQuestion());
                System.out.println("   Answers: " + q.getAnswers());
                System.out.println("   Correct Answer: " + q.getCorrectAns());
                System.out.println("   Difficulty: " + q.getDifficulty());
            }
        }
    }

    public void addQuestion(Scanner scanner) {
        System.out.print("Enter the question text: ");
        String questionText = scanner.nextLine();

        List<String> answers = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            System.out.print("Answer " + (i + 1) + ": ");
            answers.add(scanner.nextLine());
        }

        System.out.print("Enter the correct answer: ");
        String correctAnswer = scanner.nextLine();

        System.out.print("Enter the difficulty (EASY, MEDIUM, HARD): ");
        String difficulty = scanner.nextLine().toUpperCase();

        Question newQuestion = new Question();
        newQuestion.setQuestion(questionText);
        newQuestion.setAnswers(answers);
        newQuestion.setCorrectAns(correctAnswer);
        newQuestion.setDifficulty(difficulty);

        questions.add(newQuestion);
        saveQuestions();
        System.out.println("Question added successfully!");
    }

    public void deleteQuestion(Scanner scanner) {
        displayQuestions();
        System.out.print("Enter the question number to delete: ");
        int index = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (index > 0 && index <= questions.size()) {
            questions.remove(index - 1);
            saveQuestions();
            System.out.println("Question deleted successfully.");
        } else {
            System.out.println("Invalid question number.");
        }
    }

    public void editQuestion(Scanner scanner) {
        displayQuestions();
        System.out.print("Enter the question number to edit: ");
        int index = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (index > 0 && index <= questions.size()) {
            Question questionToEdit = questions.get(index - 1);

            System.out.print("Enter new question text (or press Enter to keep current): ");
            String newText = scanner.nextLine();
            if (!newText.isEmpty()) questionToEdit.setQuestion(newText);

            for (int i = 0; i < 4; i++) {
                System.out.print("New Answer " + (i + 1) + " (or press Enter to keep current): ");
                String answer = scanner.nextLine();
                if (!answer.isEmpty()) {
                    questionToEdit.getAnswers().set(i, answer);
                }
            }

            System.out.print("Enter new correct answer (or press Enter to keep current): ");
            String correctAnswer = scanner.nextLine();
            if (!correctAnswer.isEmpty()) questionToEdit.setCorrectAns(correctAnswer);

            System.out.print("Enter new difficulty (EASY, MEDIUM, HARD or press Enter to keep current): ");
            String difficulty = scanner.nextLine().toUpperCase();
            if (!difficulty.isEmpty()) questionToEdit.setDifficulty(difficulty);

            saveQuestions();
            System.out.println("Question updated successfully!");
        } else {
            System.out.println("Invalid question number.");
        }
    }

    // ==================== Utility Methods ====================
    public void loadQuestions() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File(jsonFilePath);

            if (file.exists()) {
                // Use QuestionWrapper to match your JSON file structure
            	QuestionsWrapper wrapper = objectMapper.readValue(file, QuestionsWrapper.class);
                questions = wrapper.getQuestions();
                System.out.println("Questions loaded successfully.");
            } else {
                System.out.println("File not found: " + jsonFilePath);
            }
        } catch (IOException e) {
            System.out.println("Failed to load questions: " + e.getMessage());
        }
    }

    public void saveQuestions() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            QuestionsWrapper wrapper = new QuestionsWrapper();
            wrapper.setQuestions(questions);
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(jsonFilePath), wrapper);
            System.out.println("Questions saved successfully.");
        } catch (IOException e) {
            System.out.println("Failed to save questions: " + e.getMessage());
        }
    }
}