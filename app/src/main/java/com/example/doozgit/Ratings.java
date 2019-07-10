package com.example.doozgit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class Ratings extends AppCompatActivity {

    TextView player1TextView, player2TextView, versusTextView;
    Button createAccountButton, doneButton;
    ProgressBar progressBar;
    ListView ratings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratings);

        player1TextView = findViewById(R.id.player1_textView);
        player2TextView = findViewById(R.id.player2_textView);
        versusTextView = findViewById(R.id.versus_button);
        createAccountButton = findViewById(R.id.create_account_button);
        doneButton = findViewById(R.id.done_button);
        progressBar = findViewById(R.id.progressBar2);
        ratings = findViewById(R.id.ratings_listView);



    }

    private ArrayList rateAccounts(ArrayList numbers, ArrayList names){
        ArrayList standings;
        int index, constant;
        standings = new ArrayList();
        constant = numbers.size();

        for (int i = 0; i < constant; i++) {
            index = 0;
            for (int j = 0; j < numbers.size(); j++) {
                if ((int)numbers.get(index) <= (int)numbers.get(j))
                    index = numbers.indexOf((Object)numbers.get(j));
            }
            standings.add(numbers.get(index));
            numbers.remove(index);
        }
        return standings;
    }
}
