package Controller;

import Model.Question;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class AddQuestionDialogController {

    // Reference to the parent controller (QuestionControl)
    private QuestionControl parentController;

    // FXML fields for the dialog UI
    @FXML
    private TextField questionTextField;
    @FXML
    private TextField answer1TextField;
    @FXML
    private TextField answer2TextField;
    @FXML
    private TextField answer3TextField;
    @FXML
    private TextField answer4TextField;
    @FXML
    private ComboBox<String> correctAnswerComboBox;
    @FXML
    private ComboBox<String> difficultyComboBox;
   
    // Method to set the parent controller
    public void setParentController(QuestionControl parentController) {
        this.parentController = parentController;
    }

    // Handle Add Question action
    @FXML
    private void handleAddQuestion() {
        // Get the inputs from the text fields and combo box
        String questionText = questionTextField.getText();
        List<String> answers = List.of(
            answer1TextField.getText(),
            answer2TextField.getText(),
            answer3TextField.getText(),
            answer4TextField.getText()
        );
        String correctAnswer = correctAnswerComboBox.getValue();

        String difficulty = difficultyComboBox.getValue();

        // Create a new Question object and set its properties
        Question newQuestion = new Question();
        newQuestion.setQuestion(questionText);
        newQuestion.setAnswers(answers);
        newQuestion.setCorrectAns(correctAnswer);
        newQuestion.setDifficulty(difficulty);

        // Pass the new question back to the parent controller
        if (parentController != null) {
            parentController.addNewQuestion(newQuestion);
        }

        // Close the dialog
        Stage stage = (Stage) questionTextField.getScene().getWindow();
        stage.close();
    }

    // Handle Cancel action
    @FXML
    private void handleCancel() {
        // Close the dialog without doing anything
        Stage stage = (Stage) questionTextField.getScene().getWindow();
        stage.close();
    }
    public void initialize() {
        // Listeners to check when answers are entered
        answer1TextField.textProperty().addListener((observable, oldValue, newValue) -> updateComboBox());
        answer2TextField.textProperty().addListener((observable, oldValue, newValue) -> updateComboBox());
        answer3TextField.textProperty().addListener((observable, oldValue, newValue) -> updateComboBox());
        answer4TextField.textProperty().addListener((observable, oldValue, newValue) -> updateComboBox());
    }

    // Method to update the ComboBox once all answers are entered
    private void updateComboBox() {
        // Get the answers from the TextFields
        String answer1 = answer1TextField.getText();
        String answer2 = answer2TextField.getText();
        String answer3 = answer3TextField.getText();
        String answer4 = answer4TextField.getText();

        // Check if all answers are entered
        if (!answer1.isEmpty() && !answer2.isEmpty() && !answer3.isEmpty() && !answer4.isEmpty()) {
            // Populate ComboBox with the answers
            List<String> answers = new ArrayList<>();
            answers.add(answer1);
            answers.add(answer2);
            answers.add(answer3);
            answers.add(answer4);

            // Set the ComboBox items
            correctAnswerComboBox.setItems(FXCollections.observableArrayList(answers));

            // Optionally, you can enable the ComboBox if it was disabled earlier
            correctAnswerComboBox.setDisable(false);
        } else {
            // If not all answers are entered, clear the ComboBox and disable it
            correctAnswerComboBox.setItems(FXCollections.emptyObservableList());
            correctAnswerComboBox.setDisable(true);
        }
    }
}
