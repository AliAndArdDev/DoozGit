package com.example.doozgit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class CreateAccount extends AppCompatActivity {

    EditText passwordEditText, ownerNameEditText;
    Button doneButton;
    ProgressBar progressBar;
    SharedPreferences dataStorage = PreferenceManager.getDefaultSharedPreferences(this);
    Intent intent;
    boolean isOnEditAccountMode;
    Thread findAccountThread;
    Runnable findAccountRunnable;
    String ownerName, password;
    int accountsNumber, accountPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        progressBar = findViewById(R.id.progressBar);
        passwordEditText = findViewById(R.id.password_editText);
        ownerNameEditText = findViewById(R.id.owner_name_editText);
        dataStorage = PreferenceManager.getDefaultSharedPreferences(this);
        intent = getIntent();
        accountsNumber = dataStorage.getInt("accountsNumber", 0);
        isOnEditAccountMode = intent.getBooleanExtra("isOnEditAccountMode", false);
        ownerName = intent.getStringExtra("ownerName");
        password = intent.getStringExtra("password");
        findAccountThread = new Thread(findAccountRunnable);
        findAccountRunnable = new Runnable() {
            @Override
            public void run() {
                accountPlace = findAccount();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        doneButton.setVisibility(View.VISIBLE);
                        ownerNameEditText.setText(ownerName);
                        passwordEditText.setText(password);
                    }
                });
            }
        };

        if (isOnEditAccountMode){
            findAccountThread.start();
        } else {
            progressBar.setVisibility(View.GONE);
            doneButton.setVisibility(View.VISIBLE);
        }

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordEditText.getText().toString().isEmpty() || ownerNameEditText.getText()
                        .toString().isEmpty()){
                    Toast.makeText(CreateAccount.this, "Fill all the fields out",
                            Toast.LENGTH_SHORT).show();
                } else {
                    String enteredPassword, enteredOwnerName;
                    enteredPassword = passwordEditText.getText().toString();
                    enteredOwnerName = ownerNameEditText.getText().toString();
                    if (isOnEditAccountMode){
                        String accountPlaceToString;
                        accountPlaceToString = Integer.valueOf(accountPlace).toString();
                        dataStorage.edit().putString("ownerName" + accountPlaceToString,
                                enteredOwnerName).apply();
                        dataStorage.edit().putString("password" + accountPlaceToString,
                                enteredPassword).apply();
                    } else {
                        String accountsNumberToString;
                        accountsNumber ++;
                        accountsNumberToString = Integer.valueOf(accountsNumber).toString();
                        dataStorage.edit().putString("ownerName" + accountsNumberToString,
                                enteredOwnerName).apply();
                        dataStorage.edit().putString("password" + accountsNumberToString,
                                enteredPassword).apply();
                        dataStorage.edit().putString("score" + accountsNumberToString,
                                enteredPassword).apply();
                    }
                    intent = new Intent(CreateAccount.this, Ratings.class);
                    startActivity(intent);
                }
            }
        });
    }

    private int findAccount(){
        for (int i = 1; i < accountsNumber; i++) {
            if (dataStorage.getString("ownerName" + Integer.valueOf(i), "")
                    .equalsIgnoreCase(ownerName)){
                return i;
            }
        }
        return 0;
    }

}
