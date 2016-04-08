package com.blackshift.verbis.rest.model.recyclerviewmodels;

import java.util.ArrayList;

/**
 * Created by Devika on 02-04-2016.
 */
public class MeaningAndExample {

    String meaning;
    ArrayList<String> example;

    public MeaningAndExample() {
    }

    public MeaningAndExample(String meaning) {
        this.meaning = meaning;
    }

    public MeaningAndExample(String meaning, ArrayList<String> example) {
        this.meaning = meaning;
        this.example = example;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public ArrayList<String> getExample() {
        return example;
    }

    public void setExample(ArrayList<String> example) {
        this.example = example;
    }
}
