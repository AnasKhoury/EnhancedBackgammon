package Controller;

import java.util.Random;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameSetupController {

    @FXML
    private ComboBox<String> difficultyComboBox;  // ComboBox for difficulty level

    @FXML
    private TextField player1Name;  // TextField for Player 1's name

    @FXML
    private TextField player2Name;  // TextField for Player 2's name

    @FXML
    private Button startGameButton;  // Button to start the game

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
//        fadeOutDice(dice1);
//        fadeOutDice(dice2);
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
    @FXML
    private void startGame() {
        // Fetch the selected difficulty level and player names
        String difficulty = difficultyComboBox.getValue();
        String player1 = player1Name.getText();
        String player2 = player2Name.getText();

        // Validate inputs
        boolean hasError = false;

        if (difficulty == null || difficulty.isEmpty()) {
            showAlert("Input Error", "Please select a difficulty level.");
            hasError = true; // Mark as invalid input
        }
        if (player1 == null || player1.trim().isEmpty()) {
            showAlert("Input Error", "Player 1 name cannot be empty.");
            hasError = true; // Mark as invalid input
        }
        if (player2 == null || player2.trim().isEmpty()) {
            showAlert("Input Error", "Player 2 name cannot be empty.");
            hasError = true; // Mark as invalid input
        }

        // If there are errors, do not proceed
        if (hasError) {
            return; // User stays on the same screen to correct the input
        }

        try {
        	FXMLLoader diceLoader = new FXMLLoader(getClass().getResource("/view/DiceRollPopup.fxml"));
            Stage diceStage = new Stage();
            diceStage.setScene(new Scene(diceLoader.load()));
            diceStage.setTitle("Dice Roll to Determine Starter");
            diceStage.setResizable(false);

            DiceRollPopUpController diceController = diceLoader.getController();
            diceController.setPlayerNames(player1, player2); // Pass player names to the pop-up

            // Show the pop-up and wait until it is closed
            diceStage.showAndWait();


            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/GameBoard.fxml"));
            Stage stage = (Stage) startGameButton.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setFullScreen(true);
            stage.setResizable(false);
            stage.show(); // Display the game board
            
            
           GameBoardController gameBoardController = loader.getController();
           gameBoardController.setPlayerNames(player1, player2);
           gameBoardController.setImageDifficultyURL("view/" + difficultyComboBox.getValue() + ".png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait(); // Display the alert and wait for user response
    }

    /*    @FXML
   void startGame() {
        // Get player names and selected difficulty
        String player1 = player1Name.getText();
        String player2 = player2Name.getText();
        String difficulty = difficultyComboBox.getValue();

        try {
            // Load the GameBoard FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/GameBoard.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(loader.load());

            // Pass the player names and difficulty to GameBoardController
            GameBoardController controller = loader.getController();
            controller.initializeGame(player1, player2, difficulty);

            // Show the game board window
            stage.setScene(scene);
            stage.show();

            // Close the GameSetup window
            Stage currentStage = (Stage) player1Name.getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
    // Additional methods can be added for handling ComboBox or TextField logic if needed
}