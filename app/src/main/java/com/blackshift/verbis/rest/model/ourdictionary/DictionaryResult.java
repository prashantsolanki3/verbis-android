package com.blackshift.verbis.rest.model.ourdictionary;

/**
 * Created by Devika on 29-03-2016.
 */
public class DictionaryResult {

    String dict_id;
    String partOfSpeech;
    String id;
    String definition; //aka meaning
    String example;

    public DictionaryResult(String dict_id, String partOfSpeech, String id, String definition, String example) {
        this.dict_id = dict_id;
        this.partOfSpeech = partOfSpeech;
        this.id = id;
        this.definition = definition;
        this.example = example;
    }

    public String getDict_id() {
        return dict_id;
    }

    public void setDict_id(String dict_id) {
        this.dict_id = dict_id;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }
}
