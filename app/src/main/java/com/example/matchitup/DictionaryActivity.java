package com.example.matchitup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class DictionaryActivity extends AppCompatActivity {
    private final String STATE_LANGUAGE = "language";
    private DictionaryLoaderCallbacks bookLoaderCallbacks = new DictionaryLoaderCallbacks();
    private TextView word, description, examples;
    private ImageButton audio;
    private String audioUrl;

    private boolean internetConnectionAvailable(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;
        if (connMgr != null) {
            activeNetwork = connMgr.getActiveNetworkInfo();
        }
        return activeNetwork != null && activeNetwork.isConnected();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            LocaleManager.setLocale(this, savedInstanceState.getString(STATE_LANGUAGE));
        }

        setContentView(R.layout.activity_dictionary);

        this.word = findViewById(R.id.wordView);
        this.description = findViewById(R.id.descriptionView);
        this.examples = findViewById(R.id.examplesView);
        this.audio = findViewById(R.id.audioButton);


        System.out.println("Hola estoy en diccionario");
    }

    public void searchWord(View view) {
        String wordText = ((EditText)findViewById(R.id.wordText)).getText().toString();

        if(internetConnectionAvailable() && (wordText.length() != 0)) {
            Bundle queryBundle = new Bundle();
            queryBundle.putString(DictionaryLoaderCallbacks.PARAM_QUERY, wordText);
            LoaderManager.getInstance(this).restartLoader(WordLoader.WORD_LOADER_ID, queryBundle, bookLoaderCallbacks);
        }
    }

    public void playAudio(View view) {
        MediaPlayer mp = new MediaPlayer();

        try {
            mp.setDataSource(audioUrl);
            mp.prepare();
            mp.start();
        } catch (IOException e) {
            Log.e("DictActivity, audio", "prepare() failed");
        }
    }

    public class DictionaryLoaderCallbacks implements LoaderManager.LoaderCallbacks<List<Word>>  {
        public static final String PARAM_QUERY = "queryParam";

        /**
         * Si al llamar a initLoader() en onCreate() de MainActivity el ID no existe, se ejecuta esta
         * función. Si existía, borra el loader que había y vuelve aquí.
         */
        @Override
        public Loader<List<Word>> onCreateLoader(int id, @Nullable Bundle args) {
            return new WordLoader(DictionaryActivity.this, args.getString(PARAM_QUERY));
        }

        /**
         * Si en initLoader() del onCreate de MainActivity existiera el ID y ya ha generado datos, se
         * llama a esta función ya que significa que habría otro loader creado con el mismo ID.
         *
         * Esta función se ejecuta también cuando un Loader previo se ha terminado. Este suele ser el
         * punto en el que mueve los datos a las View mediante el adapter. La ejecución del
         * loadInBackground() pasa a esta función.
         */
        @Override
        public void onLoadFinished(@NonNull Loader<List<Word>> loader, List<Word> data) {
            if (data != null && data.size() > 0) {
                Word w = data.get(0);

                word.setText(w.getWord());

                if(w.getDef() != null)
                    description.setText(w.getDef());
                if(w.getExamples() != null)
                    examples.setText(w.getExamples().toString());
                if(w.getAudio() != null) {
                    audioUrl = w.getAudio();
                    audio.setVisibility(View.VISIBLE);
                }
                else {
                    audio.setVisibility(View.GONE);
                }
            }
            else {
                //TODO: Mensaje de palabra no encontrada (?)
            }

            destroyLoader(loader.getId());
        }

        /**
         * Cuando un Loader previamente creado se está resetando. En este punto la app debería eliminar
         * cualquier referencia que tenga de los datos del Loader.
         *
         * Esta función se llama solamente cuando el Loader actual se está destruyendo, por lo que se puede
         * dejar en blanco la mayoría del tiempo. No intentaremos acceder a datos después de hayan sido
         * borrados
         */
        @Override
        public void onLoaderReset(@NonNull Loader<List<Word>> loader) {
            loader.reset();
            word.setText("");
            description.setText("");
            examples.setText("");
            audioUrl = "";
        }
    }

    /**
     * Used by the LoaderCallback to destroy the current Loader.
     * DO NOT USE IT IN ANY OTHER CASE.
     * @param id the id of the Loader.
     */
    protected void destroyLoader(int id) {
        LoaderManager.getInstance(this).destroyLoader(id);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(STATE_LANGUAGE,
                this.getSharedPreferences("matchPref", Context.MODE_PRIVATE)
                        .getString("language_key", Locale.getDefault().getLanguage()));
        super.onSaveInstanceState(savedInstanceState);
    }
}
