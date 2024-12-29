package Controller;

import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.fxml.FXML;
import javafx.scene.control.Label; 
import javafx.stage.Stage; 

import java.io.IOException;
import java.util.Random;

public class OpenScreenController {

    @FXML
    private Label dice1, dice2;

    @FXML
    private Pane decorativeElement;

    @FXML
    private Label titleLabel;

    private final Random random = new Random();

    public void initialize() {
        // Optional: Add initial animations to other elements
        startDiceAnimation();
    }

    private void startDiceAnimation() {
        // Rolling dice with animation
        Timeline diceRollingAnimation = new Timeline(new KeyFrame(Duration.millis(100), event -> {
            dice1.setText(getDiceSymbol(randomDiceFace()));
            dice2.setText(getDiceSymbol(randomDiceFace()));
        }));

        diceRollingAnimation.setCycleCount(30); // Roll 30 times for a longer animation
        diceRollingAnimation.setOnFinished(event -> finalizeDiceRoll());
        diceRollingAnimation.play();

        // Add rotation effect to dice
        RotateTransition rotate1 = createDiceRotation(dice1);
        RotateTransition rotate2 = createDiceRotation(dice2);

        rotate1.play();
        rotate2.play();
    }

    private void finalizeDiceRoll() {
        // Set final dice face after the animation ends
        dice1.setText(getDiceSymbol(randomDiceFace()));
        dice2.setText(getDiceSymbol(randomDiceFace()));

        // Apply fade-out effect to both dice
        fadeOutDice(dice1);
        fadeOutDice(dice2);
    }

    private int randomDiceFace() {
        return random.nextInt(6) + 1; // Random value between 1 and 6
    }

    private String getDiceSymbol(int face) {
        switch (face) {
            case 1: return "\u2680"; // 
            case 2: return "\u2681"; // 
            case 3: return "\u2682"; // 
            case 4: return "\u2683"; // 
            case 5: return "\u2684"; // 
            case 6: return "\u2685"; // 
            default: return "\u2680"; // 
        }
    }

    private RotateTransition createDiceRotation(Label dice) {
        RotateTransition rotate = new RotateTransition(Duration.seconds(1), dice);
        rotate.setByAngle(360);
        rotate.setCycleCount(2); // Dice rotates two times
        rotate.setAutoReverse(false);
        return rotate;
    }

    // Fade out the dice after animation
    private void fadeOutDice(Label dice) {
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), dice);
        fadeOut.setToValue(0);  // Fade out the dice
        fadeOut.setOnFinished(event -> dice.setVisible(false)); // Hide dice after fade-out
        fadeOut.play();
    }
    
    
    public void openGameSetup() throws IOException {
        // Load the QuestionManagement.fxml file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/GameSetup.fxml"));
        Scene gameSetup = new Scene(loader.load());

        // Get the current stage (window) by accessing the scene's window from any node
        Stage stage = (Stage) titleLabel.getScene().getWindow();

        // Set the new scene (Question Management screen)
        stage.setScene(gameSetup);

        stage.setTitle("Game Setup");
    }

    
    public void openQuestionManagement() throws IOException {
        // Load the QuestionManagement.fxml file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/QuestionsManagement.fxml"));
        Scene questionManagementScene = new Scene(loader.load());

        // Get the current stage (window) by accessing the scene's window from any node
        Stage stage = (Stage) titleLabel.getScene().getWindow();

        // Set the new scene (Question Management screen)
        stage.setScene(questionManagementScene);

        stage.setTitle("Question Management");
    }
 
    
    
    
    //FAQ AND SETTINGS
    @FXML
    private void handleFAQClick() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("FAQ - Frequently Asked Questions");
        
        VBox content = new VBox(10);
        content.setPadding(new Insets(20));
        
        // Add FAQ items
        addFAQItem(content, "How do I play?", 
            "1. Start a new game by clicking 'Start Game'\n" +
            "2. Roll the dice on your turn\n" +
            "3. Move your pieces according to dice values\n" +
            "4. Answer questions correctly to progress\n" +
            "5. First player to get all pieces home wins!");
            
        addFAQItem(content, "What are the different question difficulties?",
            "Easy: Basic questions suitable for beginners\n" +
            "Medium: Moderate difficulty requiring good knowledge\n" +
            "Hard: Challenging questions for experienced players");
            
        addFAQItem(content, "How do I win?",
            "To win the game, you must:\n" +
            "1. Move all your pieces around the board\n" +
            "2. Answer questions correctly to advance\n" +
            "3. Get all your pieces to the home position");
        
        // Add more FAQ items as needed
        
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(400);
        
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setContent(scrollPane);
        dialogPane.getButtonTypes().add(ButtonType.CLOSE);
        dialogPane.getStylesheets().add(getClass().getResource("/view/style.css").toExternalForm());
        
        // Style the dialog
        dialogPane.setStyle("-fx-background-color: #F5F5F5;");
        
        dialog.showAndWait();
    }
    
    private void addFAQItem(VBox container, String question, String answer) {
        Label questionLabel = new Label(question);
        questionLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        questionLabel.setWrapText(true);
        
        Label answerLabel = new Label(answer);
        answerLabel.setWrapText(true);
        answerLabel.setStyle("-fx-font-size: 12px;");
        answerLabel.setTextAlignment(TextAlignment.LEFT);
        
        container.getChildren().addAll(questionLabel, answerLabel);
    }
    
    @FXML
    private void handleSettingsClick() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Game Settings");
        
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        
        // Add settings sections
        Label soundLabel = new Label("Sound Settings");
        soundLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        
        // Create settings controls (you can customize these based on your needs)
        VBox soundSettings = new VBox(10);
        soundSettings.getChildren().addAll(
            createSettingItem("Background Music", true),
            createSettingItem("Sound Effects", true)
        );
        
        Label gameplayLabel = new Label("Gameplay Settings");
        gameplayLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        
        VBox gameplaySettings = new VBox(10);
        gameplaySettings.getChildren().addAll(
            createSettingItem("Show Hints", true),
            createSettingItem("Timer Enabled", false)
        );
        
        content.getChildren().addAll(
            soundLabel,
            soundSettings,
            new Label(""), // Spacer
            gameplayLabel,
            gameplaySettings
        );
        
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setContent(content);
        dialogPane.getButtonTypes().addAll(ButtonType.APPLY, ButtonType.CANCEL);
        dialogPane.getStylesheets().add(getClass().getResource("/view/style.css").toExternalForm());
        
        // Style the dialog
        dialogPane.setStyle("-fx-background-color: #F5F5F5;");
        
        dialog.showAndWait();
    }
    
    private HBox createSettingItem(String labelText, boolean defaultValue) {
        HBox setting = new HBox(10);
        Label label = new Label(labelText);
        label.setStyle("-fx-font-size: 12px;");
        
        javafx.scene.control.CheckBox checkBox = new javafx.scene.control.CheckBox();
        checkBox.setSelected(defaultValue);
        
        setting.getChildren().addAll(label, checkBox);
        return setting;
    }

}
