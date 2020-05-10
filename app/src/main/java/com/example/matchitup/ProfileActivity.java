package com.example.matchitup;

import androidx.appcompat.app.AppCompatActivity;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {
    private final String STATE_LANGUAGE = "language";
    Spinner spinner;
    String currentLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            LocaleManager.setLocale(this, savedInstanceState.getString(STATE_LANGUAGE));
        }

        currentLanguage = getResources().getConfiguration().locale.getLanguage();

        setContentView(R.layout.activity_profile);

        //TODO: Esto es para las pruebas, cambiar por algo más bonito
        spinner = (Spinner) findViewById(R.id.spinner);

        List<String> list = new ArrayList<>();

        list.add("Select language");
        list.add("English");
        list.add("Español");
        list.add("Français");
        list.add("Italiano");
        list.add("Portugues");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        setLocale("en");
                        break;
                    case 2:
                        setLocale("es");
                        break;
                    case 3:
                        setLocale("fr");
                        break;
                    case 4:
                        setLocale("it");
                        break;
                    case 5:
                        setLocale("pt");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        System.out.println("Hola estoy en profile");
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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(STATE_LANGUAGE,
                this.getSharedPreferences("matchPref", Context.MODE_PRIVATE)
                        .getString("language_key", Locale.getDefault().getLanguage()));
        super.onSaveInstanceState(savedInstanceState);
    }
}
