package com.example.matchitup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class DictionaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        System.out.println("Hola estoy en diccionario");

    }
}
