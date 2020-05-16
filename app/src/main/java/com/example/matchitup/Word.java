package com.example.matchitup;

import androidx.annotation.NonNull;

import java.util.List;

public class Word {
    private String word, def, audio;
    private List<String> examples;

    /**
     * Word Constructor
     * @param word Word
     * @param def Definition
     */
    public Word(String word, String def) {
        this.word = word;
        this.def = def;
    }


    /**
     * Word Constructor
     * @param word Word
     * @param def Definition
     * @param audio Audio
     * @param examples Examples of use
     */
    public Word(String word, String def, String audio, List<String> examples) {
        this.word = word;
        this.def = def;
        this.audio = audio;
        this.examples = examples;
    }

    /**
     * Get the word
     * @return String representing the word
     */
    public String getWord() {
        return word;
    }

    /**
     * Get the definition
     * @return String representing the definition
     */
    public String getDef() {
        return def;
    }

    /**
     * Get the audio
     * @return String representing the audio
     */
    public String getAudio() {
        return audio;
    }

    /**
     * Get examples of use
     * @return List of String
     */
    public List<String> getExamples() {
        return examples;
    }

    @NonNull
    @Override
    public String toString() {
        return word + ": " + def;
    }
}
