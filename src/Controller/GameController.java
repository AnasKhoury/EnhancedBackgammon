package Controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

import Model.Dice;
import Model.Difficulty;
import Model.Game;
import Model.GameSummary;
import Model.Piece;
import Model.Player;
import Model.Question;
import Model.Station;
import Model.SysData;

public class GameController {
	@SuppressWarnings("unused")
    private final Game gameBoard; // The game board
    private final List<Dice> diceList; // List of dice used in the game
    private Player currentPlayer; // Current player
    private final Random random; // Random generator for dice rolls
    private LocalDateTime gameStartTime; // Start time of the game
    private LocalDateTime gameEndTime; // End time of the game
    private final GameResultsController gameResultsController; // Game results controller
    private final SysData sysData; // Access to game questions

    public GameController(String player1Name, String player2Name, Difficulty difficulty) {
        Player player1 = new Player(player1Name, "Black");
        Player player2 = new Player(player2Name, "White");
        this.gameBoard = new Game(player1, player2, difficulty);
        this.diceList = new ArrayList<>();
        this.random = new Random();
        this.gameResultsController = new GameResultsController();
        this.sysData = SysData.getInstance(); // Accessing questions

        setupDice();
        determineStartingPlayer(player1, player2);
    }

    private void setupDice() {
        switch (gameBoard.getDifficulty()) {
            case EASY:
                diceList.add(new Dice("REGULAR"));
                diceList.add(new Dice("REGULAR"));
                break;
            case MEDIUM:
                diceList.add(new Dice("REGULAR"));
                diceList.add(new Dice("REGULAR"));
                diceList.add(new Dice("QUESTION"));
                break;
            case HARD:
                diceList.add(new Dice("ENHANCED"));
                diceList.add(new Dice("ENHANCED"));
                diceList.add(new Dice("QUESTION"));
                break;
            default:
                throw new IllegalArgumentException("Unsupported difficulty: " + gameBoard.getDifficulty());
        }
    }

    private void determineStartingPlayer(Player player1, Player player2) {
        Dice regularDice = new Dice("REGULAR");
        int player1Roll, player2Roll;

        do {
            player1Roll = regularDice.roll();
            player2Roll = regularDice.roll();

            if (player1Roll > player2Roll) {
                currentPlayer = player1;
            } else if (player2Roll > player1Roll) {
                currentPlayer = player2;
            }
        } while (player1Roll == player2Roll);

        System.out.println(currentPlayer.getName() + " will start the game!");
    }

