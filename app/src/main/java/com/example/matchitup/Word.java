package com.example.matchitup;

import java.util.List;

public class Word {
    private String word, def, audio;
    private List<String> examples;

    public Word(String word, String def, String audio) {
        this.word = word;
        this.def = def;
        this.audio = audio;
    }

    public Word(String word, String def, String audio, List<String> examples) {
        this.word = word;
        this.def = def;
        this.audio = audio;
        this.examples = examples;
    }

    public String getWord() {
        return word;
    }

    public String getDef() {
        return def;
    }

    public String getAudio() {
        return audio;
    }

    public List<String> getExamples() {
        return examples;
    }
}
