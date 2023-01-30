package pieces;
import chess.*;


//Parth Patel and Rishik Sarkar

public abstract class ChessPiece {

	int color=0; //0 for white, 1 for black

	boolean first=true;

	int previousChange;

	

	
	public void setColor(int col) {
		this.color = col;
	}


	public void setFirst(boolean bool) {first = bool;}

	public int getColor() {
		return this.color;
	}
	


	
	public abstract String getPieceName();


	public abstract boolean validMove(Board board, Spot startPosition, Spot endPosition);


	public abstract boolean validMoveWithoutCheck(Board board, Spot startPosition, Spot endPosition);

	public abstract int getPreviousChange();
}
