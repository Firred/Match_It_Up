package com.example.matchitup;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {
    private final String STATE_LANGUAGE = "language", STATE_EASY = "language",
            STATE_MEDIUM = "language", STATE_HARD = "language";
    private Spinner spinner;
    private String currentLanguage;
    private List<String> availableLanguages;
    private TextView easyRecord, mediumRecord, hardRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            LocaleManager.setLocale(this, savedInstanceState.getString(STATE_LANGUAGE));
        }
        currentLanguage = getResources().getConfiguration().locale.getLanguage();
        setContentView(R.layout.activity_profile);

        setupSpinner();
        easyRecord = findViewById(R.id.easyRecord);
        mediumRecord = findViewById(R.id.mediumRecord);
        hardRecord = findViewById(R.id.hardRecord);

        //TODO: RECUPERAR RECORDS Y HACER SET AQUI
        if (savedInstanceState != null) {
            setRecords(savedInstanceState.getString(STATE_EASY),
                    savedInstanceState.getString(STATE_MEDIUM),
                    savedInstanceState.getString(STATE_HARD));
        }
        else {
            setRecords(
                    this.getSharedPreferences("matchPref", Context.MODE_PRIVATE).
                            getInt("easy", 0),
                    this.getSharedPreferences("matchPref", Context.MODE_PRIVATE).
                            getInt("medium", 0),
                    this.getSharedPreferences("matchPref", Context.MODE_PRIVATE).
                            getInt("hard", 0)
            );
        }

    }

    /**
     * Changes the locale of the application, updating its preference.
     * @param localeName
     */
    public void setLocale(String localeName) {
        if (!localeName.equals(currentLanguage)) {
            currentLanguage = localeName;
            LocaleManager.setLocale(this, localeName);

            SharedPreferences.Editor editor = this.getSharedPreferences("matchPref", Context.MODE_PRIVATE).edit();
            editor.putString("language_key", localeName);
            editor.commit();

            Intent refresh = new Intent(this, MainActivity.class);
            finish();
            startActivity(refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }


    @SuppressLint("SetTextI18n")
    private void setRecords(int easy, int medium, int hard){
        easyRecord.setText(getString(R.string.level_easy) + ": " + easy);
        mediumRecord.setText(getString(R.string.level_medium) + ": " + medium);
        hardRecord.setText(getString(R.string.level_hard) + ": " + hard);
    }

    private void setRecords(String easy, String medium, String hard){
        easyRecord.setText(easy);
        mediumRecord.setText(medium);
        hardRecord.setText(hard);
    }

    private void setupSpinner(){
        availableLanguages = new ArrayList<>(Arrays.asList(
                getString(R.string.select_language), "English", "Español",
                "Français", "Italiano", "Português")
        );
        spinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, availableLanguages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    case 0: break;
                    case 1: setLocale("en"); break;
                    case 2: setLocale("es"); break;
                    case 3: setLocale("fr"); break;
                    case 4: setLocale("it"); break;
                    case 5: setLocale("pt"); break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(
                STATE_LANGUAGE, this.getSharedPreferences("matchPref",
                        Context.MODE_PRIVATE).getString("language_key",
                        Locale.getDefault().getLanguage())
        );
        savedInstanceState.putString(STATE_EASY, easyRecord.getText().toString());
        savedInstanceState.putString(STATE_MEDIUM, mediumRecord.getText().toString());
        savedInstanceState.putString(STATE_HARD, hardRecord.getText().toString());

        super.onSaveInstanceState(savedInstanceState);
    }
}
