package com.example.matchitup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class Dictionary {
    private String word, sound;
    private List<String> definitions, synonymous, antonyms, examples;

    public Dictionary() {

    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public List<String> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(List<String> definitions) {
        this.definitions = definitions;
    }

    public List<String> getSynonymous() {
        return synonymous;
    }

    public void setSynonymous(List<String> synonymous) {
        this.synonymous = synonymous;
    }

    public List<String> getAntonyms() {
        return antonyms;
    }

    public void setAntonyms(List<String> antonyms) {
        this.antonyms = antonyms;
    }

    public List<String> getExamples() {
        return examples;
    }

    public void setExamples(List<String> examples) {
        this.examples = examples;
    }

    //Actualmente solo sirve para para pasar el JSON de randomWords a una lista de palabras
    //(mover a una clase utils? mover a DictionaryService? who knows)
    public static List<String> fromJsonResponse(String info) throws JSONException, MalformedURLException {
        List<String> wordsList = null;
        JSONObject data = new JSONObject(info);

        if ((int) data.get("totalItems") > 0) {
            wordsList = new ArrayList<>();
            JSONArray itemsArray = (JSONArray) data.get("items");

            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject wordJson = itemsArray.getJSONObject(i);
                if (wordJson.has("word")) {
                    String word = wordJson.getString("word");

                    wordsList.add(word);
                }
            }
        }

        return wordsList;
    }
}
