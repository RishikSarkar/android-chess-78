package com.example.androidchess78;

import androidx.appcompat.app.AppCompatActivity;
import chess.Board;
import chess.Spot;
import pieces.Bishop;
import pieces.ChessPiece;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Queen;
import pieces.Rook;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GamePlaybackActivity extends AppCompatActivity {
    public ArrayList<String> movesToExecute;
    private Button btnNext;
    public static Board chessBoard;
    public static boolean bCanWhiteMakeNextMove;
    public TextView[][] displayBoard;
    public TextView gameOver;
    int intMoveCtr;
    public TextView[][] displayBoardBg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_playback);
        bCanWhiteMakeNextMove=true;
        intMoveCtr=1;
        gameOver=(TextView)findViewById(R.id.gameOver);
        displayBoard = new TextView[8][8];
        displayBoardBg = new TextView[8][8];
       

        chessBoard = new Board();
        Intent intent=getIntent();
        movesToExecute = intent.getStringArrayListExtra("moves");
 

        Initialize10x10Board();
        btnNext=(Button)findViewById(R.id.nextMove);


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (intMoveCtr>movesToExecute.size()) {
                    Toast.makeText(GamePlaybackActivity.this,"No Possible Moves",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(GamePlaybackActivity.this,HomePage.class);
                    startActivity(intent);
                }
                else {

                    String move=movesToExecute.get(intMoveCtr-1);

                    String[] split=move.split(","); //46,44,
                    if (split.length==2) {
                        String start = split[0];
                        String end = split[1];

                        int intTempX = Character.getNumericValue(start.charAt(0));
                        int intTempY = Character.getNumericValue(start.charAt(1));
                        int intTempXEnd = Character.getNumericValue(end.charAt(0));
                        int intTempYEnd = Character.getNumericValue(end.charAt(1));


                        ChessPiece ChesPiec1 = chessBoard.grid[intTempX][intTempY].getPiece();
                        Spot SpotEnd = chessBoard.grid[intTempXEnd][intTempYEnd];
                        SpotEnd.setPiece(ChesPiec1);
                        displayBoard[intTempXEnd][intTempYEnd].setBackgroundResource(getResource(ChesPiec1));
                        chessBoard.grid[intTempX][intTempY].setPiece(null);
                        displayBoard[intTempX][intTempY].setBackgroundResource(0);
                    }
                    else if (split.length==3){
                        if (split[2].length()==1) {
                            String start = split[0];
                            String end = split[1];
                            String piece=split[2];

                            int intTempX = Character.getNumericValue(start.charAt(0));

                            int intTempY = Character.getNumericValue(start.charAt(1));

                            int intTempXEnd = Character.getNumericValue(end.charAt(0));
                            int intTempYEnd = Character.getNumericValue(end.charAt(1));

                            if (piece.equals("Q")){
                                if (bCanWhiteMakeNextMove) {
                                    displayBoard[intTempXEnd][intTempYEnd].setBackgroundResource(R.drawable.wqueen);
                                }
                                else{
                                    displayBoard[intTempXEnd][intTempYEnd].setBackgroundResource(R.drawable.bqueen);
                                }
                            }
                            else if (piece.equals("B")){
                                if (bCanWhiteMakeNextMove) {
                                    displayBoard[intTempXEnd][intTempYEnd].setBackgroundResource(R.drawable.wbishop);
                                }
                                else{
                                    displayBoard[intTempXEnd][intTempYEnd].setBackgroundResource(R.drawable.bbishop);
                                }
                            }
                            else if (piece.equals("R")){
                                if (bCanWhiteMakeNextMove) {
                                    displayBoard[intTempXEnd][intTempYEnd].setBackgroundResource(R.drawable.wrook);
                                }
                                else{
                                    displayBoard[intTempXEnd][intTempYEnd].setBackgroundResource(R.drawable.brook);
                                }
                            }
                            else if (piece.equals("K")){
                                if (bCanWhiteMakeNextMove) {
                                    displayBoard[intTempXEnd][intTempYEnd].setBackgroundResource(R.drawable.wknight);
                                }
                                else{
                                    displayBoard[intTempXEnd][intTempYEnd].setBackgroundResource(R.drawable.bknight);
                                }
                            }

                      
                            chessBoard.grid[intTempX][intTempY].setPiece(null);
                            displayBoard[intTempX][intTempY].setBackgroundResource(0);
                        }
                        else if (split[2].length()==2){  //enpassant
                            String start = split[0];
                            String end = split[1];
                            String pieceToDestroy=split[2];

                            int intTempX = Character.getNumericValue(start.charAt(0));
                            int intTempY = Character.getNumericValue(start.charAt(1));
                            int intTempXEnd = Character.getNumericValue(end.charAt(0));
                            int intTempYEnd = Character.getNumericValue(end.charAt(1));
                            int IntEndX=Character.getNumericValue(pieceToDestroy.charAt(0));
                            int IntEndY=Character.getNumericValue(pieceToDestroy.charAt(1));

                            ChessPiece ChesPiec1 = chessBoard.grid[intTempX][intTempY].getPiece();
                            Spot SpotEnd = chessBoard.grid[intTempXEnd][intTempYEnd];
                            SpotEnd.setPiece(ChesPiec1);
                            displayBoard[intTempXEnd][intTempYEnd].setBackgroundResource(getResource(ChesPiec1));
                            chessBoard.grid[intTempX][intTempY].setPiece(null);
                            displayBoard[intTempX][intTempY].setBackgroundResource(0);
                            chessBoard.grid[IntEndX][IntEndY].setPiece(null);
                            displayBoard[IntEndX][IntEndY].setBackgroundResource(0);

                        }

                    }
                    else if (split.length==4){   //castling
                        String StrKS = split[0];
                        String StrKE = split[1];
                        String StrRS=split[2];
                        String StrRE=split[3];

                        int startingKingX = Character.getNumericValue(StrKS.charAt(0));
                        int startingKingY = Character.getNumericValue(StrKS.charAt(1));
                        int endingKingX = Character.getNumericValue(StrKE.charAt(0));
                        int endingKingY = Character.getNumericValue(StrKE.charAt(1));

                        int startingRookX = Character.getNumericValue(StrRS.charAt(0));
                        int startingRookY = Character.getNumericValue(StrRS.charAt(1));
                        int endingRookX = Character.getNumericValue(StrRE.charAt(0));
                        int endingRookY = Character.getNumericValue(StrRE.charAt(1));

                        ChessPiece ChesPiec1 = chessBoard.grid[startingKingX][startingKingY].getPiece();
                        Spot SpotEnd = chessBoard.grid[endingKingX][endingKingY];
                        SpotEnd.setPiece(ChesPiec1);
                        displayBoard[endingKingX][endingKingY].setBackgroundResource(getResource(ChesPiec1));
                        chessBoard.grid[startingKingX][startingKingY].setPiece(null);
                        displayBoard[startingKingX][startingKingY].setBackgroundResource(0);

                        ChessPiece ChessPieNextMove = chessBoard.grid[startingRookX][startingRookY].getPiece();
                        Spot SpotNew = chessBoard.grid[endingRookX][endingRookY];
                        SpotNew.setPiece(ChessPieNextMove);
                        displayBoard[endingRookX][endingRookY].setBackgroundResource(getResource(ChessPieNextMove));
                        chessBoard.grid[startingRookX][startingRookY].setPiece(null);
                        displayBoard[startingRookX][startingRookY].setBackgroundResource(0);
                    }


                }
                bCanWhiteMakeNextMove = bCanWhiteMakeNextMove ? false : true;
                if (intMoveCtr==movesToExecute.size()){
                    gameOver.setText("Game Over!!");
                    gameOver.setVisibility(View.VISIBLE);
                }
                intMoveCtr++;

            }
        });
        StopTheGame();

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

        for (int i=0; i<8; i++){ //colors switch in original setup, lazy fix
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
                ChessPiece ChesPieTemp = chessBoard.grid[i][j].getPiece();
                if (ChesPieTemp != null) {
                    if (ChesPieTemp instanceof King) {
                        if (ChesPieTemp.getColor() == 0) { //white
                            displayBoard[i][j].setBackgroundResource(R.drawable.wking);
                        } else {
                            displayBoard[i][j].setBackgroundResource(R.drawable.bking);
                        }
                    } else if (ChesPieTemp instanceof Queen) {
                        if (ChesPieTemp.getColor() == 0) { //white
                            displayBoard[i][j].setBackgroundResource(R.drawable.wqueen);
                        } else {
                            displayBoard[i][j].setBackgroundResource(R.drawable.bqueen);
                        }
                    } else if (ChesPieTemp instanceof Bishop) {
                        if (ChesPieTemp.getColor() == 0) { //white
                            displayBoard[i][j].setBackgroundResource(R.drawable.wbishop);
                        } else {
                            displayBoard[i][j].setBackgroundResource(R.drawable.bbishop);
                        }
                    } else if (ChesPieTemp instanceof Knight) {
                        if (ChesPieTemp.getColor() == 0) { //white
                            displayBoard[i][j].setBackgroundResource(R.drawable.wknight);
                        } else {
                            displayBoard[i][j].setBackgroundResource(R.drawable.bknight);
                        }
                    } else if (ChesPieTemp instanceof Rook) {
                        if (ChesPieTemp.getColor() == 0) { //white
                            displayBoard[i][j].setBackgroundResource(R.drawable.wrook);
                        } else {
                            displayBoard[i][j].setBackgroundResource(R.drawable.brook);
                        }
                    } else if (ChesPieTemp instanceof Pawn) {
                        if (ChesPieTemp.getColor() == 0) { //white
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
    private int getResource(ChessPiece piece) {
        if (bCanWhiteMakeNextMove){
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
            }else{
                return 0;
            }
        }else{
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
            }else{
                return 0;
            }
        }
    }
    public void StopTheGame() {
        for (int i=0; i<8; i++){
            for (int j=0; j<8; j++){
                displayBoard[j][i].setClickable(false);
            }
        }
    }
}