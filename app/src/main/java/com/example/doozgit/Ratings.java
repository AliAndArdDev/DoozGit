package com.example.doozgit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class Ratings extends AppCompatActivity {

    TextView player1TextView, player2TextView, versusTextView, notEnoughAccountsTextView;
    Button createAccountButton, doneButton;
    ProgressBar progressBar;
    ListView ratings;
    SharedPreferences dataStorage;
    Thread listPreparingThread;
    Runnable listPreparingRunnable;
    ArrayList<String> standings;
    Intent intent;
    int accountsNumber;
    boolean isOnQuickPlayMode, isOnTournamentMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratings);

        player1TextView = findViewById(R.id.player1_textView);
        player2TextView = findViewById(R.id.player2_textView);
        notEnoughAccountsTextView = findViewById(R.id.not_enough_accounts_textView);
        versusTextView = findViewById(R.id.versus_button);
        createAccountButton = findViewById(R.id.create_account_button);
        doneButton = findViewById(R.id.done_button);
        progressBar = findViewById(R.id.progressBar2);
        ratings = findViewById(R.id.ratings_listView);
        intent = getIntent();
        isOnQuickPlayMode = intent.getBooleanExtra("isOnQuickPlayMode", false);
        isOnTournamentMode = intent.getBooleanExtra("isOnTournamentMode", false);
        dataStorage = PreferenceManager.getDefaultSharedPreferences(this);
        standings = new ArrayList<>();
        accountsNumber = dataStorage.getInt("accountsNumber", 0);
        listPreparingThread = new Thread(listPreparingRunnable);
        listPreparingRunnable = new Runnable() {
            @Override
            public void run() {
                ArrayList scores;
                ArrayList<String> ownerNames;
                scores = new ArrayList();
                ownerNames = new ArrayList<>();
                for (int i = 1; i < accountsNumber + 1; i++) {
                    ownerNames.add(dataStorage.getString("ownerName" + Integer.valueOf(i)
                            .toString(), ""));
                    scores.add(dataStorage.getString("score" + Integer.valueOf(i).toString(),
                            ""));
                }
                standings = rateAccounts(scores, ownerNames);
                ratings.setAdapter(new ArrayAdapter(Ratings.this, android.R.layout
                        .simple_list_item_1, standings));
                progressBar.setVisibility(View.GONE);
                ratings.setVisibility(View.VISIBLE);
                if (accountsNumber == 1) {
                    notEnoughAccountsTextView.setVisibility(View.VISIBLE);
                    createAccountButton.setVisibility(View.VISIBLE);
                }
            }
        };

        if (accountsNumber != 0){
            listPreparingThread.start();
        } else {
            progressBar.setVisibility(View.GONE);
            notEnoughAccountsTextView.setVisibility(View.VISIBLE);
            createAccountButton.setVisibility(View.VISIBLE);
        }

        if (isOnQuickPlayMode){
            player1TextView.setVisibility(View.VISIBLE);
            versusTextView.setVisibility(View.VISIBLE);
            player2TextView.setVisibility(View.VISIBLE);
            doneButton.setVisibility(View.VISIBLE);
        }

    }

    public static ArrayList<String> rateAccounts(ArrayList scores, ArrayList<String> ownerNames){
        ArrayList<String> standings;
        int index, constant;
        standings = new ArrayList<>();
        constant = scores.size();

        for (int i = 0; i < constant; i++) {
            index = 0;
            for (int j = 0; j < scores.size(); j++) {
                if ((int)scores.get(index) <= (int)scores.get(j))
                    index = scores.indexOf(scores.get(j));
            }
            standings.add(ownerNames.get(index));
            standings.set(standings.size()-1, standings.get(standings.size()-1).concat(" : " +
                    Integer.valueOf((int)scores.get(index)).toString()));
            scores.remove(index);
            ownerNames.remove(index);
        }
        return standings;
    }
}
