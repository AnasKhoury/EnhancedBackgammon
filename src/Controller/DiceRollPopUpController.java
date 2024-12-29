package Controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;

public class DiceRollPopUpController {

    @FXML
    private ImageView player1Dice, player2Dice;
    
    @FXML
    private Label playerName1, playerName2;

    @FXML
    private Button player1RollButton, player2RollButton, confirmButton;

    private final Random random = new Random();
    private String startingPlayer;
    private int player1Roll = 0;
    private int player2Roll = 0;

    // Array of dice images
    private final String[] diceImages = {
            "/view/bdie1.png",
            "/view/bdie2.png",
            "/view/bdie3.png",
            "/view/bdie4.png",
            "/view/bdie5.png",
            "/view/bdie6.png"
    };

    public String getStartingPlayer() {
        return startingPlayer;
    }

    @FXML
    private void rollPlayer1Dice() {
        player1RollButton.setDisable(true); // Disable the button after rolling
        animateDiceRoll(player1Dice, rollResult -> {
            player1Roll = rollResult;
            enableConfirmIfBothRolled();
        });
    }

    @FXML
    private void rollPlayer2Dice() {
        player2RollButton.setDisable(true); // Disable the button after rolling
        animateDiceRoll(player2Dice, rollResult -> {
            player2Roll = rollResult;
            enableConfirmIfBothRolled();
        });
    }

    @FXML
    private void confirmRoll() {
        if (player1Roll > player2Roll) {
            startingPlayer = playerName1.getText() + " starts the game!";
        } else if (player2Roll > player1Roll) {
            startingPlayer = playerName2.getText() + " starts the game!";
        } else {
            startingPlayer = "It's a tie! Please roll again.";
            player1RollButton.setDisable(false); // Disable the button after rolling
            player2RollButton.setDisable(false); // Disable the button after rolling
            enableConfirmIfBothRolled();
        }

        // Show alert with the starting player
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Dice Roll Result");
        alert.setHeaderText(null);
        alert.setContentText(startingPlayer);
        alert.showAndWait();

        // Close the popup window if not a tie
        if (!startingPlayer.contains("tie")) {
            ((Stage) confirmButton.getScene().getWindow()).close();
        }
    }

    private void enableConfirmIfBothRolled() {
        confirmButton.setDisable(player1Roll == 0 || player2Roll == 0);
    }

    /**
     * Animates the dice roll with a rotation effect and updates the result.
     * @param diceImageView The ImageView of the dice to animate.
     * @param onRollComplete A callback to handle the result of the roll.
     */
    private void animateDiceRoll(ImageView diceImageView, DiceRollCallback onRollComplete) {
        Timeline diceAnimation = new Timeline();
        int totalFrames = 10; // Number of frames in the animation

        for (int frame = 0; frame < totalFrames; frame++) {
            diceAnimation.getKeyFrames().add(new KeyFrame(
                    Duration.millis(frame * 50), // Adjust duration for faster/slower animation
                    event -> {
                        // Display a random dice image during animation
                        int randomSide = random.nextInt(6);
                        diceImageView.setImage(new Image(getClass().getResource(diceImages[randomSide]).toExternalForm()));
                    }
            ));
        }

        diceAnimation.setOnFinished(event -> {
            // Roll the dice to get the final result
            int finalRoll = random.nextInt(6) + 1;
            diceImageView.setImage(new Image(getClass().getResource(diceImages[finalRoll - 1]).toExternalForm()));
            onRollComplete.onRollComplete(finalRoll);
        });

        diceAnimation.play();
    }

    // Functional interface for handling the dice roll result
    @FunctionalInterface
    private interface DiceRollCallback {
        void onRollComplete(int rollResult);
    }

	public void setPlayerNames(String player1, String player2) {
		playerName1.setText(player1);
		playerName2.setText(player2);
	}
}
