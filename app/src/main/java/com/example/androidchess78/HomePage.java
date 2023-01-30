package com.example.androidchess78;

import GameRecording.GameSave;
import GameRecording.LoadSaveData;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomePage extends AppCompatActivity {

    private Button gameButton;
    private Button dateSort;
    private Button nameSort;
    private ListView gamesListView;
    public static List<GameSave> gamesList=new ArrayList<>();   // List which stores all recorded games

   // GameSave gameSave=new GameSave();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        gameButton=(Button) findViewById(R.id.newGame);
        dateSort=(Button) findViewById(R.id.dateSort);
        nameSort=(Button) findViewById(R.id.nameSort);
        gamesListView=(ListView)findViewById(R.id.recordedGamesList);
        LoadSaveData.context = getApplicationContext();
        LoadSaveData.ReadFromFile();




       if(LoadSaveData.list != null) {

            String[] games = new String[LoadSaveData.list.size()];

            for (int i = 0; i <  LoadSaveData.list.size(); i++) {
                games[i] = LoadSaveData.list.get(i).toString();
            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, games);
            gamesListView.setAdapter(arrayAdapter);
        }

        gamesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                GameSave gameData=LoadSaveData.list.get(i);
                Log.d("Print",gameData.getMoves().toString());
                Intent intent=new Intent(HomePage.this, GamePlaybackActivity.class);
                intent.putExtra("moves",gameData.getMoves());
                startActivity(intent);

            }
        });


        gameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(HomePage.this, MainActivity.class);
                startActivity(intent);
            }
        });
        dateSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(LoadSaveData.list != null) {
                    Collections.sort(LoadSaveData.list, new sortByDate());
                    String[] StrArry1 = new String[LoadSaveData.list.size()];
                    for (int i = 0; i<LoadSaveData.list.size(); i++) {
                        StrArry1[i] = LoadSaveData.list.get(i).toString();
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(HomePage.this, android.R.layout.simple_list_item_1, StrArry1);
                    gamesListView.setAdapter(arrayAdapter);
                }
                else{
                    Toast.makeText(HomePage.this,"No saved games to sort!",Toast.LENGTH_LONG).show();
                }
            }
        });

             nameSort.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     if(LoadSaveData.list != null) {
                         Collections.sort(LoadSaveData.list, new sortByName());
                         String[] StrArry1 = new String[LoadSaveData.list.size()];
                         for (int i = 0; i<LoadSaveData.list.size(); i++) {
                             StrArry1[i] = LoadSaveData.list.get(i).toString();
                         }
                         ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(HomePage.this, android.R.layout.simple_list_item_1, StrArry1);
                         gamesListView.setAdapter(arrayAdapter);
                     }
                     else{
                         Toast.makeText(HomePage.this,"No saved games to sort!",Toast.LENGTH_LONG).show();
                     }
                 }
             });

    }



    class sortByName implements Comparator<GameSave> {
        public int compare(GameSave o1, GameSave o2) {
            if(o1 != null && o2 != null)
            {
                return o1.gameName.compareToIgnoreCase(o2.gameName);
            }
            return 0;

        }
    }
    class sortByDate implements Comparator<GameSave> {
        public int compare(GameSave o1, GameSave o2) {
            if(o1 != null && o2 != null)
            {
                return o2.gameDate.compareTo(o1.gameDate);
            }
            return 0;
        }
    }





}