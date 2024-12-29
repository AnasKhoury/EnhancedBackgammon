package Controller;

import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

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
            case 1: return "\u2680"; // ⚀
            case 2: return "\u2681"; // ⚁
            case 3: return "\u2682"; // ⚂
            case 4: return "\u2683"; // ⚃
            case 5: return "\u2684"; // ⚄
            case 6: return "\u2685"; // ⚅
            default: return "\u2680";
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
    private void handleStartGame() {
        System.out.println("Start Game button clicked!");

        try {
            // Load GameBoard.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/GameBoard.fxml"));
            Parent gameBoardRoot = loader.load();

            // Access the GameBoardController instance
            GameBoardController gameBoardController = loader.getController();

            // Initialize GameController with two players and a default difficulty
            GameController gameController = new GameController("Player 1", "Player 2", Model.Difficulty.EASY);

            // Pass the GameController to the GameBoardController
            gameBoardController.setGameController(gameController);

            // Switch to the GameBoard scene
            Stage stage = (Stage) titleLabel.getScene().getWindow();
            stage.setScene(new Scene(gameBoardRoot));
            stage.setTitle("Game Board");
            stage.show();

        } catch (IOException e) {
            System.out.println("Failed to load GameBoard: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void goToQuestionManagement(ActionEvent event) {
        try {
            // Debugging: Print resource path to verify correctness
            System.out.println(getClass().getResource("/view/QuestionManagement.fxml"));

            // Load the Question Management FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/QuestionManagement.fxml"));
            Parent root = loader.load();

            // Create a new stage for the Question Management screen
            Stage stage = new Stage();
            stage.setTitle("Question Management");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.out.println("Failed to load Question Management screen: " + e.getMessage());
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.out.println("FXML file path is invalid. Please check that the file exists in the correct directory.");
            e.printStackTrace();
        }
    }


}
