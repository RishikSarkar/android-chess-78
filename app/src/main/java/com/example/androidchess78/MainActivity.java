package com.example.androidchess78;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import chess.*;
import pieces.*;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static chess.Chess.findKingPosition;
import static chess.Chess.AmI_InChkMat;
import static chess.Chess.isKingInCheck;
import static chess.Chess.LetsStart;

import static chess.Chess.CanIBlockKing;
import static pieces.King.castledK;
import static pieces.King.castledQ;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    public static Board chessBoard;
    public static boolean whiteTurn;
    public TextView[][] displayBoard;
    public TextView[][] displayBoardBg;
    String start="";
    ChessPiece current=null;
    ChessPiece before=null;
    boolean draw=false;
    boolean lastAI=false;


    public Board prevChessBoard;

    public Spot clickedSpot;
    public Spot fromSpot;
    public boolean firstClick;
    public int numMoves = 0;
    public ArrayList<String> prevMoves;
    public TextView gameOver;
    public boolean isGameOver = false;
    public LinearLayout pawnPromoer;
    public int xStart;
    public int xEnd;
    public int yStart;
    public int yEnd;


    public Button deselect;

    public String thirdMove=null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        chessBoard = new Board();
        whiteTurn = true;
        displayBoard = new TextView[8][8];
        displayBoardBg = new TextView[8][8];
        xStart=0;
        xEnd=0;
        yEnd=0;
        yStart=0;
        clickedSpot = chessBoard.grid[0][0];
        fromSpot = chessBoard.grid[0][0];
        prevChessBoard = chessBoard;
        firstClick = true;

        isGameOver = false;

        prevMoves=new ArrayList<>();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Initialize10x10Board();

        gameOver = findViewById(R.id.gameOver);
        deselect = findViewById(R.id.deselect);
        pawnPromoer = findViewById(R.id.pawnPromoer);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (isGameOver){
            StopTheGame();
            return true;
        }
        switch(item.getItemId()){
            case R.id.undo:
                if (lastAI){
                    Toast.makeText(this, "Cannot UNDO", Toast.LENGTH_SHORT).show();
                    return true;
                }
               else {
                    Toast.makeText(this, "UNDO", Toast.LENGTH_SHORT).show();
                    String[] split = start.split(",");
                    String startPosition = split[0];
                    String endPosition = split[1];
                    int intTempX_S = Character.getNumericValue(startPosition.charAt(0));
                    int intTempY_S = Character.getNumericValue(startPosition.charAt(1));
                    int intTempX_E = Character.getNumericValue(endPosition.charAt(0));
                    int intTempY_E = Character.getNumericValue(endPosition.charAt(1));    
                    Spot destSpot = chessBoard.grid[intTempX_E][intTempY_E];

                    displayBoard[intTempX_S][intTempY_S].setBackgroundResource(getResource(current));
                    chessBoard.grid[intTempX_S][intTempY_S].setPiece(current);
                    displayBoard[intTempX_E][intTempY_E].setBackgroundResource(getResource(before));
                    chessBoard.grid[intTempX_E][intTempY_E].setPiece(before);
                    prevMoves.remove(prevMoves.size() - 1);
                    current=null;
                    before=null;




                    whiteTurn = whiteTurn ? false : true; 
                    return true;
               }
            case R.id.ai:
                Toast.makeText(this, "ai selected", Toast.LENGTH_SHORT).show();

                int currColor = whiteTurn ? 0 : 1;
                int intXStart = 0;
                int intYStart = 0;
                while (chessBoard.grid[intXStart][intYStart].isEmpty() || chessBoard.grid[intXStart][intYStart].getPiece().getColor() != currColor || noValidMoves(chessBoard.grid[intXStart][intYStart], chessBoard.grid[intXStart][intYStart].getPiece())){ //find a piece that belongs to the current player
                    intXStart = (int)Math.floor(Math.random()*8);
                    intYStart = (int)Math.floor(Math.random()*8);
                }
                ChessPiece ChesPieTemp = chessBoard.grid[intXStart][intYStart].getPiece();
                Log.d("ChessApp", "Piece picked was " + chessBoard.grid[intXStart][intYStart].getPiece().getPieceName() + " at (" + intXStart + "," + intYStart + ")");
               
                int intXTemp1 = 0;
                int intYTemp1 = 0;
                while (!ChesPieTemp.validMoveWithoutCheck(chessBoard, chessBoard.grid[intXStart][intYStart], chessBoard.grid[intXTemp1][intYTemp1]) || !chessBoard.isPathEmpty(chessBoard.grid[intXStart][intYStart], chessBoard.grid[intXTemp1][intYTemp1])){
                    intXTemp1 = (int)Math.floor(Math.random()*8);
                    intYTemp1 = (int)Math.floor(Math.random()*8);
                }
                Log.d("ChessApp", "Valid moving to (" + intXTemp1 + "," + intYTemp1 + ")");

                //spots selected, move piece
                ChesPieTemp.setFirst(false);
                if (ChesPieTemp instanceof Pawn && ((whiteTurn && intYTemp1 == 0) || (!whiteTurn && intYTemp1 == 7))){ //pawn promo
                    Queen promoQueen = new Queen(currColor);
                    chessBoard.grid[intXTemp1][intYTemp1].setPiece(promoQueen);
                    displayBoard[intXTemp1][intYTemp1].setBackgroundResource(getResource(promoQueen));
                    Log.d("ChessApp","AI PAWN PROMO @ (" + intXTemp1 + "," + intYTemp1 + ")");
                }else{
                    chessBoard.grid[intXTemp1][intYTemp1].setPiece(ChesPieTemp);
                    displayBoard[intXTemp1][intYTemp1].setBackgroundResource(getResource(ChesPieTemp));
                    prevMoves.add(Integer.toString(intXStart) + Integer.toString(intYStart) + "," + Integer.toString(intXTemp1) + Integer.toString(intYTemp1));
                }
                chessBoard.grid[intXStart][intYStart].setPiece(null);
                displayBoard[intXStart][intYStart].setBackgroundResource(0);

                whiteTurn = whiteTurn ? false : true;
                lastAI=true;
                return true;
            case R.id.resign:
                Toast.makeText(this, "resign selected", Toast.LENGTH_SHORT).show();
                if (!whiteTurn) {
                    gameOver.setText("White Wins!");
                }else{
                    gameOver.setText("Black wins!");
                }
                gameOver.setVisibility(View.VISIBLE);
                StopTheGame();
                return true;
            case R.id.draw:
                Toast.makeText(this, "draw selected", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.dialog_message)
                        .setTitle(R.string.draw);

                builder.setPositiveButton(R.string.draw, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        gameOver.setText(R.string.draw);
                        gameOver.setVisibility(View.VISIBLE);
                        draw=true;
                        StopTheGame();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        return;
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.game_options_menu,menu);
        return true;
    }

    private void Initialize10x10Board() {
        chessBoard.DrawVirginBoard();
        displayBoard[0][0] = (TextView) findViewById(R.id.F00);
        displayBoardBg[0][0] = (TextView) findViewById(R.id.bg00);
        displayBoard[1][0] = (TextView) findViewById(R.id.F10);
        displayBoardBg[1][0] = (TextView) findViewById(R.id.bg10);
        displayBoard[2][0] = (TextView) findViewById(R.id.F20);
        displayBoardBg[2][0] = (TextView) findViewById(R.id.bg20);
        displayBoard[3][0] = (TextView) findViewById(R.id.F30);
        displayBoardBg[3][0] = (TextView) findViewById(R.id.bg30);
        displayBoard[4][0] = (TextView) findViewById(R.id.F40);
        displayBoardBg[4][0] = (TextView) findViewById(R.id.bg40);
        displayBoard[5][0] = (TextView) findViewById(R.id.F50);
        displayBoardBg[5][0] = (TextView) findViewById(R.id.bg50);
        displayBoard[6][0] = (TextView) findViewById(R.id.F60);
        displayBoardBg[6][0] = (TextView) findViewById(R.id.bg60);
        displayBoard[7][0] = (TextView) findViewById(R.id.F70);
        displayBoardBg[7][0] = (TextView) findViewById(R.id.bg70);

        displayBoard[0][1] = (TextView) findViewById(R.id.F01);
        displayBoardBg[0][1] = (TextView) findViewById(R.id.bg01);
        displayBoard[1][1] = (TextView) findViewById(R.id.F11);
        displayBoardBg[1][1] = (TextView) findViewById(R.id.bg11);
        displayBoard[2][1] = (TextView) findViewById(R.id.F21);
        displayBoardBg[2][1] = (TextView) findViewById(R.id.bg21);
        displayBoard[3][1] = (TextView) findViewById(R.id.F31);
        displayBoardBg[3][1] = (TextView) findViewById(R.id.bg31);
        displayBoard[4][1] = (TextView) findViewById(R.id.F41);
        displayBoardBg[4][1] = (TextView) findViewById(R.id.bg41);
        displayBoard[5][1] = (TextView) findViewById(R.id.F51);
        displayBoardBg[5][1] = (TextView) findViewById(R.id.bg51);
        displayBoard[6][1] = (TextView) findViewById(R.id.F61);
        displayBoardBg[6][1] = (TextView) findViewById(R.id.bg61);
        displayBoard[7][1] = (TextView) findViewById(R.id.F71);
        displayBoardBg[7][1] = (TextView) findViewById(R.id.bg71);

        displayBoard[0][2] = (TextView) findViewById(R.id.F02);
        displayBoardBg[0][2] = (TextView) findViewById(R.id.bg02);
        displayBoard[1][2] = (TextView) findViewById(R.id.F12);
        displayBoardBg[1][2] = (TextView) findViewById(R.id.bg12);
        displayBoard[2][2] = (TextView) findViewById(R.id.F22);
        displayBoardBg[2][2] = (TextView) findViewById(R.id.bg22);
        displayBoard[3][2] = (TextView) findViewById(R.id.F32);
        displayBoardBg[3][2] = (TextView) findViewById(R.id.bg32);
        displayBoard[4][2] = (TextView) findViewById(R.id.F42);
        displayBoardBg[4][2] = (TextView) findViewById(R.id.bg42);
        displayBoard[5][2] = (TextView) findViewById(R.id.F52);
        displayBoardBg[5][2] = (TextView) findViewById(R.id.bg52);
        displayBoard[6][2] = (TextView) findViewById(R.id.F62);
        displayBoardBg[6][2] = (TextView) findViewById(R.id.bg62);
        displayBoard[7][2] = (TextView) findViewById(R.id.F72);
        displayBoardBg[7][2] = (TextView) findViewById(R.id.bg72);

        displayBoard[0][3] = (TextView) findViewById(R.id.F03);
        displayBoardBg[0][3] = (TextView) findViewById(R.id.bg03);
        displayBoard[1][3] = (TextView) findViewById(R.id.F13);
        displayBoardBg[1][3] = (TextView) findViewById(R.id.bg13);
        displayBoard[2][3] = (TextView) findViewById(R.id.F23);
        displayBoardBg[2][3] = (TextView) findViewById(R.id.bg23);
        displayBoard[3][3] = (TextView) findViewById(R.id.F33);
        displayBoardBg[3][3] = (TextView) findViewById(R.id.bg33);
        displayBoard[4][3] = (TextView) findViewById(R.id.F43);
        displayBoardBg[4][3] = (TextView) findViewById(R.id.bg43);
        displayBoard[5][3] = (TextView) findViewById(R.id.F53);
        displayBoardBg[5][3] = (TextView) findViewById(R.id.bg53);
        displayBoard[6][3] = (TextView) findViewById(R.id.F63);
        displayBoardBg[6][3] = (TextView) findViewById(R.id.bg63);
        displayBoard[7][3] = (TextView) findViewById(R.id.F73);
        displayBoardBg[7][3] = (TextView) findViewById(R.id.bg73);

        displayBoard[0][4] = (TextView) findViewById(R.id.F04);
        displayBoardBg[0][4] = (TextView) findViewById(R.id.bg04);
        displayBoard[1][4] = (TextView) findViewById(R.id.F14);
        displayBoardBg[1][4] = (TextView) findViewById(R.id.bg14);
        displayBoard[2][4] = (TextView) findViewById(R.id.F24);
        displayBoardBg[2][4] = (TextView) findViewById(R.id.bg24);
        displayBoard[3][4] = (TextView) findViewById(R.id.F34);
        displayBoardBg[3][4] = (TextView) findViewById(R.id.bg34);
        displayBoard[4][4] = (TextView) findViewById(R.id.F44);
        displayBoardBg[4][4] = (TextView) findViewById(R.id.bg44);
        displayBoard[5][4] = (TextView) findViewById(R.id.F54);
        displayBoardBg[5][4] = (TextView) findViewById(R.id.bg54);
        displayBoard[6][4] = (TextView) findViewById(R.id.F64);
        displayBoardBg[6][4] = (TextView) findViewById(R.id.bg64);
        displayBoard[7][4] = (TextView) findViewById(R.id.F74);
        displayBoardBg[7][4] = (TextView) findViewById(R.id.bg74);

        displayBoard[0][5] = (TextView) findViewById(R.id.F05);
        displayBoardBg[0][5] = (TextView) findViewById(R.id.bg05);
        displayBoard[1][5] = (TextView) findViewById(R.id.F15);
        displayBoardBg[1][5] = (TextView) findViewById(R.id.bg15);
        displayBoard[2][5] = (TextView) findViewById(R.id.F25);
        displayBoardBg[2][5] = (TextView) findViewById(R.id.bg25);
        displayBoard[3][5] = (TextView) findViewById(R.id.F35);
        displayBoardBg[3][5] = (TextView) findViewById(R.id.bg35);
        displayBoard[4][5] = (TextView) findViewById(R.id.F45);
        displayBoardBg[4][5] = (TextView) findViewById(R.id.bg45);
        displayBoard[5][5] = (TextView) findViewById(R.id.F55);
        displayBoardBg[5][5] = (TextView) findViewById(R.id.bg55);
        displayBoard[6][5] = (TextView) findViewById(R.id.F65);
        displayBoardBg[6][5] = (TextView) findViewById(R.id.bg65);
        displayBoard[7][5] = (TextView) findViewById(R.id.F75);
        displayBoardBg[7][5] = (TextView) findViewById(R.id.bg75);

        displayBoard[0][6] = (TextView) findViewById(R.id.F06);
        displayBoardBg[0][6] = (TextView) findViewById(R.id.bg06);
        displayBoard[1][6] = (TextView) findViewById(R.id.F16);
        displayBoardBg[1][6] = (TextView) findViewById(R.id.bg16);
        displayBoard[2][6] = (TextView) findViewById(R.id.F26);
        displayBoardBg[2][6] = (TextView) findViewById(R.id.bg26);
        displayBoard[3][6] = (TextView) findViewById(R.id.F36);
        displayBoardBg[3][6] = (TextView) findViewById(R.id.bg36);
        displayBoard[4][6] = (TextView) findViewById(R.id.F46);
        displayBoardBg[4][6] = (TextView) findViewById(R.id.bg46);
        displayBoard[5][6] = (TextView) findViewById(R.id.F56);
        displayBoardBg[5][6] = (TextView) findViewById(R.id.bg56);
        displayBoard[6][6] = (TextView) findViewById(R.id.F66);
        displayBoardBg[6][6] = (TextView) findViewById(R.id.bg66);
        displayBoard[7][6] = (TextView) findViewById(R.id.F76);
        displayBoardBg[7][6] = (TextView) findViewById(R.id.bg76);

        displayBoard[0][7] = (TextView) findViewById(R.id.F07);
        displayBoardBg[0][7] = (TextView) findViewById(R.id.bg07);
        displayBoard[1][7] = (TextView) findViewById(R.id.F17);
        displayBoardBg[1][7] = (TextView) findViewById(R.id.bg17);
        displayBoard[2][7] = (TextView) findViewById(R.id.F27);
        displayBoardBg[2][7] = (TextView) findViewById(R.id.bg27);
        displayBoard[3][7] = (TextView) findViewById(R.id.F37);
        displayBoardBg[3][7] = (TextView) findViewById(R.id.bg37);
        displayBoard[4][7] = (TextView) findViewById(R.id.F47);
        displayBoardBg[4][7] = (TextView) findViewById(R.id.bg47);
        displayBoard[5][7] = (TextView) findViewById(R.id.F57);
        displayBoardBg[5][7] = (TextView) findViewById(R.id.bg57);
        displayBoard[6][7] = (TextView) findViewById(R.id.F67);
        displayBoardBg[6][7] = (TextView) findViewById(R.id.bg67);
        displayBoard[7][7] = (TextView) findViewById(R.id.F77);
        displayBoardBg[7][7] = (TextView) findViewById(R.id.bg77);

        for (int i=0; i<8; i++) { //colors switch in original setup, lazy fix
            for (int j=0; j<8; j++){
                if ((i+j)%2 == 0){
                    displayBoardBg[j][i].setBackgroundResource(R.color.colorBoardLight);
                }else {
                    displayBoardBg[j][i].setBackgroundResource(R.color.colorBoardDark);
                }
            }
        }
        SetDownPieces();
    }

    private void SetDownPieces() {
        for (int i=0; i<8; i++){
            for (int j=0; j<8; j++){
                ChessPiece currPiece = chessBoard.grid[i][j].getPiece();
                if (currPiece != null) {
                    if (currPiece instanceof King) {
                        if (currPiece.getColor() == 0) { //white
                            displayBoard[i][j].setBackgroundResource(R.drawable.wking);
                        } else {
                            displayBoard[i][j].setBackgroundResource(R.drawable.bking);
                        }
                    } else if (currPiece instanceof Queen) {
                        if (currPiece.getColor() == 0) { //white
                            displayBoard[i][j].setBackgroundResource(R.drawable.wqueen);
                        } else {
                            displayBoard[i][j].setBackgroundResource(R.drawable.bqueen);
                        }
                    } else if (currPiece instanceof Bishop) {
                        if (currPiece.getColor() == 0) { //white
                            displayBoard[i][j].setBackgroundResource(R.drawable.wbishop);
                        } else {
                            displayBoard[i][j].setBackgroundResource(R.drawable.bbishop);
                        }
                    } else if (currPiece instanceof Knight) {
                        if (currPiece.getColor() == 0) { //white
                            displayBoard[i][j].setBackgroundResource(R.drawable.wknight);
                        } else {
                            displayBoard[i][j].setBackgroundResource(R.drawable.bknight);
                        }
                    } else if (currPiece instanceof Rook) {
                        if (currPiece.getColor() == 0) { //white
                            displayBoard[i][j].setBackgroundResource(R.drawable.wrook);
                        } else {
                            displayBoard[i][j].setBackgroundResource(R.drawable.brook);
                        }
                    } else if (currPiece instanceof Pawn) {
                        if (currPiece.getColor() == 0) { //white
                            displayBoard[i][j].setBackgroundResource(R.drawable.wpawn);
                        } else {
                            displayBoard[i][j].setBackgroundResource(R.drawable.bpawn);
                        }
                    }
                }else{
                    displayBoard[i][j].setBackgroundResource(0);
                }
            }
        }
    }
    public void clearBoardSelections(){
        for (int i=0; i<8; i++){
            for (int j=0; j<8; j++){
                if ((i + j) % 2 == 0){
                    displayBoardBg[j][i].setBackgroundResource(R.color.colorBoardLight);
                }else {
                    displayBoardBg[j][i].setBackgroundResource(R.color.colorBoardDark);
                }
            }
        }
    }
    public void deselect(View v){
        int currColor = whiteTurn ? 0 : 1;
        firstClick = true;
        clearBoardSelections();
        if (isKingInCheck(currColor, findKingPosition(currColor, chessBoard), chessBoard)){
            Spot kingSpot = findKingPosition(currColor, chessBoard);
            displayBoardBg[kingSpot.getXCoordinate()][kingSpot.getYCoordinate()].setBackgroundResource(R.color.colorKingInDanger);
        }
        deselect.setVisibility(View.INVISIBLE);
        fromSpot = chessBoard.grid[0][0];
    }
    @Override
    public void onClick(View v){
        //Log.d("ChessApp", "CLICKED" + v.getId());
        //Toast newToast = Toast.makeText(this, v.getId()+"!", Toast.LENGTH_SHORT);
        //newToast.show();
        switch (v.getId()){
            case R.id.F00:
                clickedSpot = chessBoard.grid[0][0];
                break;
            case R.id.F10:
                clickedSpot = chessBoard.grid[1][0];
                break;
            case R.id.F20:
                clickedSpot = chessBoard.grid[2][0];
                break;
            case R.id.F30:
                clickedSpot = chessBoard.grid[3][0];
                break;
            case R.id.F40:
                clickedSpot = chessBoard.grid[4][0];
                break;
            case R.id.F50:
                clickedSpot = chessBoard.grid[5][0];
                break;
            case R.id.F60:
                clickedSpot = chessBoard.grid[6][0];
                break;
            case R.id.F70:
                clickedSpot = chessBoard.grid[7][0];
                break;
            case R.id.F01:
                clickedSpot = chessBoard.grid[0][1];
                break;
            case R.id.F11:
                clickedSpot = chessBoard.grid[1][1];
                break;
            case R.id.F21:
                clickedSpot = chessBoard.grid[2][1];
                break;
            case R.id.F31:
                clickedSpot = chessBoard.grid[3][1];
                break;
            case R.id.F41:
                clickedSpot = chessBoard.grid[4][1];
                break;
            case R.id.F51:
                 clickedSpot = chessBoard.grid[5][1];
                break;
            case R.id.F61:
                clickedSpot = chessBoard.grid[6][1];
                break;
            case R.id.F71:
                clickedSpot = chessBoard.grid[7][1];
                break;
            case R.id.F02:
                clickedSpot = chessBoard.grid[0][2];
                break;
            case R.id.F12:
                clickedSpot = chessBoard.grid[1][2];
                break;
            case R.id.F22:
                clickedSpot = chessBoard.grid[2][2];
                break;
            case R.id.F32:
                clickedSpot = chessBoard.grid[3][2];
                break;
            case R.id.F42:
                clickedSpot = chessBoard.grid[4][2];
                break;
            case R.id.F52:
                clickedSpot = chessBoard.grid[5][2];
                break;
            case R.id.F62:
                clickedSpot = chessBoard.grid[6][2];
                break;
            case R.id.F72:
                clickedSpot = chessBoard.grid[7][2];
                break;
            case R.id.F03:
                clickedSpot = chessBoard.grid[0][3];
                break;
            case R.id.F13:
                clickedSpot = chessBoard.grid[1][3];
                break;
            case R.id.F23:
                clickedSpot = chessBoard.grid[2][3];
                break;
            case R.id.F33:
                clickedSpot = chessBoard.grid[3][3];
                break;
            case R.id.F43:
                clickedSpot = chessBoard.grid[4][3];
                break;
            case R.id.F53:
                clickedSpot = chessBoard.grid[5][3];
                break;
            case R.id.F63:
                clickedSpot = chessBoard.grid[6][3];
                break;
            case R.id.F73:
                clickedSpot = chessBoard.grid[7][3];
                break;
            case R.id.F04:
                clickedSpot = chessBoard.grid[0][4];
                break;
            case R.id.F14:
                clickedSpot = chessBoard.grid[1][4];
                break;
            case R.id.F24:
                clickedSpot = chessBoard.grid[2][4];
                break;
            case R.id.F34:
                clickedSpot = chessBoard.grid[3][4];
                break;
            case R.id.F44:
                clickedSpot = chessBoard.grid[4][4];
                break;
            case R.id.F54:
                clickedSpot = chessBoard.grid[5][4];
                break;
            case R.id.F64:
                clickedSpot = chessBoard.grid[6][4];
                break;
            case R.id.F74:
                clickedSpot = chessBoard.grid[7][4];
                break;
            case R.id.F05:
                clickedSpot = chessBoard.grid[0][5];
                break;
            case R.id.F15:
                clickedSpot = chessBoard.grid[1][5];
                break;
            case R.id.F25:
                clickedSpot = chessBoard.grid[2][5];
                break;
            case R.id.F35:
                clickedSpot = chessBoard.grid[3][5];
                break;
            case R.id.F45:
                clickedSpot = chessBoard.grid[4][5];
                break;
            case R.id.F55:
                clickedSpot = chessBoard.grid[5][5];
                break;
            case R.id.F65:
                clickedSpot = chessBoard.grid[6][5];
                break;
            case R.id.F75:
                clickedSpot = chessBoard.grid[7][5];
                break;
            case R.id.F06:
                clickedSpot = chessBoard.grid[0][6];
                break;
            case R.id.F16:
                clickedSpot = chessBoard.grid[1][6];
                break;
            case R.id.F26:

                clickedSpot = chessBoard.grid[2][6];
                break;
            case R.id.F36:
                clickedSpot = chessBoard.grid[3][6];
                break;
            case R.id.F46:
                clickedSpot = chessBoard.grid[4][6];
                break;
            case R.id.F56:
                clickedSpot = chessBoard.grid[5][6];
                break;
            case R.id.F66:
                clickedSpot = chessBoard.grid[6][6];
                break;
            case R.id.F76:
                clickedSpot = chessBoard.grid[7][6];
                break;
            case R.id.F07:
                clickedSpot = chessBoard.grid[0][7];
                break;
            case R.id.F17:
                clickedSpot = chessBoard.grid[1][7];
                break;
            case R.id.F27:
                clickedSpot = chessBoard.grid[2][7];
                break;
            case R.id.F37:
                clickedSpot = chessBoard.grid[3][7];
                break;
            case R.id.F47:
                clickedSpot = chessBoard.grid[4][7];
                break;
            case R.id.F57:
                clickedSpot = chessBoard.grid[5][7];
                break;
            case R.id.F67:
                clickedSpot = chessBoard.grid[6][7];
                break;
            case R.id.F77:
                clickedSpot = chessBoard.grid[7][7];
                break;
        }
        int currColor = whiteTurn ? 0 : 1;
        

        if (firstClick) {
            if (chessBoard.grid[clickedSpot.getXCoordinate()][clickedSpot.getYCoordinate()].isEmpty()) { //do nothing, invalid selection
                Log.d("ChessApp", "empty select!");
                return;
            }else if (chessBoard.grid[clickedSpot.getXCoordinate()][clickedSpot.getYCoordinate()].getPiece().getColor() != currColor) 
            {
                Toast.makeText(this, "BAd color!", Toast.LENGTH_SHORT).show();
                Log.d("ChessApp", "BAd color!");
                return;
            }
            else if (noValidMoves(clickedSpot, chessBoard.grid[clickedSpot.getXCoordinate()][clickedSpot.getYCoordinate()].getPiece())){
                Toast.makeText(this, "No possible moves " + chessBoard.grid[clickedSpot.getXCoordinate()][clickedSpot.getYCoordinate()].getPiece().getPieceName(), Toast.LENGTH_SHORT).show();
                Log.d("ChessApp", "No possible moves " + chessBoard.grid[clickedSpot.getXCoordinate()][clickedSpot.getYCoordinate()].getPiece().getPieceName());
                return;
            }
            else if (!canBlockCheck(clickedSpot) && !(chessBoard.grid[clickedSpot.getXCoordinate()][clickedSpot.getYCoordinate()].getPiece() instanceof King) && ((whiteTurn && isKingInCheck(0, findKingPosition(0, chessBoard), chessBoard)) || (!whiteTurn && isKingInCheck(1, findKingPosition(1, chessBoard), chessBoard)))){ //King is in check, move something else dummy
                Toast.makeText(this, "cannot block check!", Toast.LENGTH_SHORT).show();
                Log.d("ChessApp", "cannot block check!");
                return;
            }
            else{ 
                displayBoardBg[clickedSpot.getXCoordinate()][clickedSpot.getYCoordinate()].setBackgroundResource(R.color.colorSelected);
                Log.d("ChessApp", clickedSpot.getPiece().getPieceName() + " was selected!");

                deselect.setVisibility(View.VISIBLE);
                firstClick = false;
                fromSpot = clickedSpot;
            }
        }else
        { //second selection
                if (fromSpot.getPiece().validMoveWithoutCheck(chessBoard, fromSpot, clickedSpot) && chessBoard.isPathEmpty(fromSpot, clickedSpot)){ //valid move to empty space
                    boolean pawnPromo = false;
                    boolean enPassant = false;
                   
                    if (castledK) {
                        if (whiteTurn){
                            //move rook only as king moving is handled below
                            chessBoard.grid[5][7].setPiece(chessBoard.grid[7][7].getPiece());
                            displayBoard[5][7].setBackgroundResource(R.drawable.wrook);
                            chessBoard.grid[7][7].setPiece(null);
                            displayBoard[7][7].setBackgroundResource(0);
                            prevMoves.add("47,67,77,57");
                        }else{
                            chessBoard.grid[5][0].setPiece(chessBoard.grid[7][0].getPiece());
                            displayBoard[5][0].setBackgroundResource(R.drawable.brook);
                            chessBoard.grid[7][0].setPiece(null);
                            displayBoard[7][0].setBackgroundResource(0);
                            prevMoves.add("40,60,70,50");
                        }
                       // castledK = false;
                    }
                    if (castledQ) {
                        if (whiteTurn){
                            //same thing move rook only
                            chessBoard.grid[3][7].setPiece(chessBoard.grid[0][7].getPiece());
                            displayBoard[3][7].setBackgroundResource(R.drawable.wrook);
                            chessBoard.grid[0][7].setPiece(null);
                            displayBoard[0][7].setBackgroundResource(0);
                            prevMoves.add("47,27,07,37");

                        }else{
                            chessBoard.grid[3][0].setPiece(chessBoard.grid[0][0].getPiece());
                            displayBoard[3][0].setBackgroundResource(R.drawable.brook);
                            chessBoard.grid[0][0].setPiece(null);
                            displayBoard[0][0].setBackgroundResource(0);
                            prevMoves.add("40,20,00,30");
                        }

                    }


                    
                    ChessPiece ChesPieTemp = fromSpot.getPiece();
                    
                    ChessPiece destPiece=clickedSpot.getPiece();
                    int intXTemp1 = clickedSpot.getXCoordinate();
                    int intYTemp1 = clickedSpot.getYCoordinate();
                    int intXStart = fromSpot.getXCoordinate();
                    int intYStart = fromSpot.getYCoordinate();
                    start=Integer.toString(intXStart) + Integer.toString(intYStart) + "," + Integer.toString(intXTemp1) + Integer.toString(intYTemp1);
                    current=ChesPieTemp;
                    before=destPiece;

                    ChesPieTemp.setFirst(false);
                    

                    if (ChesPieTemp instanceof Pawn) { 
                        Pawn currPawn = (Pawn) ChesPieTemp;
                        if (whiteTurn) {
                            if (fromSpot.getYCoordinate() == 1)
                            {

                                pawnPromoer.setVisibility(View.VISIBLE);

                                pawnPromo = true;
                                xStart=intXStart;
                                yStart=intYStart;
                                xEnd=intXTemp1;
                                yEnd=intYTemp1;
                                
                            }
                            else if (currPawn.getEnPassant())
                            {
                                enPassant = true;
                                
                                chessBoard.grid[intXTemp1][intYTemp1+1].setPiece(null);
                                displayBoard[intXTemp1][intYTemp1+1].setBackgroundResource(0);
                                clickedSpot.setPiece(ChesPieTemp);
                                displayBoard[intXTemp1][intYTemp1].setBackgroundResource(R.drawable.wpawn);
                                thirdMove=Integer.toString(intXTemp1)+Integer.toString(intYTemp1+1);
                            }
                        }
                        else
                        {
                            if (fromSpot.getYCoordinate() == 6){
                            
                                pawnPromoer.setVisibility(View.VISIBLE);
                                pawnPromo = true;
                                xStart=intXStart;
                                yStart=intYStart;
                                xEnd=intXTemp1;
                                yEnd=intYTemp1;
                              
                            }else if (currPawn.getEnPassant()){
                                enPassant = true;
                                chessBoard.grid[intXTemp1][intYTemp1-1].setPiece(null);
                                displayBoard[intXTemp1][intYTemp1-1].setBackgroundResource(0);
                                clickedSpot.setPiece(ChesPieTemp);
                                displayBoard[intXTemp1][intYTemp1].setBackgroundResource(R.drawable.bpawn);
                                thirdMove=Integer.toString(intXTemp1)+ Integer.toString(intYTemp1-1);
                            }
                        }
                    }
                    if (!pawnPromo && !enPassant) {
                        

                        clickedSpot.setPiece(ChesPieTemp);
                        displayBoard[intXTemp1][intYTemp1].setBackgroundResource(getResource(ChesPieTemp));
                        fromSpot.setPiece(null);
                        displayBoard[intXStart][intYStart].setBackgroundResource(0);
                
                        if (whiteTurn && isKingInCheck(0,findKingPosition(0, chessBoard), chessBoard)) {
                            Log.d("ChessApp","Bad Move");
                            fromSpot.setPiece(ChesPieTemp);
                            displayBoard[intXStart][intYStart].setBackgroundResource(getResource(ChesPieTemp));
                            clickedSpot.setPiece(destPiece);
                            displayBoard[intXTemp1][intYTemp1].setBackgroundResource(getResource(destPiece));
                            return;
                        }
                        else if (!whiteTurn && isKingInCheck(1,findKingPosition(1, chessBoard), chessBoard)) {
                            Log.d("ChessApp","Bad Move");
                            fromSpot.setPiece(ChesPieTemp);
                            displayBoard[intXStart][intYStart].setBackgroundResource(getResource(ChesPieTemp));
                            clickedSpot.setPiece(destPiece);
                            displayBoard[intXTemp1][intYTemp1].setBackgroundResource(getResource(destPiece));
                            return;
                        }

                    }
                    fromSpot.setPiece(null);
                    displayBoard[intXStart][intYStart].setBackgroundResource(0);
                    if (!castledK && !castledQ && !pawnPromo) {
                        if (thirdMove==null) {
                            prevMoves.add(Integer.toString(fromSpot.getXCoordinate()) + Integer.toString(fromSpot.getYCoordinate()) + "," + Integer.toString(clickedSpot.getXCoordinate()) + Integer.toString(clickedSpot.getYCoordinate()));
                        }
                        else{
                            prevMoves.add(Integer.toString(fromSpot.getXCoordinate()) + Integer.toString(fromSpot.getYCoordinate()) + "," + Integer.toString(clickedSpot.getXCoordinate()) + Integer.toString(clickedSpot.getYCoordinate()) + "," + thirdMove);  //for enpassant
                            }
                    }
                    castledK = false;
                    castledQ=false;

                  
                    if ((intXStart + intYStart)%2 == 0) {
                        displayBoardBg[intXStart][intYStart].setBackgroundResource(R.color.colorBoardLight);
                    }else {
                        displayBoardBg[intXStart][intYStart].setBackgroundResource(R.color.colorBoardDark);
                    }
                    if (!isKingInCheck(currColor, findKingPosition(currColor, chessBoard), chessBoard)){
                        Spot kingSpot = findKingPosition(currColor, chessBoard);
                        if ((kingSpot.getXCoordinate() + kingSpot.getYCoordinate()) % 2 == 0) {
                            displayBoardBg[kingSpot.getXCoordinate()][kingSpot.getYCoordinate()].setBackgroundResource(R.color.colorBoardLight);
                        }else {
                            displayBoardBg[kingSpot.getXCoordinate()][kingSpot.getYCoordinate()].setBackgroundResource(R.color.colorBoardDark);
                        }
                    }
                    firstClick = true;
                    whiteTurn = whiteTurn ? false : true; 
                    deselect.setVisibility(View.INVISIBLE);
                    pawnPromo = false; 
                    enPassant = false;
                }
        }
        if (firstClick) {
            if (whiteTurn) {
               
                boolean check = isKingInCheck(0, findKingPosition(0, chessBoard), chessBoard);
                Spot kingSpot = findKingPosition(0, chessBoard);
                if (check) {
                    displayBoardBg[kingSpot.getXCoordinate()][kingSpot.getYCoordinate()].setBackgroundResource(R.color.colorKingInDanger);
                    boolean checkmate = AmI_InChkMat(0, chessBoard);
                    if (checkmate) {
                        gameOver.setText("Black Wins!");
                        Log.d("moves",prevMoves.toString());
                        gameOver.setVisibility(View.VISIBLE);
                        StopTheGame();
                    } else {
                        Log.d("ChessApp", "White King in Check");
                    }
                } else {
                    if ((kingSpot.getXCoordinate() + kingSpot.getYCoordinate()) % 2 == 0) {
                        displayBoardBg[kingSpot.getXCoordinate()][kingSpot.getYCoordinate()].setBackgroundResource(R.color.colorBoardLight);
                    }else {
                        displayBoardBg[kingSpot.getXCoordinate()][kingSpot.getYCoordinate()].setBackgroundResource(R.color.colorBoardDark);
                    }
                }
                Log.d("ChessApp", "White's move: ");

            } else {
                System.out.println();
                boolean check = isKingInCheck(1, findKingPosition(1, chessBoard), chessBoard);
                Spot kingSpot = findKingPosition(1, chessBoard);
                if (check) {
                    displayBoardBg[kingSpot.getXCoordinate()][kingSpot.getYCoordinate()].setBackgroundResource(R.color.colorKingInDanger);
                    boolean checkmate = AmI_InChkMat(1, chessBoard);
                    if (checkmate) {
                        gameOver.setText("White Wins!");
                        Log.d("moves",prevMoves.toString());
                        gameOver.setVisibility(View.VISIBLE);
                        StopTheGame();
                    } else {
                        Log.d("ChessApp", "Black King in Check");
                    }
                } else {
                    if ((kingSpot.getXCoordinate() + kingSpot.getYCoordinate()) % 2 == 0) {
                        displayBoardBg[kingSpot.getXCoordinate()][kingSpot.getYCoordinate()].setBackgroundResource(R.color.colorBoardLight);
                    }else {
                        displayBoardBg[kingSpot.getXCoordinate()][kingSpot.getYCoordinate()].setBackgroundResource(R.color.colorBoardDark);
                    }
                }
                Log.d("ChessApp", "Black's Move");
            }
        }
        lastAI=false;
      
        thirdMove=null;
    }
    public void pawnPick(View v){
        int currColor = whiteTurn ? 1 : 0; 
        switch (v.getId()){
            case R.id.pawn2queen :
                chessBoard.grid[clickedSpot.getXCoordinate()][clickedSpot.getYCoordinate()].setPiece(new Queen(currColor));
                thirdMove="Q";
                if (!whiteTurn){
                    displayBoard[clickedSpot.getXCoordinate()][clickedSpot.getYCoordinate()].setBackgroundResource(R.drawable.wqueen);
                }else{
                    displayBoard[clickedSpot.getXCoordinate()][clickedSpot.getYCoordinate()].setBackgroundResource(R.drawable.bqueen);
                }
                break;
            case R.id.pawn2rook :
                chessBoard.grid[clickedSpot.getXCoordinate()][clickedSpot.getYCoordinate()].setPiece(new Rook(currColor));
                thirdMove="R";
                if (!whiteTurn){
                    displayBoard[clickedSpot.getXCoordinate()][clickedSpot.getYCoordinate()].setBackgroundResource(R.drawable.wrook);
                }else{
                    displayBoard[clickedSpot.getXCoordinate()][clickedSpot.getYCoordinate()].setBackgroundResource(R.drawable.brook);
                }
                break;
            case R.id.pawn2bishop :
                chessBoard.grid[clickedSpot.getXCoordinate()][clickedSpot.getYCoordinate()].setPiece(new Bishop(currColor));
                thirdMove="B";
                if (!whiteTurn){
                    displayBoard[clickedSpot.getXCoordinate()][clickedSpot.getYCoordinate()].setBackgroundResource(R.drawable.wbishop);
                }else{
                    displayBoard[clickedSpot.getXCoordinate()][clickedSpot.getYCoordinate()].setBackgroundResource(R.drawable.bbishop);
                }
                break;
            case R.id.pawn2knight :
                chessBoard.grid[clickedSpot.getXCoordinate()][clickedSpot.getYCoordinate()].setPiece(new Knight(currColor));
                thirdMove="K";
                if (!whiteTurn){
                    displayBoard[clickedSpot.getXCoordinate()][clickedSpot.getYCoordinate()].setBackgroundResource(R.drawable.wknight);
                }else{
                    displayBoard[clickedSpot.getXCoordinate()][clickedSpot.getYCoordinate()].setBackgroundResource(R.drawable.bknight);
                }
                break;
        }
      
        prevMoves.add(Integer.toString(xStart)+Integer.toString(yStart)+ "," + Integer.toString(xEnd) + Integer.toString(yEnd) + "," + thirdMove);
        pawnPromoer.setVisibility(View.INVISIBLE);

    }
    private boolean noValidMoves(Spot selectedSpot, ChessPiece piece){
        int currColor = whiteTurn ? 0 : 1;
        for (int i=0; i<8; i++){
            for (int j=0; j<8; j++){
         
                if (piece.validMoveWithoutCheck(chessBoard, selectedSpot, chessBoard.grid[i][j]) && chessBoard.isPathEmpty(selectedSpot, chessBoard.grid[i][j])){
                    if (piece instanceof King && ((selectedSpot.getXCoordinate() == 4 && selectedSpot.getYCoordinate() == 7 && currColor == 0) || (selectedSpot.getXCoordinate() == 4 && selectedSpot.getYCoordinate() == 0 && currColor == 1))){
                        castledK = false;
                        castledQ = false;
                        piece.setFirst(true);
                        Log.d("ChessApp", "Castling flags reset!");
                    }
                    if (piece instanceof Pawn){
                        ((Pawn) piece).enPassant = false;
                    }
                    Log.d("ChessApp", "Valid move to (" + i + "," + j + ")");
                    return false;
                }
            }
        }
        return true;
    }

    public boolean canBlockCheck(Spot currSpot){
        int currColor = whiteTurn ? 0 : 1;
        for (int a=0;a<8;a++) {
            for (int b=0;b<8;b++) {
                Spot newPosition=chessBoard.grid[b][a];
                ChessPiece newPositionPiece=newPosition.getPiece();
                ChessPiece current = currSpot.getPiece();
                if (current.validMoveWithoutCheck(chessBoard, currSpot, newPosition) && chessBoard.isPathEmpty(currSpot, newPosition)) {
                    if (current instanceof King && ((currSpot.getXCoordinate() == 4 && currSpot.getYCoordinate() == 7 && currColor == 0) || (currSpot.getXCoordinate() == 4 && currSpot.getYCoordinate() == 0 && currColor == 1))){ //dangerous
                        castledK = false;
                        castledQ = false;
                        current.setFirst(true);
                        Log.d("ChessApp", "Castling flags reset!");
                    }

                    newPosition.setPiece(current);
                    currSpot.setPiece(null);
                    if (!isKingInCheck(currColor,findKingPosition(currColor, chessBoard), chessBoard)) 
                    {
                  
                        currSpot.setPiece(current);
                        newPosition.setPiece(newPositionPiece);
                        return true;
                    }
                }
                currSpot.setPiece(current);
                newPosition.setPiece(newPositionPiece);
            }
        }
        return false;
    }

    private int getResource(ChessPiece piece){
        if (piece != null && piece.getColor() == 0){

            if (piece instanceof Pawn){
                return R.drawable.wpawn;
            }else if (piece instanceof Rook){
                return R.drawable.wrook;
            }else if (piece instanceof Knight){
                return R.drawable.wknight;
            }else if (piece instanceof Bishop){
                return R.drawable.wbishop;
            }else if (piece instanceof Queen){
                return R.drawable.wqueen;
            }else if (piece instanceof King){
                return R.drawable.wking;
            }
        }else if (piece != null && piece.getColor() == 1){
            if (piece instanceof Pawn){
                return R.drawable.bpawn;
            }else if (piece instanceof Rook){
                return R.drawable.brook;
            }else if (piece instanceof Knight){
                return R.drawable.bknight;
            }else if (piece instanceof Bishop){
                return R.drawable.bbishop;
            }else if (piece instanceof Queen){
                return R.drawable.bqueen;
            }else if (piece instanceof King){
                return R.drawable.bking;
            }
        }
        return 0;
    }


    public void StopTheGame(){
        isGameOver = true;

        for (int i=0; i<8; i++){
            for (int j=0; j<8; j++){
                displayBoard[j][i].setClickable(false);
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("U want to record")
                .setTitle("GAME OVER");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String winner=whiteTurn? "Black":"White";
                if (draw){
                    winner="Draw";
                }
                Intent intent = new Intent(MainActivity.this, SaveGameScreen.class);
                intent.putExtra("moves", prevMoves);
                intent.putExtra("winner",winner);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(MainActivity.this, HomePage.class);
                startActivity(intent);

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}