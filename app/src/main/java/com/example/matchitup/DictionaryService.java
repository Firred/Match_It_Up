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
import java.util.ArrayList;
import java.util.List;

public class DictionaryService {
    private static final String DEBUG_TAG = DictionaryService.class.getSimpleName();
    private static final String BASE_URL = "http://api.wordnik.com/v4/";

    /**
     * Private method responsible for converting an InputStream object into a String
     * @param stream InputStream
     * @return String
     * @throws IOException
     */
    private static String convertInputToString(InputStream stream) throws IOException {
        BufferedReader reader;
        reader = new BufferedReader(new InputStreamReader(stream,"UTF-8"));
        String line;
        String result = "";
        while ((line = reader.readLine()) != null){
            result += line;
        }
        stream.close();
        return result;
    }

    /**
     * This method is responsible for parsing a string and removing all the HTML or XML tags contained
     * on it
     * @param input String to be parsed
     * @return String parsed
     */
    private static String parseString(String input){
        return input.replaceAll("\\<.*?\\>", "");
    }

    /**
     * This private function receive an URL in string format, and perform the corresponding HTTP request
     * to get the information from the API
     * @param myurl String representing the URI
     * @return String representing the JSON
     * @throws IOException
     */
    private static String downloadUrl(String myurl) throws IOException {
        InputStream responseBody = null;
        HttpURLConnection conn = null;
        try {
            URL url = new URL(myurl);
            conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(DEBUG_TAG, "The request is: " + myurl);
            Log.d(DEBUG_TAG, "The code response is: " + response);
            if(response == 200){
                responseBody = conn.getInputStream();
                String contentAsString = convertInputToString(responseBody);
                return contentAsString;
            } else{
                return null; //Too Many Requests o Not Found
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
     * Obtains a given number of random words
     * @param limit max number of words
     * @return list of words
     */
    public static List<String> getRandomWords(int limit, int lowFrecuency, int highFrecuency) {
        Uri builtURI = Uri.parse(BASE_URL+"words.json/randomWords").buildUpon()
                .appendQueryParameter("limit", String.valueOf(limit))
                .appendQueryParameter("minCorpusCount", String.valueOf(lowFrecuency))
                .appendQueryParameter("maxCorpusCount", String.valueOf(highFrecuency))
                .appendQueryParameter("hasDictionaryDef", "true")
                .appendQueryParameter("api_key", BuildConfig.API_KEY)
                .build();

        List<String> words = new ArrayList<>();
        try {
            String info = downloadUrl(builtURI.toString());

            if (info != null) {
                JSONArray data = new JSONArray(info);

                for (int i = 0; i < data.length(); i++) {
                    JSONObject wordJson = data.getJSONObject(i);
                    if (wordJson.has("word")) {
                        String word = wordJson.getString("word");
                        words.add(word);
                    }
                }
            } else{
                return null; // Ha habido un problema con la petición
            }
        } catch (IOException | JSONException e) {
            System.out.println("Ha habido un error en DictionaryService: " + e.getMessage());
        }
        return words;
    }

    /**
     * Obtains the URL of an audio with the pronunciation of a given word
     * @param word the word to search
     * @return String with the URL of the audio
     */
    public static String getAudio(String word) {
        Uri builtURI = Uri.parse(BASE_URL+"word.json/").buildUpon()
                .appendPath(word)
                .appendEncodedPath("audio")
                .appendQueryParameter("api_key", BuildConfig.API_KEY)
                .build();

        String audio = "";

        try {
            String info = downloadUrl(builtURI.toString());
            boolean found = false;
            if(info != null) {
                JSONArray data = new JSONArray(info);

                for (int i = 0; i < data.length() && !found; i++) {
                    JSONObject audioJson = data.getJSONObject(i);

                    if (audioJson.has("fileUrl")) {
                        audio = audioJson.getString("fileUrl");
                        found = true;
                    }
                }
            } else{
                return null; // Ha habido un problema con la petición
            }
        } catch (IOException | JSONException e) {
            System.out.println("Ha habido un error en DictionaryService: " + e.getMessage());
        }
        return audio;
    }

    /**
     * Obtains the examples of use of a given word
     * @param word the word to search
     * @param limit the maximum number of examples
     * @return list with examples
     */
    public static List<String> getExamples(String word, int limit) {
        Uri builtURI = Uri.parse(BASE_URL+"word.json/").buildUpon()
                .appendPath(word)
                .appendEncodedPath("examples")
                .appendQueryParameter("includeDuplicates", "false")
                .appendQueryParameter("limit", String.valueOf(limit))
                .appendQueryParameter("api_key", BuildConfig.API_KEY)
                .build();

        List<String> examples = new ArrayList<>();
        String example;
        try {
            String info = downloadUrl(builtURI.toString());

            if(info != null) {
                JSONObject data = new JSONObject(info);
                if (data.has("examples")) {
                    JSONArray itemsArray = (JSONArray) data.get("examples");
                    for (int i = 0; i < itemsArray.length(); i++) {
                        JSONObject json = itemsArray.getJSONObject(i);

                        if (json.has("text")) {
                            example = json.getString("text");
                            example = parseString(example);

                            examples.add(example);
                        }
                    }
                }
            } else {
                return null; // Ha habido un problema con la petición
            }
        } catch (IOException | JSONException e) {
            System.out.println("Ha habido un error en DictionaryService: " + e.getMessage());
        }
        return examples;
    }

    /**
     * Obtains the definition for a word
     * @param word the word to search
     * @return definition of the word
     */
    public static String getDefinition(String word) {
        Uri builtURI = Uri.parse(BASE_URL+"word.json/").buildUpon()
                .appendPath(word)
                .appendEncodedPath("definitions")
                .appendQueryParameter("limit", String.valueOf(20))
                .appendQueryParameter("includeRelated", "false")
                .appendQueryParameter("api_key", BuildConfig.API_KEY)
                .build();

        String definition = "";
        try {
            String info = downloadUrl(builtURI.toString());

            if(info != null) {
                boolean found = false;
                JSONArray data = new JSONArray(info);
                for (int i = 0; i < data.length() && !found; i++) {
                    JSONObject audioJson = data.getJSONObject(i);

                    if (audioJson.has("text")) {
                        String textDefinition = audioJson.getString("text");
                        definition = parseString(textDefinition);
                        found = true;
                    }
                }
            } else {
                return null; // Ha habido un problema con la petición
            }
        } catch (IOException | JSONException e) {
            System.out.println("Ha habido un error en DictionaryService: " + e.getMessage());
        }

        return definition;
    }


}
