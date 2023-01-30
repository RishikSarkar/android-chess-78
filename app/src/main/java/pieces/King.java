package pieces;

import android.util.Log;

import chess.Board;
import chess.Spot;


//Parth Patel and Rishik Sarkar

public class King extends ChessPiece{

	String pieceName="K";

	public static boolean castledQ = false;

	public static boolean castledK = false;

	int pieceColor=0;

	int previousChange=0;

	public King(int color) {
		this.setColor(color);
		this.pieceColor=color;
	}

	public King(int color, int previousChange) {
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
			this.pieceName="wK";
		}
		else {
			this.pieceName="bK";
		}
		return this.pieceName;
	}

	@Override
	public boolean validMove(Board board, Spot startPosition, Spot endPosition) {
		
		if (color == 0){
	
			if (startPosition.getXCoordinate() == 4  && startPosition.getYCoordinate() == 7 && endPosition.getXCoordinate() == 6 && endPosition.getYCoordinate() == 7 && first && !board.grid[7][7].isEmpty() && board.grid[7][7].getPiece().getPieceName().equals("wR") && board.grid[7][7].getPiece().first && board.grid[5][7].isEmpty() && board.grid[6][7].isEmpty()){
				Log.d("ChessApp","**white castle kings side**");
				castledK = true;
				first = false;
				return true;
			}
	
			if (startPosition.getXCoordinate() == 4 && startPosition.getYCoordinate() == 7 && endPosition.getXCoordinate() == 2 && endPosition.getYCoordinate() == 7 && first && !board.grid[0][7].isEmpty() && board.grid[0][7].getPiece().getPieceName().equals("wR") && board.grid[0][7].getPiece().first && board.grid[1][7].isEmpty() && board.grid[2][7].isEmpty() && board.grid[3][7].isEmpty()){
				Log.d("ChessApp","**white castle queens side**");
				castledQ = true;
				first = false;
				return true;
			}
		}
		
		if (color == 1) {
			
			if (startPosition.getXCoordinate() == 4 && startPosition.getYCoordinate() == 0 && endPosition.getXCoordinate() == 6 && endPosition.getYCoordinate() == 0 && first && !board.grid[7][0].isEmpty() && board.grid[7][0].getPiece().getPieceName().equals("bR") && board.grid[7][0].getPiece().first && board.grid[5][0].isEmpty() && board.grid[6][0].isEmpty()){
				Log.d("ChessApp","**black castle kings side**");
				castledK = true;
				first = false;
				return true;
			}
			
			if (startPosition.getXCoordinate() == 4 && startPosition.getYCoordinate() == 0 && endPosition.getXCoordinate() == 2 && endPosition.getYCoordinate() == 0 && first && !board.grid[0][0].isEmpty() && board.grid[0][0].getPiece().getPieceName().equals("bR") && board.grid[0][0].getPiece().first && board.grid[1][0].isEmpty() && board.grid[2][0].isEmpty() && board.grid[3][0].isEmpty()){
				Log.d("ChessApp","**black castle queens side**");
				castledQ = true;
				first = false;
				return true;
			}

		}
		int yChange=Math.abs(endPosition.getYCoordinate()-startPosition.getYCoordinate());
		int xChange=Math.abs(endPosition.getXCoordinate()-startPosition.getXCoordinate());

		if ((xChange == 1 && yChange ==1) || (xChange ==1 && yChange == 0) || (xChange ==0 && yChange == 1)){
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
		
		if (color == 0){
			
			if (startPosition.getXCoordinate() == 4 && startPosition.getYCoordinate() == 7 && endPosition.getXCoordinate() == 6 && endPosition.getYCoordinate() == 7 && first && !board.grid[7][7].isEmpty() && board.grid[7][7].getPiece().getPieceName().equals("wR") && board.grid[7][7].getPiece().first && board.grid[5][7].isEmpty() && board.grid[6][7].isEmpty()){
				Log.d("ChessApp","**white castle kings side**");
				castledK = true;
				first = false;
				return true;
			}
			
			if (startPosition.getXCoordinate() == 4 && startPosition.getYCoordinate() == 7 && endPosition.getXCoordinate() == 2 && endPosition.getYCoordinate() == 7 && first && !board.grid[0][7].isEmpty() && board.grid[0][7].getPiece().getPieceName().equals("wR") && board.grid[0][7].getPiece().first && board.grid[1][7].isEmpty() && board.grid[2][7].isEmpty() && board.grid[3][7].isEmpty()){
				Log.d("ChessApp","**white castle queens side**");
				castledQ = true;
				first = false;
				return true;
			}
		}
		
		if (color == 1){
			
			if (startPosition.getXCoordinate() == 4 && startPosition.getYCoordinate() == 0 && endPosition.getXCoordinate() == 6 && endPosition.getYCoordinate() == 0 && first && !board.grid[7][0].isEmpty() && board.grid[7][0].getPiece().getPieceName().equals("bR") && board.grid[7][0].getPiece().first && board.grid[5][0].isEmpty() && board.grid[6][0].isEmpty()){
				Log.d("ChessApp","**black castle kings side**");
				castledK = true;
				first = false;
				return true;
			}
			
			if (startPosition.getXCoordinate() == 4 && startPosition.getYCoordinate() == 0 && endPosition.getXCoordinate() == 2 && endPosition.getYCoordinate() == 0 && first && !board.grid[0][0].isEmpty() && board.grid[0][0].getPiece().getPieceName().equals("bR") && board.grid[0][0].getPiece().first && board.grid[1][0].isEmpty() && board.grid[2][0].isEmpty() && board.grid[3][0].isEmpty()){
				Log.d("ChessApp", "**black castle queens side**");
				castledQ = true;
				first = false;
				return true;
			}
		}
		int yChange=Math.abs(endPosition.getYCoordinate()-startPosition.getYCoordinate());
		int xChange=Math.abs(endPosition.getXCoordinate()-startPosition.getXCoordinate());

		if ((xChange == 1 && yChange ==1) || (xChange ==1 && yChange == 0) || (xChange ==0 && yChange == 1)){
			previousChange = yChange;
			return true;
		}
		return false;
	}

}