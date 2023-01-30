package chess;

import android.util.Log;

import java.util.*;
import pieces.*;
import static pieces.King.castledK;
import static pieces.King.castledQ;
import com.example.androidchess78.MainActivity.*;

//Parth Patel and Rishik Sarkar

public class Chess {


	static boolean whiteTurn = true;

	static boolean askDraw=false;


	
	public static void LetsStart(Board chessBoard) {
		chessBoard = new Board();
		chessBoard.PaintCurrentBoard();
	}
	
	


   	public static boolean isKingInCheck(int color, Spot kingPosition, Board chessBoard) {
		if (color==0) {   
	
		for (int i=0;i<8;i++) {
			for (int j=0;j<8;j++) {
				if (chessBoard.grid[j][i].getPiece()!=null && chessBoard.grid[j][i].getPiece().getColor()==1 && chessBoard.grid[j][i].getPiece().validMove(chessBoard, chessBoard.grid[j][i], kingPosition ) && chessBoard.isPathEmpty(chessBoard.grid[j][i], kingPosition)) {
					return true;
				}
			}
		}
		}
		else {   
			
			for (int i=0;i<8;i++) {
				for (int j=0;j<8;j++) {
					if (chessBoard.grid[j][i].getPiece()!=null && chessBoard.grid[j][i].getPiece().getColor()==0 && chessBoard.grid[j][i].getPiece().validMove(chessBoard, chessBoard.grid[j][i], kingPosition ) && chessBoard.isPathEmpty(chessBoard.grid[j][i], kingPosition)) {
						return true;
					}
				}
			}
	}
		return false;
	}

	public static boolean AmI_InChkMat(int color, Board chessBoard) {
		Spot kingPosition=findKingPosition(color, chessBoard);
	
		int[] xDirection=new int[] {-1,0,1,-1,1,-1,0,1};
		int[] yDirection=new int[] {-1,-1,-1,0,0,1,1,1};
		
	
			for (int i=0;i<xDirection.length;i++) {
				int newX=kingPosition.getXCoordinate()+xDirection[i];
				int newY=kingPosition.getYCoordinate()+yDirection[i];
				if (newX<0 || newX>7 || newY<0 || newY>7) {
					continue;
				}
				Spot endPosition=chessBoard.grid[newX][newY];
				if (kingPosition.getPiece().validMoveWithoutCheck(chessBoard, kingPosition, endPosition)) {
					if (!isKingInCheck(color,endPosition, chessBoard)) {
						return false;
				}
			}
		}
			if (!CanIBlockKing(color, chessBoard)) {
				return true;
			}

		return false;
	}
	

	public static Spot findKingPosition(int color, Board chessBoard) {
		Spot kingPosition=null;
		if (color==0) {
		for (int i=0;i<8;i++) {
			for (int j=0;j<8;j++) {
				
				if (!chessBoard.grid[j][i].isEmpty() && chessBoard.grid[j][i].getPiece().getPieceName().equals("wK")) {
					kingPosition=chessBoard.grid[j][i];
					break;
				}
					
			}
		}
		}
		else if (color==1) {
			for (int i=0;i<8;i++) {
				for (int j=0;j<8;j++) {
					
					if (!chessBoard.grid[j][i].isEmpty() && chessBoard.grid[j][i].getPiece().getPieceName().equals("bK")) {
						kingPosition=chessBoard.grid[j][i];
						break;
					}
						
				}
			}
		}
		return kingPosition;
	}
	

	
	public static boolean CanIBlockKing(int color, Board chessBoard) {
		String TempStrExcl="";
		if (color==0) {
			TempStrExcl="wK";
		}
		else {
			TempStrExcl="bK";
		}
		for (int i=0;i<8;i++) {
			for (int j=0;j<8;j++) {
				if (chessBoard.grid[j][i].getPiece()!=null && chessBoard.grid[j][i].getPiece().getColor()==color && !chessBoard.grid[j][i].getPiece().getPieceName().equals(TempStrExcl)) {
					for (int a=0;a<8;a++) {
						for (int b=0;b<8;b++) {
							Spot newPosition=chessBoard.grid[b][a];
							ChessPiece newPositionPiece=newPosition.getPiece();
							ChessPiece current=chessBoard.grid[j][i].getPiece();
							if (chessBoard.grid[j][i].getPiece().validMoveWithoutCheck(chessBoard, chessBoard.grid[j][i], newPosition) && chessBoard.isPathEmpty(chessBoard.grid[j][i], newPosition)) {
								
								
								newPosition.setPiece(current);
								chessBoard.grid[j][i].setPiece(null);
								if (!isKingInCheck(color,findKingPosition(color, chessBoard), chessBoard)) {
									
;									chessBoard.grid[j][i].setPiece(current);
									newPosition.setPiece(newPositionPiece);
									return true;
								}
							}
							chessBoard.grid[j][i].setPiece(current);
							newPosition.setPiece(newPositionPiece);
						}
					}
				}
			}
		}
		
		return false;
	}
	
	
	
	
	

	

}
