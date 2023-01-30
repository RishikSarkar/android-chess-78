package chess;
import pieces.Rook;
import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Queen;

//Parth Patel and Rishik Sarkar

public class Board {

    public Spot[][] grid;

    public Board() {
        grid=new Spot[8][8];
        DrawVirginBoard();
    }


    public void DrawVirginBoard() {

        //black pieces
        grid[0][0] = new Spot(0, 0, new Rook(1));
        grid[0][1] = new Spot(0, 1, new Pawn(1));
        grid[1][0] = new Spot(1, 0, new Knight(1));
        grid[1][1] = new Spot(1, 1, new Pawn(1));
        grid[2][0] = new Spot(2, 0, new Bishop(1));
        grid[2][1] = new Spot(2, 1, new Pawn(1));
        grid[3][0] = new Spot(3, 0, new Queen(1));
        grid[3][1] = new Spot(3, 1, new Pawn(1));
        grid[4][0] = new Spot(4, 0, new King(1));
        grid[4][1] = new Spot(4, 1, new Pawn(1));
        grid[5][0] = new Spot(5, 0, new Bishop(1));
        grid[5][1] = new Spot(5, 1, new Pawn(1));
        grid[6][0] = new Spot(6, 0, new Knight(1));
        grid[6][1] = new Spot(6, 1, new Pawn(1));
        grid[7][0] = new Spot(7, 0, new Rook(1));
        grid[7][1] = new Spot(7, 1, new Pawn(1));

        //white pieces
        grid[0][6] = new Spot(0, 6, new Pawn(0));
        grid[0][7] = new Spot(0, 7, new Rook(0));
        grid[1][6] = new Spot(1, 6, new Pawn(0));
        grid[1][7] = new Spot(1, 7, new Knight(0));
        grid[2][6] = new Spot(2, 6, new Pawn(0));
        grid[2][7] = new Spot(2, 7, new Bishop(0));
        grid[3][6] = new Spot(3, 6, new Pawn(0));
        grid[3][7] = new Spot(3, 7, new Queen(0));
        grid[4][6] = new Spot(4, 6, new Pawn(0));
        grid[4][7] = new Spot(4, 7, new King(0));
        grid[5][6] = new Spot(5, 6, new Pawn(0));
        grid[5][7] = new Spot(5, 7, new Bishop(0));
        grid[6][6] = new Spot(6, 6, new Pawn(0));
        grid[6][7] = new Spot(6, 7, new Knight(0));
        grid[7][6] = new Spot(7, 6, new Pawn(0));
        grid[7][7] = new Spot(7, 7, new Rook(0));

        //remaining are empty pieces
        for (int a=0;a<8;a++) {
            for (int b=2;b<6;b++) {
                grid[a][b] = new Spot(a, b, null);
            }
        }


    }



    public void PaintCurrentBoard() {
        String[][] sboard = new String[8][8];

        int color = 0;
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                if (color == 0){
                    sboard[i][j] = "   ";
                    color = 1;
                }else{
                    sboard[i][j] = "## ";
                    color = 0;
                }
            }
            color = color == 0 ? 1 : 0;
        }
        for (int a = 0; a < 8; a++){
            for (int b = 0; b < 8; b++){
                if (this.grid[a][b].getPiece() != null){
                    sboard[a][b] = this.grid[a][b].getPiece().getPieceName() + " " ;
                }
            }
        }


        System.out.println();
        for (int a = 0; a < 8; a++){
            for (int b = 0; b < 8; b++){
                System.out.print(sboard[b][a]);
            }
            System.out.print(8-a);
            System.out.println();
        }
        System.out.println(" a  b  c  d  e  f  g  h");
    }


    public boolean isPathEmpty(Spot startPosition, Spot endPosition) {


        String piece=startPosition.getPiece().getPieceName();
        if (piece.equals("wK") || piece.equals("bK") || piece.equals("bN") || piece.equals("wN")) {
            return true;
        }
        int yChange= endPosition.getYCoordinate()-startPosition.getYCoordinate();
        int xChange=endPosition.getXCoordinate()-startPosition.getXCoordinate();

        int startBoxesX = startPosition.getXCoordinate();
        int startBoxesY = startPosition.getYCoordinate();

        int yDirection=yChange>0?1:yChange<0?-1:0;
        int xDirection=xChange>0?1:xChange<0?-1:0;



        if (yChange == 0) { 
            startBoxesX = startBoxesX + xDirection;
            int numberofBoxes=Math.abs(xChange)-1;
            int i=0;
            while (i<numberofBoxes) {
                if (grid[startBoxesX][startBoxesY].getPiece()!=null) {
                    return false;
                }
                startBoxesX=startBoxesX+xDirection;
                i++;
            }

        }
        else if (xChange == 0) { 

            startBoxesY = startBoxesY + yDirection;
            int numberofBoxes=Math.abs(yChange)-1;
            int i=0;
            while (i<numberofBoxes) {
                if (grid[startBoxesX][startBoxesY].getPiece()!=null) {
                    return false;
                }
                startBoxesY=startBoxesY+yDirection;
                i++;
            }

        }

        else { 
            startBoxesY = startBoxesY + yDirection;
            startBoxesX = startBoxesX + xDirection;
            int numberofBoxes=Math.abs(yChange)-1;
            int i=0;
            while (i<numberofBoxes) {
                if (grid[startBoxesX][startBoxesY].getPiece()!=null) {
                    return false;
                }
                startBoxesY = startBoxesY + yDirection;
                startBoxesX=startBoxesX+xDirection;
                i++;
            }
        }

        return true;
    }


}
