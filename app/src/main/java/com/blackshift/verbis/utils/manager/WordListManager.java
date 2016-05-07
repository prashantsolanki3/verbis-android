package com.blackshift.verbis.utils.manager;

import android.content.Context;
import android.support.annotation.NonNull;

import com.blackshift.verbis.App;
import com.blackshift.verbis.rest.model.wordlist.Word;
import com.blackshift.verbis.rest.model.wordlist.WordList;
import com.blackshift.verbis.utils.DateUtils;
import com.blackshift.verbis.utils.FirebaseKeys;
import com.blackshift.verbis.utils.annotations.PrivacyLevel;
import com.blackshift.verbis.utils.listeners.WordArrayListener;
import com.blackshift.verbis.utils.listeners.WordListArrayListener;
import com.blackshift.verbis.utils.listeners.WordListListener;
import com.blackshift.verbis.utils.listeners.WordListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.prashantsolanki.secureprefmanager.encryptor.AESEncryptor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
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
    AESEncryptor encryptor;


    public WordListManager(Context context) {
        this.context = context;
        this.encryptor = new AESEncryptor(context);
    }

    /**
     * Creates a new wordlist of the logged in user.
     *
     * @param title Title of the Wordlist should be specified.
     * @param listener called after the task if executed successfully or otherwise.
     * */
    public void createWordList(@NonNull String title, final WordListListener listener){

        //Go to Wordlist then go to user and then push to create a new object
        Firebase firebase = getListFirebaseRef().push();
        if(title.isEmpty())
            throw new RuntimeException("Wordlist Title Cannot be Empty.");

        final WordList wordList = new WordList(firebase.getKey(), title);
        long timeCreated = new Timestamp(DateUtils.getDateTimeUTC().getMillis()).getTime();
        wordList.setCreatedAt(timeCreated);
        wordList.setModifiedAt(timeCreated);
        wordList.setOwner(App.getApp().getFirebase().getAuth().getUid());
        firebase.setValue(wordList,App.getApp().getFirebase().getAuth().getUid() ,new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                handleListener(firebaseError, firebase.getPath().toString(), listener);
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
        Map<String,Object> map = new HashMap<>();
        map.put("title", title);
        updateWordlist(id, map, listener);
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
        Firebase firebase = getListFirebaseRef();
        deleteAllWords(id,null);
        firebase.child(id).removeValue(new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                handleListener(firebaseError, firebase.getPath().toString(), listener);
            }
        });
    }

    public void getAllWordLists(WordListArrayListener listener){
        getWordListByPrivacy(-1, listener);
    }

    public void getWordLists(@PrivacyLevel int privacy,WordListArrayListener listener){
        getWordListByPrivacy(privacy, listener);
    }

    /**
     * @param privacy if==-1 returns all else returns the requested privacy level wordlist.
     * */
    private void getWordListByPrivacy(int privacy, final WordListArrayListener listener){
        
        final Firebase firebase = getListFirebaseRef();
        Query base;
        String rawUid = App.getApp().getFirebase().getAuth().getUid();
        //TODO: Fix Privacy
        if(privacy!=-1)
            base = firebase.orderByChild("owner").equalTo(rawUid);
        else
            base = firebase.orderByChild("owner").equalTo(rawUid);

        base.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<WordList> wordLists = new ArrayList<>();
                try {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        WordList wordList = snapshot.getValue(WordList.class);
                        wordLists.add(wordList);
                    }

                    //Starred comes first, then comes modified
                    Collections.sort(wordLists, new Comparator<WordList>() {
                        @Override
                        public int compare(WordList lhs, WordList rhs) {
                            //-1 -> move to starting
                            if (lhs.isStarred())
                                return -1;
                            // move towards end
                            if(rhs.isStarred())
                                return 1;

                            if(lhs.getModifiedAt()>rhs.getModifiedAt())
                                return -1;
                            else
                                return 1;
                        }
                    });
                    listener.onSuccess(wordLists);
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onFailure(FirebaseError.fromException(e));
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                firebaseError.toException().printStackTrace();
                listener.onFailure(firebaseError);
            }
        });

    }

    public void getWordsFromWordList(WordList wordList, WordArrayListener listener){
        getWordsFromWordList(wordList.getId(), listener);
    }

    public void getWordsFromWordList(String wordListId,final WordArrayListener listener){
        getContentFirebaseRef().child(wordListId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Word> wordLists = new ArrayList<>();
                try {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Word word = snapshot.getValue(Word.class);
                        wordLists.add(word);
                    }
                    listener.onSuccess(wordLists);
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onFailure(FirebaseError.fromException(e));
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                listener.onFailure(firebaseError);
            }
        });
    }

    public void addWord(@NonNull Word word,WordList wordList, WordListener listener){
        addWord(word, wordList.getId(), listener);
    }

    public void addWord(@NonNull Word word,String wordListId, WordListener listener){
        addWord(word.getHeadword(), word.getUrl(), wordListId, listener);
    }

    public void addWord(String headWord, String url, String wordListId, final WordListener listener){
        Firebase firebase = getContentFirebaseRef();
        try{
            String id = urlToWordId(url);
            final Word word = new Word(id, headWord, url);
            word.setAddedOn(DateUtils.getTimestampUTC());

            firebase.child(wordListId).child(id).setValue(word, new Firebase.CompletionListener() {
                @Override
                public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                    handleListener(firebaseError, firebase.getPath().toString(), listener);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            if(listener!=null)
                listener.onFailure(FirebaseError.fromException(e));
        }
    }

    public void deleteWord(@NonNull Word word,WordList wordList ,WordListener listener){
        deleteWord(word.getId(), wordList.getId(), listener);
    }

    public void deleteWord(String id, String wordListId, final WordListener listener){
        Firebase firebase = getContentFirebaseRef();
        firebase.child(wordListId).child(id).removeValue(new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                handleListener(firebaseError, firebase.getPath().toString(), listener);
            }
        });
    }

    public void deleteAllWords(String wordlistId,final WordListener listener){
        Firebase firebase = getContentFirebaseRef();
        firebase.child(wordlistId).removeValue(new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                handleListener(firebaseError, firebase.getPath().toString(),listener);
            }
        });
    }

    public void updatePrivacy(String wordListId,@PrivacyLevel int privacyLevel, WordListListener listener){
        Map<String,Object> map = new HashMap<>();
        map.put("privacy",privacyLevel);
        updateWordlist(wordListId, map, listener);
    }

    public void updateTitle(String wordListId,String newName, WordListListener listener){
        Map<String,Object> map = new HashMap<>();
        map.put("title",newName);
        updateWordlist(wordListId, map, listener);
    }

    public void starWordlist(String wordList, final WordListListener listener){
        Firebase firebase = getListFirebaseRef().child(wordList);
        Map<String,Object> map =new HashMap<>();
        map.put("starred",true);
        firebase.updateChildren(map, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                handleListener(firebaseError,firebase.getPath().toString(), listener);
            }
        });
    }

    public void unstarWordlist(String wordList, final WordListListener listener){
        Firebase firebase = getListFirebaseRef().child(wordList);
        Map<String,Object> map =new HashMap<>();
        map.put("starred",false);
        firebase.updateChildren(map, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                handleListener(firebaseError,firebase.getPath().toString(), listener);
            }
        });
    }

    public void updateWordlist(final String id,@NonNull Map<String,Object> map, final WordListListener listener){
        if(map!=null)
            updateModifiedAtWordlist(id, map, listener);
    }

    /** Update modifiedAt field of the given wordlist.
     *
     * @param id Wordlist Id
     * @param map additional fields to be updated or null if only modifiedAt needs to be updated.
     * */
    public void updateModifiedAtWordlist(final String id,Map<String,Object> map, final WordListListener listener){
        Firebase firebase = getListFirebaseRef().child(id);
        if(map==null)
            map = new HashMap<>();

        map.put("modifiedAt", DateUtils.getTimestampUTC());
        firebase.updateChildren(map, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                handleListener(firebaseError, firebase.getPath().toString(), listener);
            }
        });
    }

    /**
     * Handles firebase onComplete.
     *
     * @param firebaseError
     * @param listener
     * */
    private void handleListener(FirebaseError firebaseError, String referenceString, WordListListener listener){
        if(firebaseError!=null){
            //Some Error has occurred.
            if (listener!=null)
                listener.onFailure(firebaseError);
        }else{
            //List saved successfully.
            if(listener!=null)
                listener.onSuccess(referenceString);
        }
    }

    /**
     * Handles firebase onComplete.
     *
     * @param firebaseError
     * @param listener
     * */
    private void handleListener(FirebaseError firebaseError, String referenceString, WordListener listener){
        if(firebaseError!=null){
            //Some Error has occurred.
            if (listener!=null)
                listener.onFailure(firebaseError);
        }else{
            //List saved successfully.
            if(listener!=null)
                listener.onSuccess(referenceString);
        }
    }

    /**
     * @return Firebase instance pointing to the users wordlist.
     * */
    public Firebase getListFirebaseRef(){
        Firebase firebase = App.getApp().getFirebase();
        return firebase.child(FirebaseKeys.WORD_LIST)
                //.child(firebase.getAuth().getUid())
                .child(FirebaseKeys.WORD_LIST_LISTS);
    }

    /**
     * @return Firebase instance pointing to the user's wordlist content.
     * */
    public Firebase getContentFirebaseRef(){
        Firebase firebase = App.getApp().getFirebase();
        return firebase.child(FirebaseKeys.WORD_LIST)
                //.child(firebase.getAuth().getUid())
                .child(FirebaseKeys.WORD_LIST_CONTENT);
    }

    public String urlToWordId(String url){
        try {
            String wordId = encryptor.encrypt(url);
            wordId = wordId.replace("/","__");
            return wordId;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String wordIdToUrl(String wordId){
        try {
            String url = encryptor.decrypt(wordId);
            url = url.replace("__","/");
            return url;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //TODO: Like wordlists. in Future Releases.

}

