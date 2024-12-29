package Controller;

import Model.Question;
import Model.QuestionsWrapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    private Button deleteQuestionBtn;
    private Button QuestionBtn;
    private List<Question> questions;
    private final String jsonFilePath = "src/question.json";

    public QuestionControl() {
        questions = new ArrayList<>();
    }

    // ==================== Initialization for JavaFX ====================
    @FXML
    public void initialize() {
        loadQuestions(); // Load questions on initialization
        updateDifficultyLabels();
        displayQuestionsInListView();
        
    }

    private void displayQuestionsInListView() {
        if (questionsListView != null) {
            questionsListView.getItems().clear();
            for (int i = 0; i < questions.size(); i++) {
                Question q = questions.get(i);
                // Prepend the question number and display it in the ListView
                questionsListView.getItems().add((i + 1) + ". " + q.getQuestion());
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
   /* private void filterQuestionsByDifficulty(String difficulty) {
        // Clear the list view
        questionsListView.getItems().clear();

        // Filter questions by difficulty
        for (Question q : questions) {
            // Check if the question matches the difficulty
            if (q.getDifficulty().equalsIgnoreCase(difficulty)) {
                questionsListView.getItems().add(q.getQuestion());
            }
        }

        // Debug: Print out filtered questions
        System.out.println("Filtered Questions for " + difficulty + " category:");
        for (String questionText : questionsListView.getItems()) {
            System.out.println(questionText);
        }
    }*/


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

    @FXML
    public void editQuestion(ActionEvent event) {
        // Get the selected question from the ListView
        int selectedIndex = questionsListView.getSelectionModel().getSelectedIndex();
        
        if (selectedIndex >= 0) {
            Question questionToEdit = questions.get(selectedIndex);
            
            try {
                // Load the FXML for the edit dialog
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/EditQuestionDialog.fxml"));
                Parent root = loader.load();

                // Create a new dialog stage
                Stage dialogStage = new Stage();
                dialogStage.setTitle("Edit Question");
                dialogStage.initModality(Modality.APPLICATION_MODAL); // Make window modal
                dialogStage.setScene(new Scene(root));
                

                // Get the controller and pass the question to edit
                EditQuestionDialogController controller = loader.getController();
                controller.setQuestionToEdit(questionToEdit);
                controller.setParentController(this);

                // Show the dialog and wait for it to close
                dialogStage.showAndWait();
                
            } catch (IOException e) {
                showAlert("Error", "Could not open edit dialog: " + e.getMessage());
                System.out.print(e.getMessage());
            }
        } else {
            showAlert("No Selection", "Please select a question to edit.");
        }
    }

    @FXML
    public void deleteQuestion(ActionEvent event) {
        // Get the selected question from the ListView
        int selectedIndex = questionsListView.getSelectionModel().getSelectedIndex();
        
        if (selectedIndex >= 0) {
            // Create confirmation alert
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirm Delete");
            alert.setHeaderText("Delete Question");
            alert.setContentText("Are you sure you want to delete this question?");
            

            // Show confirmation dialog and wait for response
            Optional<ButtonType> result = alert.showAndWait();
            
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Remove the question
                questions.remove(selectedIndex);
                saveQuestions();
                displayQuestionsInListView(); // Refresh the ListView
                showAlert("Success", "Question deleted successfully.");
            }
        } else {
            showAlert("No Selection", "Please select a question to delete.");
        }
    }

    // ==================== Utility Methods ====================
    public void loadQuestions() {
        try {
//        	System.out.println("File Path: " + new File("src/question.json").getAbsolutePath());

        	System.out.println("Current Working Directory: " + System.getProperty("user.dir"));

            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File(jsonFilePath);
            System.out.println(jsonFilePath);

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
    
    
    
    @FXML
    public void handleEditQuestion(ActionEvent event) {
        // Get the selected question's index
        int selectedIndex = questionsListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            Question selectedQuestion = questions.get(selectedIndex);
            
            // Open a dialog for editing the selected question (can be a new FXML panel)
            openEditQuestionDialog(selectedQuestion);
        } else {
            showAlert("No Selection", "Please select a question to edit.");
        }
    }

    @FXML
    public void handleDeleteQuestion(ActionEvent event) {
        // Get the selected question's index
        int selectedIndex = questionsListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            questions.remove(selectedIndex);
            saveQuestions(); // Update the JSON file
            displayQuestionsInListView(); // Refresh the ListView
            showAlert("Deleted", "Question deleted successfully.");
        } else {
            showAlert("No Selection", "Please select a question to delete.");
        }
    }

    @FXML
    public void editQuestionPanel() {
        try {
            // Load the FXML for the Edit Question panel
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/EditQuestionPanel.fxml"));
            Parent root = loader.load();

            // Get the controller of the new panel
            EditQuestionDialogController dialogController = loader.getController();
            dialogController.setParentController(this);  // Pass the main controller to the dialog controller

            // Create a new Stage (pop-up window)
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Question");
            dialogStage.initModality(Modality.APPLICATION_MODAL);  // Block interaction with parent window
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();  // Show the dialog and wait for it to be closed

        } catch (IOException e) {
            showAlert("Error", "Failed to open Edit Question dialog.");
            e.printStackTrace();
        }
    }
    private void openEditQuestionDialog(Question selectedQuestion) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/EditQuestionDialog.fxml"));
            Parent root = loader.load();

            // Get the controller for the EditQuestionDialog
            EditQuestionDialogController dialogController = loader.getController();
            dialogController.setQuestionToEdit(selectedQuestion);  // Pass the question to edit

            // Create a new stage (pop-up window)
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Question");
            dialogStage.initModality(Modality.APPLICATION_MODAL);  // This blocks interaction with the parent window
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();  // Show the dialog and wait for it to be closed
        } catch (IOException e) {
            showAlert("Error", "Failed to open edit question dialog.");
            e.printStackTrace();
        }
    }
    
    public void updateQuestionInListView(Question editedQuestion) {
        // Update the question in your list
        int index = questions.indexOf(editedQuestion);
        if (index != -1) {
            questions.set(index, editedQuestion);
            displayQuestionsInListView();  // Update the ListView to reflect the change
        }
    }
    @FXML
    public void addNewQuestionPanel() {
        try {
            // Load the FXML for the Add Question panel
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddQuestionPanel.fxml"));
            Parent root = loader.load();

            // Get the controller of the new panel
            AddQuestionDialogController dialogController = loader.getController();
            dialogController.setParentController(this);  // Pass the main controller to the dialog controller

            // Create a new Stage (pop-up window)
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add New Question");
            dialogStage.initModality(Modality.APPLICATION_MODAL);  // Block interaction with parent window
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();  // Show the dialog and wait for it to be closed

        } catch (IOException e) {
            showAlert("Error", "Failed to open Add Question dialog.");
            e.printStackTrace();
        }
    }
    
    
 

    public void addNewQuestion(Question newQuestion) {
        // Add the new question to the questions list
        questions.add(newQuestion);

        // Update the ListView or any UI element showing the list of questions
        saveQuestions();
        displayQuestionsInListView();  // Example method to update ListView with new question

        // Show success message (optional)
        showAlert("Success", "Question added successfully!");
    }

    /*EDAFEEEEEEEEE*/
    
    
    public void updateDifficultyLabels() {
        for (Question q : questions) {
            String difficulty = q.getDifficulty();
            
            switch (difficulty) {
                case "1":
                    q.setDifficulty("EASY");
                    break;
                case "2":
                    q.setDifficulty("MEDIUM");
                    break;
                case "3":
                    q.setDifficulty("HARD");
                    break;
                default:
                    // If difficulty is not recognized, keep it as is or handle it if needed
                    break;
            }
        }

        // Save the updated questions list to the JSON file
        saveQuestions();

        // Optional: Show a success alert
     //   showAlert("Success", "All questions' difficulties have been updated.");
    }

}