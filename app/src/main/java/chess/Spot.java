package chess;
import pieces.ChessPiece;


public class Spot {

    int x;

    int y;

    ChessPiece piece;


    public Spot(int x, int y, ChessPiece piece) {
        this.x = x;
        this.y = y;
        this.piece = piece;

    }
  
    public ChessPiece getPiece()

    {
        return this.piece;
    }

    public void setPiece(ChessPiece p)

    {
        this.piece = p;
    }


    public boolean isEmpty() { return piece == null;}

  
    public int getXCoordinate()

    {
        return this.x;
    }


    public int getYCoordinate()

    {
        return this.y;
    }

}
