package Model;

import Controller.GameController;
import Controller.GameResultsController;
import Controller.QuestionControl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Scanner;

public class main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println(getClass().getResource("/view/OpenScreen.fxml")); // Debugging

        // Load the OpenScreen FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/OpenScreen.fxml"));
        Scene scene = new Scene(loader.load());
        primaryStage.setTitle("Backgammon Enhanced");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


   /* public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);
    }*/
    public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);
        
    }

    @SuppressWarnings("unused")
    private static void startCLI() {
        Scanner scanner = new Scanner(System.in);

        // Question management system
        QuestionControl questionControl = new QuestionControl();
        questionControl.loadQuestions(); // Load the questions from the JSON file

        System.out.println("Welcome to the Game!");
        System.out.print("Enter Player 1's name: ");
        String player1Name = scanner.nextLine();

        System.out.print("Enter Player 2's name: ");
        String player2Name = scanner.nextLine();

        System.out.print("Choose difficulty (EASY, MEDIUM, HARD): ");
        Difficulty difficulty = null;
        while (difficulty == null) {
            try {
                String difficultyInput = scanner.nextLine().toUpperCase();
                difficulty = Difficulty.valueOf(difficultyInput);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid difficulty. Please choose EASY, MEDIUM, or HARD.");
            }
        }

        // Initialize the game
        GameController gameController = new GameController(player1Name, player2Name, difficulty);
        GameResultsController gameResultsController = new GameResultsController();

        boolean running = true;

         while (running) {
            System.out.println("\n=== Main Menu ===");
            System.out.println("1. Start Game");
            System.out.println("2. Manage Questions");
            System.out.println("3. View Game History");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    gameController.startGame();
                    break;
                case 2:
                   // manageQuestions(scanner, questionControl);
                    break;
                case 3:
                    gameResultsController.displayGameHistory();
                    break;
                case 4:
                    System.out.println("Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }

        scanner.close();
    }

  /*  private static void manageQuestions(Scanner scanner, QuestionControl questionControl) {
        boolean managing = true;

        while (managing) {
            System.out.println("\n=== Question Management Menu ===");
            System.out.println("1. Display All Questions");
            System.out.println("2. Add a Question");
            System.out.println("3. Edit a Question");
            System.out.println("4. Delete a Question");
            System.out.println("5. Back to Main Menu");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    questionControl.displayQuestions();
                    break;
                case 2:
                    questionControl.addQuestion(scanner);
                    break;
                case 3:
                    questionControl.editQuestion(scanner);
                    break;
                case 4:
                    questionControl.deleteQuestion(scanner);
                    break;
                case 5:
                    managing = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    */
    }














//package Model;
//
//import Controller.GameController;
//import Controller.GameResultsController;
//import Controller.QuestionControl;
//
//import java.util.Scanner;
//
//public class main {
//
//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//
//        // Question management system
//        QuestionControl questionControl = new QuestionControl();
//        String filePath = "src/question.json";
//        questionControl.loadQuestions(filePath);
//
//        GameController gameController = null;
//        GameResultsController gameResultsController = new GameResultsController();
//
//        boolean running = true;
//
//        while (running) {
//            System.out.println("\n=== Main Menu ===");
//            System.out.println("1. Start Game");
//            System.out.println("2. Manage Questions");
//            System.out.println("3. View Game History");
//            System.out.println("4. Exit");
//            System.out.print("Choose an option: ");
//
//            int choice = scanner.nextInt();
//            scanner.nextLine(); // Consume newline
//
//            switch (choice) {
//                case 1:
//                    // Prompt for player names and difficulty when starting the game
//                    System.out.print("Enter Player 1's name: ");
//                    String player1Name = scanner.nextLine();
//
//                    System.out.print("Enter Player 2's name: ");
//                    String player2Name = scanner.nextLine();
//
//                    System.out.print("Choose difficulty (EASY, MEDIUM, HARD): ");
//                    Difficulty difficulty = null;
//                    while (difficulty == null) {
//                        try {
//                            String difficultyInput = scanner.nextLine().toUpperCase();
//                            difficulty = Difficulty.valueOf(difficultyInput);
//                        } catch (IllegalArgumentException e) {
//                            System.out.println("Invalid difficulty. Please choose EASY, MEDIUM, or HARD.");
//                        }
//                    }
//
//                    gameController = new GameController(player1Name, player2Name, difficulty);
//                    gameController.startGame();
//                    break;
//                case 2:
//                    manageQuestions(scanner, questionControl);
//                    break;
//                case 3:
//                    gameResultsController.displayGameHistory();
//                    break;
//                case 4:
//                    System.out.println("Goodbye!");
//                    running = false;
//                    break;
//                default:
//                    System.out.println("Invalid choice. Please try again.");
//            }
//        }
//
//        scanner.close();
//    }
//
//    private static void manageQuestions(Scanner scanner, QuestionControl questionControl) {
//        boolean managing = true;
//
//        while (managing) {
//            System.out.println("\n=== Question Management Menu ===");
//            System.out.println("1. Display All Questions");
//            System.out.println("2. Add a Question");
//            System.out.println("3. Edit a Question");
//            System.out.println("4. Delete a Question");
//            System.out.println("5. Back to Main Menu");
//            System.out.print("Choose an option: ");
//
//            int choice = scanner.nextInt ();
//
//            scanner.nextLine(); // Consume newline
//
//            switch (choice) {
//                case 1:
//                    questionControl.displayQuestions();
//                    break;
//                case 2:
//                    questionControl.addQuestion(scanner);
//                    break;
//                case 3:
//                    questionControl.editQuestion(scanner);
//                    break;
//                case 4:
//                    questionControl.deleteQuestion(scanner);
//                    break;
//                case 5:
//                    managing = false;
//                    break;
//                default:
//                    System.out.println("Invalid choice. Please try again.");
//            }
//        }
//    }
//}