    public void startGame() {
        gameStartTime = LocalDateTime.now(); // Start time of the game
        boolean gameOver = false;
        int turnCount = 0;
        int maxTurns = 100; // Safeguard to prevent infinite loops

        System.out.println("Game starts with difficulty: " + gameBoard.getDifficulty());
        gameBoard.displayBoard();

        while (!gameOver && turnCount < maxTurns) {
            System.out.println("\n--- Turn " + (turnCount + 1) + " ---");
            boolean turnCompleted = playTurn(currentPlayer);

            if (turnCompleted) {
                gameOver = checkWinCondition(currentPlayer);
            }

            if (gameOver) {
                System.out.println("Congratulations! " + currentPlayer.getName() + " has won the game!");
                break;
            }

            // Switch players
            currentPlayer = (currentPlayer == gameBoard.getPlayer1()) ? gameBoard.getPlayer2() : gameBoard.getPlayer1();
            turnCount++;

            // Add a delay to simulate game duration
            try {
                Thread.sleep(1000); // 1 second delay
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        gameEndTime = LocalDateTime.now(); // Ensure gameEndTime is set

        if (!gameOver && turnCount >= maxTurns) {
            System.out.println("Game ended in a draw due to reaching the maximum turn limit.");
        } else {
            saveGameResult(); // Save game result after ensuring gameEndTime is set
        }
    }
    private boolean playTurn(Player player) {
        System.out.println(player.getName() + "'s turn.");
        System.out.println("Remaining pieces for " + player.getName() + ": " + countRemainingPieces(player));//bayan:added this here to use the func.

        int firstDiceResult = 0;
        int secondDiceResult = 0;
        Question randomQuestion = null;


        for (Dice dice : diceList) {
            int rollResult = dice.roll();
            if(!dice.getType().equals("QUESTION")) {
	            System.out.println(player.getName() + " rolled: " + rollResult);
	            if(firstDiceResult == 0) {
	            	firstDiceResult = rollResult;
	            }
	            else {
	            	secondDiceResult = rollResult;
	            	}
            }
            else {
                // Fetch questions with the specified difficulty
                List<Question> questions = SysData.getInstance().getQuestions();
                System.out.println(questions);
                List<Question> filteredQuestions = null;
                switch (rollResult) {
                    case 1:
                        filteredQuestions = questions.stream()
                                .filter(q -> q.getDifficulty().equals("1"))
                                .collect(Collectors.toList());

                        break;
                    case 2:
                        filteredQuestions = questions.stream()
                                .filter(q -> q.getDifficulty().equals("2"))
                                .collect(Collectors.toList());

                        break;
                    case 3:
                        filteredQuestions = questions.stream()
                                .filter(q -> q.getDifficulty().equals("3"))
                                .collect(Collectors.toList());

                        break;
                    default:
                        System.out.println("Invalid roll for difficulty question: " + rollResult);
                        return false;
                }

                if (filteredQuestions == null || filteredQuestions.isEmpty()) {
                    System.out.println("No questions available for the rolled difficulty.");
                    System.out.println(filteredQuestions);

                }

                // Select a random question ( check for a null value!)
                randomQuestion = filteredQuestions.get(new Random().nextInt(filteredQuestions.size()));
            }

        }
        boolean answer = handleDiceQuestion(player,randomQuestion);
        if(answer) {
        	//boolean turnResult = movePieces(player, firstDiceResult,secondDiceResult);//bayan:this is giving warnings as we do not need this rn,changed it to:
        	movePieces(player, firstDiceResult, secondDiceResult);

        }
        gameBoard.displayBoard();
        return true;
//        // Display the number of remaining pieces for both players
//        System.out.println("Remaining pieces for " + gameBoard.getPlayer1().getName() + ": " + countRemainingPieces(gameBoard.getPlayer1()));
//        System.out.println("Remaining pieces for " + gameBoard.getPlayer2().getName() + ": " + countRemainingPieces(gameBoard.getPlayer2()));

    }
    private int countRemainingPieces(Player player) {
        int remainingPieces = 0;
        for (Station station : gameBoard.getStations()) {
            for (Piece piece : station.getPieces()) {
                if (piece.getPieceColor().equals(player.getColor())) {
                    remainingPieces++;
                }
            }
        }
        return remainingPieces;
    }

 // Method to move a piece from one station to another
    private boolean movePiece(Station currentStation, int steps, Player player) {
        // Validate the move using game rules
        if (!isValidMove(currentStation, steps, player)) {
            System.out.println("Invalid move based on backgammon rules. Please try again.");
            return false;
        }

        // Calculate the target station index
        int targetStationIndex;
        if (player.getColor().equals("Black")) {
            targetStationIndex = gameBoard.getStations().indexOf(currentStation) + steps;
        } else {
            targetStationIndex = gameBoard.getStations().indexOf(currentStation) - steps;
        }

        // Check if the piece can be removed (reached home area)
        if ((targetStationIndex > 23 && player.getColor().equals("Black")) || (targetStationIndex < 0 && player.getColor().equals("White"))) {
            System.out.println(player.getName() + " has successfully removed a piece!");

            // Remove the piece from the current station and then from the player's pieces
            Piece pieceToRemove = currentStation.getPieces().remove(currentStation.getPieces().size() - 1);
            player.getPieces().removeIf(piece -> piece.getPieceID() == pieceToRemove.getPieceID());
        } else {
            // Get the target station
            Station targetStation = gameBoard.getStations().get(targetStationIndex);

            // Check if the target station is occupied by exactly one opponent piece
            if (isOccupiedByOpponent(targetStation, player.getColor()) && getPieceCountByColor(targetStation, player.getColor()) == 1) {
                // Move the opponent's piece to the hold area
                Piece opponentPiece = targetStation.getPieces().remove(0);
                System.out.println("Opponent's piece has been moved to the hold area.");
                // Add the opponent's piece to the bar
                gameBoard.addOnBarPiece(opponentPiece);
            }

            // Move the top piece from the current station to the target station
            Piece movingPiece = currentStation.getPieces().remove(currentStation.getPieces().size() - 1);
            targetStation.getPieces().add(movingPiece);

            System.out.println(player.getName() + " moved a piece to station " + (targetStationIndex + 1));

            // Handle special stations if applicable
            if (targetStation.isQuestionStation()) {
                handleQuestionStation(player);
            } else if (targetStation.isSurpriseStation()) {
                handleSurpriseStation(player);
            }
        }

        return true;
    }

    // Method to validate if a move is possible
    private boolean isValidMove(Station currentStation, int steps, Player player) {
        // Calculate the target station index
        int targetStationIndex;
        if (player.getColor().equals("Black")) {
            targetStationIndex = gameBoard.getStations().indexOf(currentStation) + steps;
        } else {
            targetStationIndex = gameBoard.getStations().indexOf(currentStation) - steps;
        }

        // Check bounds for home area
        if ((targetStationIndex > 23 && player.getColor().equals("Black")) || (targetStationIndex < 0 && player.getColor().equals("White"))) {
            return true;
        }

        // Check if the target station is within valid range
        if (targetStationIndex < 0 || targetStationIndex >= gameBoard.getStations().size()) {
            return false;
        }

        // Check if the target station is occupied by an opponent
        Station targetStation = gameBoard.getStations().get(targetStationIndex);
        if (isOccupiedByOpponent(targetStation, player.getColor()) && getPieceCountByColor(targetStation, player.getColor()) > 1) {
            return false;
        }

        return true;
    }

    // Method to handle the player's move using dice results
    //bayan:also wrapped this method with try(),to avoid warnings.
    private boolean movePieces(Player player, int firstDiceResult, int secondDiceResult) {
        System.out.println("Player: " + player.getName());
        System.out.println("Available dice rolls: " + firstDiceResult + " and " + secondDiceResult);

        try (Scanner scanner = new Scanner(System.in)) { // Try-with-resources to ensure closure
            // Check if the player has pieces on the bar
            if (hasPiecesOnBar(player)) {
                System.out.println("You have pieces on the bar. You must re-enter them before moving other pieces.");

                boolean canUseFirstDice = reEnterPieceFromBar(player, firstDiceResult, false);
                boolean canUseSecondDice = reEnterPieceFromBar(player, secondDiceResult, false);

                if (!canUseFirstDice && !canUseSecondDice) {
                    System.out.println("Neither dice can be used to re-enter a piece. Turn skipped.");
                    return false; // Turn ends if no re-entry is possible
                }

                while (hasPiecesOnBar(player)) {
                    System.out.println("Enter the number of steps to move the piece from the bar onto the board (choose " 
                            + (canUseFirstDice ? firstDiceResult : "") 
                            + (canUseSecondDice ? " or " + secondDiceResult : "") + "):");
                    int steps;
                    while (true) {
                        try {
                            steps = Integer.parseInt(scanner.nextLine());
                            if ((steps == firstDiceResult && canUseFirstDice) || (steps == secondDiceResult && canUseSecondDice)) {
                                break;
                            } else {
                                System.out.println("Invalid input. Please choose a valid dice result.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter a valid number.");
                        }
                    }

                    if (reEnterPieceFromBar(player, steps, true)) {
                        if (steps == firstDiceResult) {
                            firstDiceResult = 0;
                            canUseFirstDice = false;
                        } else {
                            secondDiceResult = 0;
                            canUseSecondDice = false;
                        }
                    } else {
                        System.out.println("Re-entry failed. Try a different move.");
                    }
                }
            }

            boolean sumUsed = false;

            // Allow player to use the sum of the dice
            System.out.println("Do you want to use the sum of the dice (" + (firstDiceResult + secondDiceResult) + ")? Enter yes or no:");
            String useSum = scanner.nextLine().trim().toLowerCase();
            if (useSum.equals("yes")) {
                int sum = firstDiceResult + secondDiceResult;
                while (true) {
                    System.out.println("Enter the index of the station to move from (1-based):");
                    Station selectedStation = getPlayerSelectedStation(player);

                    System.out.println("Enter the number of steps to move the piece (using the sum " + sum + "):");
                    int steps;
                    try {
                        steps = Integer.parseInt(scanner.nextLine());
                        if (steps != sum) {
                            System.out.println("Invalid input. You must use the dice sum " + sum + ".");
                            continue;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a valid number.");
                        continue;
                    }

                    if (movePiece(selectedStation, steps, player)) {
                        sumUsed = true;
                        break; // Exit loop after a successful move
                    }
                }
            }

            if (sumUsed) {
                return true; // End turn if the sum was used
            }

            // Prompt the player for moves using both dice
            for (int diceResult : new int[]{firstDiceResult, secondDiceResult}) {
                if (diceResult == 0) {
                    continue; // Skip used dice
                }

                while (true) {
                    System.out.println("Enter the index of the station to move from (1-based):");
                    Station selectedStation = getPlayerSelectedStation(player);

                    System.out.println("Enter the number of steps to move the piece (using dice roll " + diceResult + "):");
                    int steps;
                    try {
                        steps = Integer.parseInt(scanner.nextLine());
                        if (steps != diceResult) {
                            System.out.println("Invalid input. You must use the dice roll " + diceResult + ".");
                            continue;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a valid number.");
                        continue;
                    }

                    if (movePiece(selectedStation, steps, player)) {
                        break; // Exit loop after a successful move
                    }
                }
            }
        } // Scanner is automatically closed here
        return true;
    }


    // Check if a player has pieces on the bar
    private boolean hasPiecesOnBar(Player player) {
        return gameBoard.getOnBarPieces().stream().anyMatch(piece -> piece.getPieceColor().equals(player.getColor()));
    }

    // Re-enter a piece from the bar onto the board
    private boolean reEnterPieceFromBar(Player player, int steps, boolean executeMove) {
        Piece pieceToReEnter = gameBoard.getOnBarPieces().stream()
            .filter(piece -> piece.getPieceColor().equals(player.getColor()))
            .findFirst()
            .orElse(null);

        if (pieceToReEnter == null) {
            return false; // No pieces to re-enter
        }

        int targetStationIndex = (player.getColor().equals("Black")) ? steps - 1 : 24 - steps;
        Station targetStation = gameBoard.getStations().get(targetStationIndex);

        if (!isValidMove(targetStation, 0, player)) {
            return false; // Invalid re-entry
        }

        if (executeMove) {
            gameBoard.getOnBarPieces().remove(pieceToReEnter);
            targetStation.getPieces().add(pieceToReEnter);
            System.out.println("Piece re-entered onto station " + (targetStationIndex + 1));
        }

        return true;
    }




    // Method to get the station selected by the player
    //bayan:added try() method to scanner
    private Station getPlayerSelectedStation(Player player) {
        try (Scanner scanner = new Scanner(System.in)) { // Use try-with-resources
            int stationIndex;
            while (true) {
                try {
                    stationIndex = Integer.parseInt(scanner.nextLine()) - 1;
                    if (stationIndex >= 0 && stationIndex < gameBoard.getStations().size()) {
                        Station station = gameBoard.getStations().get(stationIndex);
                        if (station.stationColor(player.getColor())) {
                            return station;
                        } else {
                            System.out.println("The selected station does not have your pieces. Please try again.");
                        }
                    } else {
                        System.out.println("Invalid station index. Please try again.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid station index.");
                }
            }
        }
    }


    // Check if a station is occupied by opponent pieces
    private boolean isOccupiedByOpponent(Station station, String playerColor) {
        return station.getPieces().stream().anyMatch(piece -> !piece.getPieceColor().equals(playerColor));
    }

    // Get the count of pieces of a specific color on a station
    private int getPieceCountByColor(Station station, String color) {
        return (int) station.getPieces().stream().filter(piece -> piece.getPieceColor().equals(color)).count();
    }




    //bayan:changed this func,put try() around scanner to close it and remove warning
    private boolean handleQuestionStation(Player player) {
        List<Question> questions = sysData.getQuestions();
        Question randomQuestion = questions.get(random.nextInt(questions.size()));

        System.out.println("Landed on a Question Station!");
        System.out.println("Question: " + randomQuestion.getQuestion());
        System.out.println("Options: " + randomQuestion.getAnswers());

        // Use try-with-resources to handle Scanner
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Your answer: ");
            String playerAnswer = scanner.nextLine().trim();

            if (playerAnswer.equalsIgnoreCase(randomQuestion.getCorrectAns())) {
                System.out.println("Correct! You can continue.");
                return true;
            } else {
                System.out.println("Incorrect! Your turn ends.");
                return false;
            }
        }
    }
    
    //bayan:added try wrap around scanner
    private boolean handleDiceQuestion(Player player, Question randomQuestion) {
        String difficulty;
        if (randomQuestion.getDifficulty().equals("1")) {
            difficulty = "Easy";
        } else if (randomQuestion.getDifficulty().equals("2")) {
            difficulty = "Medium";
        } else {
            difficulty = "Hard";
        }

        System.out.println("You got a " + difficulty + " Question");
        System.out.println("Question: " + randomQuestion.getQuestion());
        System.out.println("Options: " + randomQuestion.getAnswers());

        try (Scanner scanner = new Scanner(System.in)) { // Use try-with-resources for automatic closure
            System.out.print("Your answer: ");
            String playerAnswer = scanner.nextLine().trim();

            if (playerAnswer.equalsIgnoreCase(randomQuestion.getCorrectAns())) {
                System.out.println("Correct! You can continue.");
                return true;
            } else {
                System.out.println("Incorrect! Your turn ends.");
                return false;
            }
        }
    }


    private void handleSurpriseStation(Player player) {
        System.out.println("Landed on a Surprise Station!");
        int surpriseEffect = random.nextInt(2); // 0 for extra turn, 1 for remove a piece

        if (surpriseEffect == 0) {
            System.out.println("Surprise! " + player.getName() + " gets an extra turn!");
            playTurn(player);
        } else {
            if (!player.getPieces().isEmpty()) {
                System.out.println("Surprise! " + player.getName() + " can remove one of their pieces!");
                Piece pieceToRemove = player.getPieces().get(0);
                player.getPieces().remove(pieceToRemove);
                System.out.println("A piece was removed for " + player.getName());
            } else {
                System.out.println("No pieces left to remove!");
            }
        }
    }

    private boolean checkWinCondition(Player player) {
        boolean hasNoPieces = player.getPieces().isEmpty(); // Check if the player has no pieces left
        System.out.println(player.getName() + " win condition: " + hasNoPieces);
        return hasNoPieces;
    }

    private void saveGameResult() {
        long totalDuration = java.time.Duration.between(gameStartTime, gameEndTime).getSeconds();
        GameSummary gameSummary = new GameSummary(
                gameBoard.getPlayer1().getName(),
                gameBoard.getPlayer2().getName(),
                gameBoard.getDifficulty().toString(),
                currentPlayer.getName(),
                totalDuration,
                gameStartTime,
                gameEndTime
        );

        gameResultsController.addGameResult(gameSummary);
        System.out.println("Game result saved successfully.");
    }
  //bayan added here:
    public int[] rollDice() {
        Random random = new Random();
        int die1 = random.nextInt(6) + 1; // Roll result: 1 to 6
        int die2 = random.nextInt(6) + 1; // Roll result: 1 to 6
        return new int[]{die1, die2};
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    public void switchPlayer() {
        currentPlayer = (currentPlayer == gameBoard.getPlayer1()) ? gameBoard.getPlayer2() : gameBoard.getPlayer1();
        System.out.println("It's now " + currentPlayer.getName() + "'s turn.");
    }

}