package com.blackshift.verbis.utils.manager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.blackshift.verbis.App;
import com.blackshift.verbis.rest.model.WordList;
import com.blackshift.verbis.utils.FirebaseKeys;
import com.blackshift.verbis.utils.annotations.PrivacyLevel;
import com.blackshift.verbis.utils.listeners.WordListListener;
import com.blackshift.verbis.utils.listeners.WordListener;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;

/**
 * Package com.blackshift.verbis.utils
 * <p/>
 * Created by Prashant on 3/19/2016.
 * <p/>
 * Email: solankisrp2@gmail.com
 * Github: @prashantsolanki3
 */
public class WordListManager {

    Context context;

    public WordListManager(Context context) {
        this.context = context;
    }


    /**
     * Creates a new wordlist of the logged in user.
     *
     * @param title Title of the Wordlist should be specified.
     * @param listener called after the task if executed successfully or otherwise.
     * */
    public void createWordList(@NonNull String title, final WordListListener listener){

        //Go to Wordlist then go to user and then push to create a new object
        Firebase firebase = getBaseFirebaseRef().push();

        final WordList wordList = new WordList(firebase.getKey(),title);

        firebase.setValue(wordList, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                handleListener(firebaseError, wordList, listener);
            }
        });
    }

    /**
     * Renames the given wordlist.
     *
     * @param title Title of the Wordlist should be specified.
     * @param list Wordlist to be renamed
     * @param listener called after the task if executed successfully or otherwise.
     * */
    public void renameWordList(@NonNull String title,@NonNull final WordList list, final WordListListener listener){
        renameWordList(title, list.getId(), listener);
    }

    /**
     * Renames the given wordlist.
     *
     * @param title Title of the Wordlist should be specified.
     * @param id  Id of the Wordlist to be renamed
     * @param listener called after the task if executed successfully or otherwise.
     * */
    public void renameWordList(@NonNull final String title,@NonNull final String id, final WordListListener listener){

        Firebase firebase = App.getApp().getFirebase();
        firebase = firebase.child(FirebaseKeys.WORD_LIST).child(firebase.getAuth().getUid()).child(id);

        Map<String,Object> map = new HashMap<>();
        map.put("title",title);

        firebase.updateChildren(map, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                handleListener(firebaseError, new WordList(id, title), listener);
            }
        });
    }


    /**
     * Deletes the given wordlist.
     *
     * @param list Wordlist to be deleted
     * @param listener called after the task if executed successfully or otherwise.
     * */
    public void deleteWordList(@NonNull WordList list, WordListListener listener){
        deleteWordList(list.getId(), listener);
    }

    /**
     * Deletes the given wordlist.
     *
     * @param id  Id of the Wordlist to be deleted
     * @param listener called after the task if executed successfully or otherwise.
     * */
    public void deleteWordList(@NonNull String id, final WordListListener listener){
        Firebase firebase = getBaseFirebaseRef();
        firebase.child(id).removeValue(new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                handleListener(firebaseError, null, listener);
            }
        });
    }

    public void getWordLists(@PrivacyLevel int privacy){
        Firebase firebase = getBaseFirebaseRef();
    }

    public void addWord(@NonNull Word word,WordList wordList, WordListener listener){
        addWord(word, wordList.getId(), listener);
    }

    public void addWord(@NonNull Word word,String wordListId, WordListener listener){
        addWord(word.getHeadword(),word.getUrl(),wordListId,listener);
    }

    public void addWord(String word,String url,String wordListId, WordListener listener){
        Firebase firebase = getBaseFirebaseRef();
        String key = firebase.child(wordListId).push().getKey();
    }

    public void deleteWord(@NonNull Word word,WordList wordList ,WordListener listener){
        deleteWord(word.getId(),wordList.getId(),listener);
    }

    public void deleteWord(String id, String wordListId, final WordListener listener){
        Firebase firebase = getBaseFirebaseRef();
        firebase.child(wordListId).child(id).removeValue(new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                    handleListener(firebaseError,null,listener);
            }
        });
    }

    /**
     * Handles firebase onComplete.
     *
     * @param firebaseError
     * @param listener
     * */
    private void handleListener(FirebaseError firebaseError,@Nullable WordList wordList, WordListListener listener){
        if(firebaseError!=null){
            //Some Error has occurred.
            if (listener!=null)
                listener.onFailure(firebaseError);
        }else{
            //List saved successfully.
            if(listener!=null)
                listener.onSuccess(wordList);
        }
    }

    /**
     * Handles firebase onComplete.
     *
     * @param firebaseError
     * @param listener
     * */
    private void handleListener(FirebaseError firebaseError,@Nullable Word word, WordListener listener){
        if(firebaseError!=null){
            //Some Error has occurred.
            if (listener!=null)
                listener.onFailure(firebaseError);
        }else{
            //List saved successfully.
            if(listener!=null)
                listener.onSuccess(word);
        }
    }

    /**
     * @return Firebase instance pointing to the users wordlist.
     * */
    private Firebase getBaseFirebaseRef(){
        Firebase firebase = App.getApp().getFirebase();
        return firebase.child(FirebaseKeys.WORD_LIST).child(firebase.getAuth().getUid());
    }

    class Word{
        private String id;
        private String headword;
        private String url;

        public Word(String headword, String url) {
            this.headword = headword;
            this.url = url;
        }

        public Word(String headword) {
            this.headword = headword;
        }

        public Word(String id, String headword, String url) {
            this.id = id;
            this.headword = headword;
            this.url = url;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getHeadword() {
            return headword;
        }

        public void setHeadword(String headword) {
            this.headword = headword;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

}

