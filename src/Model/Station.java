package Model;

import java.util.ArrayList;
import java.util.List;

public class Station {
    private final int index; // Station number
    private final List<Piece> pieces; // Pieces at this station
    private boolean isQuestionStation; // Whether the station is a question station
    private boolean isSurpriseStation; // Whether the station is a surprise station

    public Station(int index) {
        this.index = index;
        this.pieces = new ArrayList<>();
        this.isQuestionStation = false;
        this.isSurpriseStation = false;
    }

    // Add a single piece to this station
    public void addPiece(Piece piece) {
        pieces.add(piece);
    }

    // Add multiple pieces to this station
    public void addPieces(List<Piece> newPieces) {
        pieces.addAll(newPieces);
    }

    // Remove a piece from this station
    public void removePiece(Piece piece) {
        pieces.remove(piece);
    }

    // Get the list of pieces at this station
    public List<Piece> getPieces() {
        return pieces;
    }

    public boolean isQuestionStation() {
        return isQuestionStation;
    }

    public void setQuestionStation(boolean questionStation) {
        if (!isSurpriseStation) {
            isQuestionStation = questionStation;
        }
    }

    public boolean isSurpriseStation() {
        return isSurpriseStation;
    }

    public void setSurpriseStation(boolean surpriseStation) {
        if (!isQuestionStation) {
            isSurpriseStation = surpriseStation;
        }
    }
   public boolean stationColor(String color) {
	   if(!this.pieces.isEmpty()) {
		   return this.pieces.get(0).getPieceColor().equals(color);
	   }
	   return false;
   }

    @Override
    public String toString() {
        return "Station " + index + " | Pieces: " + pieces.size() + "| Color:"+ ((pieces.size() > 0) ? pieces.get(0).getPieceColor(): "") +
               " | Question: " + isQuestionStation +
               " | Surprise: " + isSurpriseStation;
    }
}
