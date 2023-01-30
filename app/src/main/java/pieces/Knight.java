package pieces;
import chess.*;

//Parth Patel and Rishik Sarkar


public class Knight extends ChessPiece {

	String pieceName="N";

	int pieceColor=0;

	int previousChange = 0;

	public Knight(int color) {
		this.setColor(color);
		this.pieceColor=color;
	}

	public Knight(int color, int previousChange) {
		this.setColor(color);
		this.pieceColor=color;
		this.previousChange = previousChange;
	}
	public int getPreviousChange() {
		return previousChange;
	}

	
	@Override
	public String getPieceName() {
		if (pieceColor==0) {
			this.pieceName="wN";
		}
		else {
			this.pieceName="bN";
		}
		return this.pieceName;
	}

	@Override
	public boolean validMove(Board board, Spot startPosition, Spot endPosition) {
		
	
		int yChange=Math.abs(endPosition.getYCoordinate()-startPosition.getYCoordinate());
		int xChange=Math.abs(endPosition.getXCoordinate()-startPosition.getXCoordinate());

		if ((xChange == 2 && yChange == 1) || (xChange == 1 && yChange == 2)){
			previousChange = yChange;
			return true;
		}
		return false;
		
	}
	@Override
	public boolean validMoveWithoutCheck(Board board, Spot startPosition, Spot endPosition) {
		if (!endPosition.isEmpty() && endPosition.getPiece().getColor()==this.getColor()) {
			return false;
		}

		int yChange=Math.abs(endPosition.getYCoordinate()-startPosition.getYCoordinate());
		int xChange=Math.abs(endPosition.getXCoordinate()-startPosition.getXCoordinate());

		if ((xChange == 2 && yChange == 1) || (xChange == 1 && yChange == 2)){
			previousChange = yChange;
			return true;
		}
        return false;
		
	}

}