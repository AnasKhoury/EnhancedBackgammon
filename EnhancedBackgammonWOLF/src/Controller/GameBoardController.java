package Controller;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;

public class GameBoardController {

    // Dice visuals
    @FXML private ImageView die1, die2;

    // Buttons
    @FXML private Button btnRoll;

    // Information display
    @FXML private Text infoText;

    // Player pieces (list placeholders)
    private final List<Circle> player1Pieces = new ArrayList<>();
    private final List<Circle> player2Pieces = new ArrayList<>();

    // Reference to GameController
    private GameController gameController;

    /**
     * Set the GameController instance passed from OpenScreenController.
     */
    public void setGameController(GameController controller) {
        this.gameController = controller;

        // Display starting player
        if (controller != null) {
            infoText.setText(controller.getCurrentPlayer().getName() + " starts the game!");
            System.out.println("GameController has been set in GameBoardController!");

        }
    }


    /**
     * Initialize UI components when the GameBoard is loaded.
     */
    public void initialize() {
        setupRollButton();
        setupInitialPieces();
    }

    /**
     * Setup the roll dice button action.
     */
    private void setupRollButton() {
        btnRoll.setOnAction(event -> rollDice());
    }

    /**
     * Roll the dice using GameController and update visuals.
     */
    private void rollDice() {
        if (gameController == null) {
            System.out.println("GameController is not initialized!");
            return;
        }

        // Get dice results from GameController
        int[] diceResults = gameController.rollDice();

        // Update dice images
        updateDieImage(die1, diceResults[0]);
        updateDieImage(die2, diceResults[1]);

        // Display roll results
        infoText.setText(gameController.getCurrentPlayer().getName() + " rolled: " +
                diceResults[0] + " and " + diceResults[1]);

        // Switch player after rolling
        gameController.switchPlayer();
        infoText.setText("Next turn: " + gameController.getCurrentPlayer().getName());
    }

    /**
     * Update the dice images based on rolled values.
     */
    private void updateDieImage(ImageView die, int value) {
        try {
            String imagePath = getClass().getResource("/View/dice" + value + ".png").toExternalForm();
            die.setImage(new Image(imagePath));
        } catch (Exception e) {
            System.out.println("Error loading dice image: " + e.getMessage());
        }
    }

    /**
     * Set initial visuals for player pieces.
     */
    private void setupInitialPieces() {
        for (Circle piece : player1Pieces) {
            setPieceColor(piece, Color.BLACK); // Player 1 pieces are black
        }

        for (Circle piece : player2Pieces) {
            setPieceColor(piece, Color.WHITE); // Player 2 pieces are white
        }
    }

    /**
     * Helper method to set piece color.
     */
    private void setPieceColor(Circle piece, Color color) {
        if (piece != null) {
            piece.setFill(color);
        }
    }
}
