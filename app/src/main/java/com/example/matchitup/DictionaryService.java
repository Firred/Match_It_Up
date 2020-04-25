package com.example.matchitup;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class DictionaryService {
    private static final String DEBUG_TAG = DictionaryService.class.getSimpleName();
    private static final String BASE_URL = "http://api.wordnik.com/v4/";

    private static String convertInputToString(InputStream stream) throws IOException {
        BufferedReader reader;
        reader = new BufferedReader(new InputStreamReader(stream,"UTF-8"));
        String line = "";
        String result = "";
        while ((line = reader.readLine()) != null){
            result += line;
        }
        stream.close();
        return result;
    }


    private static String downloadUrl(String myurl) throws IOException {
        InputStream responseBody = null;
        HttpURLConnection conn = null;
        try {
            URL url = new URL(myurl);
            conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            int response = conn.getResponseCode();
            if(response == 200){
                Log.d(DEBUG_TAG, "The code response is: " + response);
                responseBody = conn.getInputStream();
                String contentAsString = convertInputToString(responseBody);
                Log.d(DEBUG_TAG, "The string is: " + contentAsString);
                return contentAsString;
            } else{
                return null;
            }
            // Close the InputStream and connection
        } finally {
            conn.disconnect();
            if (responseBody != null) {
                responseBody.close();
            }
        }
    }

    /**
     * Devuelve un número {limit} de palabras aleatorias
     * @param limit maximo de palabras
     * @return lista con palabras
     */
    public static List<String> getRandomWords(int limit) {
        String url = BASE_URL+"words.json/randomWords";

        Uri builtURI = Uri.parse(url).buildUpon()
                .appendQueryParameter("limit", String.valueOf(limit))
                .build();

        List<String> words = null;
        try {
            String info = downloadUrl(builtURI.toString());
            words = Dictionary.fromJsonResponse(info);

        } catch (IOException | JSONException e) {
            System.out.println("Ha habido un error en DictionaryService: " + e.getMessage());
        }
        Log.d(DEBUG_TAG, "The JSON is: " + words);
        return words;
    }

    public static int getFrequency(String word) {
        String url = BASE_URL+"word.json/" + word + "/frequency?api_key=";

        int frequency = 0;

        try {
            String info = downloadUrl(url);

            JSONObject data = new JSONObject(info);

            if ((int) data.get("totalItems") > 0) {
                JSONArray itemsArray = (JSONArray) data.get("items");

                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject json = itemsArray.getJSONObject(i);
                    if (json.has("totalCount")) {
                        frequency = json.getInt("totalCount");
                    }
                }
            }
        } catch (IOException | JSONException e) {
            System.out.println("Ha habido un error en DictionaryService: " + e.getMessage());
        }
        Log.d(DEBUG_TAG, "The JSON is: " + frequency);

        return frequency;
    }

    public static String getAudio(String word) {
        String url = BASE_URL+"word.json/" + word + "/audio?api_key=";

        String audio = "";

        try {
            String info = downloadUrl(url);
            boolean found = false;

            JSONObject data = new JSONObject(info);

            if ((int) data.get("totalItems") > 0) {
                JSONArray itemsArray = (JSONArray) data.get("items");

                for (int i = 0; i < itemsArray.length() && !found; i++) {
                    JSONObject freqJson = itemsArray.getJSONObject(i);
                    if (freqJson.has("fileUrl")) {
                        audio = freqJson.getString("fileUrl");
                        found = true;
                    }
                }
            }
        } catch (IOException | JSONException e) {
            System.out.println("Ha habido un error en DictionaryService: " + e.getMessage());
        }
        Log.d(DEBUG_TAG, "The JSON is: " + audio);

        return audio;
    }

    public static List<String> getExamples(String word, int limit) {
        String url = BASE_URL+"word.json/" + word + "/examples?includeDuplicates=false&useCanonical=false&limit="
                + limit + "&api_key=";

        List<String> examples = null;
        String example;

        try {
            String info = downloadUrl(url);

            JSONObject data = new JSONObject(info);

            if ((int) data.get("totalItems") > 0) {
                JSONArray itemsArray = (JSONArray) data.get("items");

                for (int i = 0; i < itemsArray.length(); i++) {
                    JSONObject json = itemsArray.getJSONObject(i);
                    if (json.has("examples")) {
                         JSONArray examplesJson = json.getJSONArray("examples");

                        for (int j = 0; j < examplesJson.length(); j++) {
                            if (json.has("text")) {
                                example = json.getString("text");

                                examples.add(example);
                            }
                        }
                    }
                }
            }
        } catch (IOException | JSONException e) {
            System.out.println("Ha habido un error en DictionaryService: " + e.getMessage());
        }
        Log.d(DEBUG_TAG, "The JSON is: " + examples);

        return examples;
    }
}
