package Model;

public class Piece {
	private int pieceID;
    private String pieceColor; // Color of the piece



    // Constructor
    public Piece(String pieceColor, int pieceID) {
        this.pieceColor = pieceColor;
        this.pieceID = pieceID;
    }

    // Getter for pieceColor
    public String getPieceColor() {
        return pieceColor;
    }

    // Setter for pieceColor
    public void setPieceColor(String pieceColor) {
        this.pieceColor = pieceColor;
    }

    @Override
    public String toString() {
        return "Piece{" + "color='" + pieceColor + '\'' + '}';
    }

	public int getPieceID() {
		return pieceID;
	}

	public void setPieceID(int pieceID) {
		this.pieceID = pieceID;
	}
}
