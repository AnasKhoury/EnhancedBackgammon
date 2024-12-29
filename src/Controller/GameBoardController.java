package Controller;

import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.nio.file.Paths;
import java.util.Random;

import Model.Difficulty;

public class GameBoardController {

    // Dice visuals
    @FXML private ImageView bdie1, bdie2;

    // Player names
    @FXML private Text player1Label;
    @FXML private Text player2Label;
    @FXML private ImageView difficulty;

    // Sound effect for dice hitting the table
    private AudioClip diceSound;

    // Random number generator
    private final Random random = new Random();

    // Dice image paths
    private final String[] diceImages = {
        "/view/bdie1.png",
        "/view/bdie2.png",
        "/view/bdie3.png",
        "/view/bdie4.png",
        "/view/bdie5.png",
        "/view/bdie6.png"
    };

    public GameBoardController() {}

    public void initialize() {
        diceSound = new AudioClip(Paths.get("src/view/dice_sound.mp3").toUri().toString());
        setupDiceActions();
        System.out.println(difficulty.getImage().getUrl());
    }

    public void initalizeGameController() {
        GameController game = new GameController(player1Label.getText(), player2Label.getText(), getGameDifficulty());
    }

    public void setImageDifficultyURL(String url) {
        System.out.println(url);

        // Set an image URL
        String imageUrl = url; // Replace with your actual image path
        Image image = new Image(imageUrl, false);

        // Assign the image to the ImageView
        difficulty.setImage(image);
        System.out.println(difficulty.getImage().getUrl());
    }

    public Difficulty getGameDifficulty() {
        if (difficulty.getImage() == null) {
            return Difficulty.Unknown; // Default if no image is set
        }

        // Extract the URL of the current image
        String imageUrl = difficulty.getImage().getUrl();

        // Check the URL to determine the difficulty
        if (imageUrl.contains("easy")) {
            return Difficulty.EASY;
        } else if (imageUrl.contains("medium")) {
            return Difficulty.MEDIUM;
        } else if (imageUrl.contains("hard")) {
            return Difficulty.HARD;
        }
        return Difficulty.Unknown;
    }

    /**
     * Setup dice click actions to trigger animation.
     */
    private void setupDiceActions() {
        bdie1.setOnMouseClicked(event -> rollBothDice());
        bdie2.setOnMouseClicked(event -> rollBothDice());
    }

    public void setPlayerNames(String player1, String player2) {
        player1Label.setText(player1);
        player2Label.setText(player2);
    }

    /**
     * Roll both dice simultaneously with animations and sound.
     */
    private void rollBothDice() {
        // Create individual animations for both dice
        ParallelTransition diceAnimations = new ParallelTransition(
            createDiceAnimation(bdie1),
            createDiceAnimation(bdie2)
        );

        // Set sound timing
        Timeline soundDelay = new Timeline(new KeyFrame(
            Duration.millis(380), // Adjust this delay to match the dice landing visually
            event -> diceSound.play()
        ));

        // Play animations and sound
        diceAnimations.play();
        soundDelay.play();
    }

    /**
     * Creates the animation for a single die.
     */
    private ParallelTransition createDiceAnimation(ImageView die) {
        // Define fixed landing position for the dice
        double fixedX = 160 + random.nextInt(50); // Random X-coordinate of the landing spot
        double fixedY = 50 + random.nextInt(20); // Random Y-coordinate of the landing spot
        double bounceHeight = 50; // Height of the bounce

        // Create a bounce path
        Path path = new Path();
        path.getElements().add(new MoveTo(die.getLayoutX(), die.getLayoutY())); // Start at current position
        path.getElements().add(new LineTo(fixedX, fixedY - bounceHeight));      // Upward bounce
        path.getElements().add(new LineTo(fixedX, fixedY));                    // Settle down

        // Movement animation
        PathTransition pathTransition = new PathTransition();
        pathTransition.setNode(die);
        pathTransition.setDuration(Duration.millis(1000));
        pathTransition.setPath(path);

        // Rotation animation
        RotateTransition rotate = new RotateTransition();
        rotate.setNode(die);
        rotate.setDuration(Duration.millis(1000));
        rotate.setByAngle(720); // Rotate multiple times during throw

        // Random dice face animation during throw
        Timeline faceChangeAnimation = new Timeline();
        int totalFrames = 20;

        for (int frame = 0; frame < totalFrames; frame++) {
            final int currentFrame = frame; // Make loop variable effectively final
            faceChangeAnimation.getKeyFrames().add(new KeyFrame(
                Duration.millis(currentFrame * 50),
                event -> {
                    int randomSide = random.nextInt(6) + 1;
                    updateDieImage(die, randomSide);
                }
            ));
        }

        faceChangeAnimation.play();
        return new ParallelTransition(pathTransition, rotate);
    }

