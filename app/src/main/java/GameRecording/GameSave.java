package GameRecording;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

public class GameSave implements Serializable {
    private static final long serialVersionUID = -5381145134256449565L;
    public String gameName;
    public Calendar gameDate;
    private String winner;
    public ArrayList<String> gameMoves;
    public GameSave(String name, String winner, ArrayList<String> moves){
        this.gameName=name;
        this.gameDate=Calendar.getInstance();
        this.winner=winner;
        this.gameMoves=moves;

    }

    public String getGameName(){
        return this.gameName;
    }

    public Calendar getGameDate() {
        return this.gameDate;
    }

    public String getWinner() {
        return this.winner;
    }

    public  ArrayList<String> getMoves(){
        return this.gameMoves;
    }

    public String toString(){
        return this.getGameName()+ "|" + this.gameDate.getTime().toString() + "|" + this.getWinner();
    }
}
