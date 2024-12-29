package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    private final int NUM_STATIONS = 24; // Total stations on the board
    private final List<Station> stations; // List of stations
    private final List<Piece> onBarPieces;
    private final Player player1; // Player 1 (Black)
    private final Player player2; // Player 2 (White)
    private final Difficulty difficulty; // Selected difficulty level

    // Constructor
    public Game(Player player1, Player player2, Difficulty difficulty) {
        this.player1 = player1;
        this.player2 = player2;
        this.difficulty = difficulty;
        this.stations = new ArrayList<>();
        onBarPieces = new ArrayList<>();
        initializeBoard();
        initializeSpecialStations(); // Add special stations
        initializePieces();
    }

    // Initializes the game board with empty stations
    private void initializeBoard() {
        for (int i = 1; i <= NUM_STATIONS; i++) {
            stations.add(new Station(i));
        }
    }

    // Randomly assigns question and surprise stations
    private void initializeSpecialStations() {
        Random random = new Random();
        List<Integer> specialStations = new ArrayList<>();

        // Assign 3 question stations
        while (specialStations.size() < 3) {
            int stationIndex = random.nextInt(NUM_STATIONS);
            if (!specialStations.contains(stationIndex)) {
                stations.get(stationIndex).setQuestionStation(true);
                specialStations.add(stationIndex);
            }
        }

        // Assign 1 surprise station
        while (true) {
            int stationIndex = random.nextInt(NUM_STATIONS);
            if (!specialStations.contains(stationIndex)) {
                stations.get(stationIndex).setSurpriseStation(true);
                break;
            }
        }
    }

    // Places the players' pieces at their initial positions
    private void initializePieces() {
        if (player1.getPieces().size() != 15 || player2.getPieces().size() != 15) {
            throw new IllegalStateException("Each player must start with exactly 15 pieces.");
        }

        stations.get(0).addPieces(player1.getPieces().subList(0, 2));
        stations.get(11).addPieces(player1.getPieces().subList(2, 7));
        stations.get(16).addPieces(player1.getPieces().subList(7, 10));
        stations.get(18).addPieces(player1.getPieces().subList(10, 15));

        stations.get(23).addPieces(player2.getPieces().subList(0, 2));
        stations.get(12).addPieces(player2.getPieces().subList(2, 7));
        stations.get(7).addPieces(player2.getPieces().subList(7, 10));
        stations.get(5).addPieces(player2.getPieces().subList(10, 15));
    }

    public List<Station> getStations() {
        return stations;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void displayBoard() {
        for (Station station : stations) {
            System.out.println(station);
        }
    }

	public List<Piece> getOnBarPieces() {
		return onBarPieces;
	}
	public boolean addOnBarPiece(Piece piece) {
		return onBarPieces.add(piece);
	}
}