    /**
     * Update the dice image based on the rolled value.
     */
    private void updateDieImage(ImageView die, int value) {
        try {
            String imagePath = getClass().getResource(diceImages[value - 1]).toExternalForm();
            die.setImage(new Image(imagePath));
        } catch (Exception e) {
            System.err.println("Error loading dice image: " + e.getMessage());
        }
    }
}


















//package Controller;
//
//import javafx.fxml.FXML;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.scene.text.Text;
//import javafx.scene.control.Button;
//import javafx.scene.paint.Color;
//import javafx.scene.shape.Circle;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class GameBoardController {
//
//    // Dice visuals
//    @FXML private ImageView die1, die2;
//    
//
//    // Buttons
//    @FXML private Button btnRoll;
//
//    // Information display
//    @FXML private Text infoText;
//
//    // Player pieces (list placeholders)
//    private final List<Circle> player1Pieces = new ArrayList<>();
//    private final List<Circle> player2Pieces = new ArrayList<>();
//
//    // Reference to GameController
//    private GameController gameController;
//
//    /**
//     * Set the GameController instance passed from OpenScreenController.
//     */
//    public void setGameController(GameController controller) {
//        this.gameController = controller;
//
//        // Display starting player
//        if (controller != null) {
//            infoText.setText(controller.getCurrentPlayer().getName() + " starts the game!");
//            System.out.println("GameController has been set in GameBoardController!");
//
//        }
//    }
//
//
//    /**
//     * Initialize UI components when the GameBoard is loaded.
//     */
//    
//  /*  public void initializeGame(String player1, String player2, String difficulty) {
//        player1Label.setText("Player 1: " + player1);
//        player2Label.setText("Player 2: " + player2);
//        difficultyLabel.setText("Difficulty: " + difficulty);
//    }*/
//    public void initialize() {
////        setupRollButton();
//        setupInitialPieces();
//    }
//
//    /**
//     * Setup the roll dice button action.
//     */
////    private void setupRollButton() {
////        btnRoll.setOnAction(event -> rollDice());
////    }
//
//    /**
//     * Roll the dice using GameController and update visuals.
//     */
//    private void rollDice() {
//        if (gameController == null) {
//            System.out.println("GameController is not initialized!");
//            return;
//        }
//
//        // Get dice results from GameController
//        int[] diceResults = gameController.rollDice();
//
//        // Update dice images
//        updateDieImage(die1, diceResults[0]);
//        updateDieImage(die2, diceResults[1]);
//
//        // Display roll results
//        infoText.setText(gameController.getCurrentPlayer().getName() + " rolled: " +
//                diceResults[0] + " and " + diceResults[1]);
//
//        // Switch player after rolling
//        gameController.switchPlayer();
//        infoText.setText("Next turn: " + gameController.getCurrentPlayer().getName());
//    }
//
//    /**
//     * Update the dice images based on rolled values.
//     */
//    private void updateDieImage(ImageView die, int value) {
//        try {
//            String imagePath = getClass().getResource("/View/dice" + value + ".png").toExternalForm();
//            die.setImage(new Image(imagePath));
//        } catch (Exception e) {
//            System.out.println("Error loading dice image: " + e.getMessage());
//        }
//    }
//
//    /**
//     * Set initial visuals for player pieces.
//     */
//    private void setupInitialPieces() {
//        for (Circle piece : player1Pieces) {
//            setPieceColor(piece, Color.BLACK); // Player 1 pieces are black
//        }
//
//        for (Circle piece : player2Pieces) {
//            setPieceColor(piece, Color.WHITE); // Player 2 pieces are white
//        }
//    }
//
//    /**
//     * Helper method to set piece color.
//     */
//    private void setPieceColor(Circle piece, Color color) {
//        if (piece != null) {
//            piece.setFill(color);
//        }
//    }
//}


