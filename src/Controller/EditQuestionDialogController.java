package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.List;

import Model.Question;

public class EditQuestionDialogController {
    private QuestionControl parentController;

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
    private TextField correctAnswerTextField;
    @FXML
    private ComboBox<String> difficultyComboBox;

    private Question questionToEdit;  // The question to be edited

    // Set the question to edit in the dialog
    public void setQuestionToEdit(Question question) {
        this.questionToEdit = question;
        
        // Populate the UI elements with the question's data
        questionTextField.setText(question.getQuestion());
        answer1TextField.setText(question.getAnswers().get(0));
        answer2TextField.setText(question.getAnswers().get(1));
        answer3TextField.setText(question.getAnswers().get(2));
        answer4TextField.setText(question.getAnswers().get(3));
        correctAnswerTextField.setText(question.getCorrectAns());
        difficultyComboBox.setValue(question.getDifficulty());
    }
    public void setParentController(QuestionControl parentController) {
        this.parentController = parentController;
    }
    // Save the edited question (triggered by the save button)
    @FXML
    public void saveEditedQuestion() {
        if (questionToEdit != null) {
            // Update the question with new values from the text fields
            questionToEdit.setQuestion(questionTextField.getText());
            questionToEdit.setAnswers(List.of(
                answer1TextField.getText(),
                answer2TextField.getText(),
                answer3TextField.getText(),
                answer4TextField.getText()
            ));
            questionToEdit.setCorrectAns(correctAnswerTextField.getText());
            questionToEdit.setDifficulty(difficultyComboBox.getValue());

            // Assuming you have a method in the parent controller to update the question
            parentController.updateQuestionInListView(questionToEdit);

            // Close the dialog
            Stage stage = (Stage) questionTextField.getScene().getWindow();
            stage.close();
        }
    }
    @FXML
    public void handleCancel() {
        Stage stage = (Stage) questionTextField.getScene().getWindow();
        stage.close();
    }
}
