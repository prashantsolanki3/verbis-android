package com.blackshift.verbis.utils;

import com.blackshift.verbis.rest.model.recyclerviewmodels.MeaningAndExample;
import com.blackshift.verbis.rest.model.wordapimodels.Result;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Package com.blackshift.verbis.utils
 * <p/>
 * Created by Prashant on 3/16/2016.
 * <p/>
 * Email: solankisrp2@gmail.com
 * Github: @prashantsolanki3
 */
public class Utils {

    public static Set<String> getAllPartOfSpeech(ArrayList<Result> results){
        Set<String> partOfSpeech = new HashSet<>();
        for (Result result : results){
            partOfSpeech.add(result.getPartOfSpeech());
        }
        return partOfSpeech;
    }

    public static ArrayList<ArrayList<MeaningAndExample>> getResultSortedByPartOfSpeech(ArrayList<Result> results, Set<String> partOfSpeech){
        ArrayList<ArrayList<MeaningAndExample>> meaningAndExampleLists = new ArrayList<>();
        for (String string : partOfSpeech){
            ArrayList<MeaningAndExample> meaningAndExamples = new ArrayList<>();
            for (Result result : results){
                if (result.getPartOfSpeech().equals(string)){
                    meaningAndExamples.add(new MeaningAndExample(result.getDefinition(), result.getExamples()));
                }
            }
            meaningAndExampleLists.add(meaningAndExamples);
        }
        return meaningAndExampleLists;
    }

}
