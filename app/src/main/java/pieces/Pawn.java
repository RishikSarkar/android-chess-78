package pieces;
import chess.*;

//Parth Patel and Rishik Sarkar/

public class Pawn extends ChessPiece{
	
	String pieceName="p";

	int pieceColor=0;

	int previousChange=0;

	public boolean enPassant = false;

	public Pawn(int color) {
		this.setColor(color);
		this.pieceColor=color;
	}

	public Pawn(int color, int previousChange){
		this.previousChange = previousChange;
		this.setColor(color);
		this.pieceColor=color;
	}
	public int getPreviousChange() {
		return previousChange;
	}


	public boolean getEnPassant(){
		return enPassant;
	}

	@Override
	public String getPieceName() {
		if (pieceColor==0) {
			this.pieceName="wp";
		}
		else {
			this.pieceName="bp";
		}
		return this.pieceName;
	}

	@Override
	public boolean validMove(Board board, Spot startPosition, Spot endPosition) {
		enPassant = false;

		int yChange=endPosition.getYCoordinate()-startPosition.getYCoordinate();
		int xChange=Math.abs(endPosition.getXCoordinate()-startPosition.getXCoordinate());

		if (board.grid[endPosition.getXCoordinate()][endPosition.getYCoordinate()].isEmpty() && xChange == 1 && ((yChange==1 && color == 1) || (yChange == -1 && color == 0))) { //potential en passant
			if (pieceColor == 0){ //white turn
				if (!board.grid[endPosition.getXCoordinate()][endPosition.getYCoordinate()+1].isEmpty() && board.grid[endPosition.getXCoordinate()][endPosition.getYCoordinate()+1].getPiece().equals("bp") && board.grid[endPosition.getXCoordinate()][endPosition.getYCoordinate()+1].getPiece().previousChange == 2) {
					enPassant = true;
					
					return true;
				}
			}else { //black turn
				if (!board.grid[endPosition.getXCoordinate()][endPosition.getYCoordinate()-1].isEmpty() && board.grid[endPosition.getXCoordinate()][endPosition.getYCoordinate()-1].getPiece().equals("wp") && board.grid[endPosition.getXCoordinate()][endPosition.getYCoordinate()-1].getPiece().previousChange == 2){
					enPassant = true;
					
					return true;
				}
			}
		}

		if ((board.grid[endPosition.getXCoordinate()][endPosition.getYCoordinate()].getPiece()==null && ((yChange==1 && color == 1) || (yChange == -1 && color == 0)) && xChange==0) || (board.grid[endPosition.getXCoordinate()][endPosition.getYCoordinate()].getPiece()!=null && xChange == 1 && ((yChange==1 && color == 1) || (yChange == -1 && color == 0))) || (board.grid[endPosition.getXCoordinate()][endPosition.getYCoordinate()].getPiece()==null && this.first == true && ((yChange == 2 && color == 1) || (yChange == -2 && color == 0))   && xChange == 0)) {
			previousChange = Math.abs(yChange);
			
			return true;
		}
		return false;
	}
	@Override
	public boolean validMoveWithoutCheck(Board board, Spot startPosition, Spot endPosition) {
		enPassant = false;
		if (!endPosition.isEmpty() && endPosition.getPiece().getColor()==this.getColor()) {  //same color piece
			return false;
		}

		int yChange=endPosition.getYCoordinate()-startPosition.getYCoordinate();
		int xChange=Math.abs(endPosition.getXCoordinate()-startPosition.getXCoordinate());

		if (board.grid[endPosition.getXCoordinate()][endPosition.getYCoordinate()].isEmpty() && xChange == 1 && ((yChange==1 && color == 1) || (yChange == -1 && color == 0))) { //potential en passant
			
			if (pieceColor== 0){ //white turn
				if (!board.grid[endPosition.getXCoordinate()][endPosition.getYCoordinate()+1].isEmpty() && board.grid[endPosition.getXCoordinate()][endPosition.getYCoordinate()+1].getPiece().getPieceName().equals("bp") && board.grid[endPosition.getXCoordinate()][endPosition.getYCoordinate()+1].getPiece().getPreviousChange() == 2) {
					enPassant = true;
					
					return true;
				}
			}else { 
				if (!board.grid[endPosition.getXCoordinate()][endPosition.getYCoordinate()-1].isEmpty() && board.grid[endPosition.getXCoordinate()][endPosition.getYCoordinate()-1].getPiece().getPieceName().equals("wp") && board.grid[endPosition.getXCoordinate()][endPosition.getYCoordinate()-1].getPiece().getPreviousChange() == 2){
					enPassant = true;
					
					return true;
				}
			}
		}
		
		if ((board.grid[endPosition.getXCoordinate()][endPosition.getYCoordinate()].getPiece()==null && ((yChange==1 && color == 1) || (yChange == -1 && color == 0)) && xChange==0) || (board.grid[endPosition.getXCoordinate()][endPosition.getYCoordinate()].getPiece()!=null && xChange == 1 && ((yChange==1 && color == 1) || (yChange == -1 && color == 0))) || (board.grid[endPosition.getXCoordinate()][endPosition.getYCoordinate()].getPiece()==null && this.first == true && ((yChange == 2 && color == 1) || (yChange == -2 && color == 0))   && xChange == 0)) {
			previousChange = Math.abs(yChange);
			
			return true;
		}
		return false;
	}

}