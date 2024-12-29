package Model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final String name; // Player's name
    private final String color; // "Black" or "White"
    private final List<Piece> pieces; // List of the player's pieces
    private boolean isTurn; // Indicates if it's the player's turn

    public Player(String name, String color) {
        this.name = name;
        this.color = color;
        this.pieces = new ArrayList<>();
        this.isTurn = false;
        initializePieces();
    }

    // Initializes the player's pieces
    private void initializePieces() {
        for (int i = 0; i < 15; i++) {
            pieces.add(new Piece(color,i+1));
        }
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<Piece> getPieces() {
        return pieces;
    }

    public boolean isTurn() {
        return isTurn;
    }

    public void setTurn(boolean turn) {
        isTurn = turn;
    }

    @Override
    public String toString() {
        return "Player: " + name + " | Color: " + color + " | Pieces: " + pieces.size();
    }
}
