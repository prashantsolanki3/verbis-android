package com.blackshift.verbis.utils.manager

import android.content.Context
import android.util.Log
import com.blackshift.verbis.App
import com.blackshift.verbis.rest.model.wordlist.Word
import com.blackshift.verbis.rest.model.wordlist.WordList
import com.blackshift.verbis.utils.DateUtils
import com.blackshift.verbis.utils.annotations.PrivacyLevel
import com.blackshift.verbis.utils.keys.FirebaseKeys
import com.blackshift.verbis.utils.listeners.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.prashantsolanki.secureprefmanager.encryptor.AESEncryptor
import java.sql.Timestamp
import java.util.*

/**
 * Package com.blackshift.verbis.utils
 *
 *
 * Created by Prashant on 3/19/2016.
 *
 *
 * Email: solankisrp2@gmail.com
 * Github: @prashantsolanki3
 */
class WordListManager(internal var context: Context) {

    internal var encryptor: AESEncryptor


    init {
        this.encryptor = AESEncryptor(context)
    }

    /**
     * Creates a new wordlist of the logged in user.

     * @param title Title of the Wordlist should be specified.
     * *
     * @param listener called after the task if executed successfully or otherwise.
     * *
     */
    fun createWordList(title: String, listener: WordListListener) {

        //Go to Wordlist then go to user and then push to create a new object
        val firebase = listFirebaseRef.push()
        if (title.isEmpty())
            throw RuntimeException("Wordlist Title Cannot be Empty.")

        val wordList = WordList(firebase.key, title)
        val timeCreated = Timestamp(DateUtils.getDateTimeUTC().millis).time
        wordList.createdAt = timeCreated
        wordList.modifiedAt = timeCreated
        wordList.owner = FirebaseAuth.getInstance().currentUser!!.uid
        firebase.setValue(wordList, FirebaseAuth.getInstance().currentUser!!.uid) { firebaseError, firebase ->
            //TODO:See to this
            Log.e("Firebase ToString", firebase.toString())
            handleListener(firebaseError, firebase.toString(), listener)
        }
    }

    /**
     * Renames the given wordlist.

     * @param title Title of the Wordlist should be specified.
     * *
     * @param list Wordlist to be renamed
     * *
     * @param listener called after the task if executed successfully or otherwise.
     * *
     */
    fun renameWordList(title: String, list: WordList, listener: WordListListener) {
        renameWordList(title, list.id, listener)

    }

    /**
     * Renames the given wordlist.

     * @param title Title of the Wordlist should be specified.
     * *
     * @param id  Id of the Wordlist to be renamed
     * *
     * @param listener called after the task if executed successfully or otherwise.
     * *
     */
    fun renameWordList(title: String, id: String, listener: WordListListener) {
        val map = HashMap<String, Any>()
        map.put("title", title)
        updateWordlist(id, map, listener)
    }

    /**
     * Deletes the given wordlist.

     * @param list Wordlist to be deleted
     * *
     * @param listener called after the task if executed successfully or otherwise.
     * *
     */
    fun deleteWordList(list: WordList, listener: WordListListener) {
        deleteWordList(list.id, listener)
    }

    /**
     * Deletes the given wordlist.

     * @param id  Id of the Wordlist to be deleted
     * *
     * @param listener called after the task if executed successfully or otherwise.
     * *
     */
    fun deleteWordList(id: String, listener: WordListListener) {
        val firebase = listFirebaseRef
        deleteAllWords(id, null)
        firebase.child(id).removeValue { firebaseError, firebase -> handleListener(firebaseError, firebase.toString(), listener) }
    }

    fun getAllWordLists(listener: WordListArrayListener) {
        getWordListByPrivacy(-1, listener)
    }

    fun getWordLists(@PrivacyLevel privacy: Int, listener: WordListArrayListener) {
        getWordListByPrivacy(privacy, listener)
    }

    /**
     * @param privacy if==-1 returns all else returns the requested privacy level wordlist.
     * *
     */
    private fun getWordListByPrivacy(privacy: Int, listener: WordListArrayListener) {

        val firebase = listFirebaseRef
        val base: Query
        val rawUid = FirebaseAuth.getInstance().currentUser!!.uid
        //TODO: Fix Privacy
        if (privacy == -1)
            base = firebase.orderByChild("owner").equalTo(rawUid)
        else if (privacy == PrivacyLevel.PUBLIC)
            base = firebase.orderByChild("")
        else if( privacy == PrivacyLevel.PRIVATE)
            base = firebase.orderByChild("owner").equalTo(rawUid)
        else {
            listener.onFailure(DatabaseError.fromException(Throwable("Illegal Privacy Level")))
            return
        }

        base.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val wordLists = ArrayList<WordList>()
                try {
                    for (snapshot in dataSnapshot.children) {
                        val wordList = snapshot.getValue(WordList::class.java)
                        wordLists.add(wordList)
                    }

                    //Starred comes first, then comes modified
                    Collections.sort(wordLists, Comparator<com.blackshift.verbis.rest.model.wordlist.WordList> { lhs, rhs ->
                        //-1 -> move to starting
                        if (lhs.isStarred)
                            return@Comparator -1
                        // move towards end
                        if (rhs.isStarred)
                            return@Comparator 1

                        if (lhs.modifiedAt > rhs.modifiedAt)
                            -1
                        else
                            1
                    })
                    listener.onSuccess(wordLists)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFailure(DatabaseError.fromException(e))
                }

            }

            override fun onCancelled(firebaseError: DatabaseError) {
                firebaseError.toException().printStackTrace()
                listener.onFailure(firebaseError)
            }
        })

    }

    fun getWordsFromWordList(wordList: WordList, listener: WordArrayListener) {
        getWordsFromWordList(wordList.id, listener)
    }

    fun getWordsFromWordList(wordListId: String, listener: WordArrayListener) {
        contentFirebaseRef.child(wordListId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val wordLists = ArrayList<Word>()
                try {
                    for (snapshot in dataSnapshot.children) {
                        val word = snapshot.getValue(Word::class.java)
                        wordLists.add(word)
                    }
                    listener.onSuccess(wordLists)
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onFailure(DatabaseError.fromException(e))
                }

            }

            override fun onCancelled(firebaseError: DatabaseError) {
                listener.onFailure(firebaseError)
            }
        })
    }

    fun containsWord(word: String, wordListId: String, listener: ExistenceListener?) {
        val firebase = contentFirebaseRef
        try {
            firebase.child(wordListId).orderByChild("headword").equalTo(word).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.hasChildren()) {
                        val word1 = dataSnapshot.children.iterator().next().getValue(Word::class.java)
                        if (word1 != null && word1.headword != null)
                            listener!!.onSuccess(word1.headword.equals(word, ignoreCase = true))
                    } else
                    //Does not exist or empty wordlist
                        listener!!.onSuccess(false)
                }

                override fun onCancelled(firebaseError: DatabaseError) {
                    listener!!.onFailure(firebaseError)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
            listener?.onFailure(DatabaseError.fromException(e))
        }

    }

    fun addWord(word: Word, wordList: WordList, listener: WordListener) {
        addWord(word, wordList.id, listener)
    }

    fun addWord(word: Word, wordListId: String, listener: WordListener) {
        addWord(word.headword, word.url, wordListId, listener)
    }

    fun addWord(headWord: String, url: String, wordListId: String, listener: WordListener?) {
        val firebase = contentFirebaseRef
        try {
            val id = urlToWordId(url)
            val word = Word(id, headWord, url)
            word.addedOn = DateUtils.getTimestampUTC()

            firebase.child(wordListId).child(id!!).setValue(word, DatabaseReference.CompletionListener { firebaseError, firebase -> handleListener(firebaseError, firebase.toString(), listener) })
        } catch (e: Exception) {
            e.printStackTrace()
            listener?.onFailure(DatabaseError.fromException(e))
        }

    }

    fun deleteWord(word: Word, wordList: WordList, listener: WordListener) {
        deleteWord(word.id, wordList.id, listener)
    }

    fun deleteWord(id: String, wordListId: String, listener: WordListener) {
        val firebase = contentFirebaseRef
        firebase.child(wordListId).child(id).removeValue { firebaseError, firebase -> handleListener(firebaseError, firebase.toString(), listener) }
    }

    fun deleteAllWords(wordlistId: String, listener: WordListener?) {
        val firebase = contentFirebaseRef
        firebase.child(wordlistId).removeValue { firebaseError, firebase -> handleListener(firebaseError, firebase.toString(), listener) }
    }

    fun updatePrivacy(wordListId: String, @PrivacyLevel privacyLevel: Int, listener: WordListListener) {
        val map = HashMap<String, Any>()
        map.put("privacy", privacyLevel)
        updateWordlist(wordListId, map, listener)
    }

    fun updateTitle(wordListId: String, newName: String, listener: WordListListener) {
        val map = HashMap<String, Any>()
        map.put("title", newName)
        updateWordlist(wordListId, map, listener)
    }

    fun starWordlist(wordList: String, listener: WordListListener) {
        val firebase = listFirebaseRef.child(wordList)
        val map = HashMap<String, Any>()
        map.put("starred", true)
        firebase.updateChildren(map) { firebaseError, firebase -> handleListener(firebaseError, firebase.toString(), listener) }
    }

    fun unstarWordlist(wordList: String, listener: WordListListener) {
        val firebase = listFirebaseRef.child(wordList)
        val map = HashMap<String, Any>()
        map.put("starred", false)
        firebase.updateChildren(map) { firebaseError, firebase -> handleListener(firebaseError, firebase.toString(), listener) }
    }

    fun updateWordlist(id: String, map: MutableMap<String, Any>, listener: WordListListener) {
        if (map != null)
            updateModifiedAtWordlist(id, map, listener)
    }

    /** Update modifiedAt field of the given wordlist.

     * @param id Wordlist Id
     * *
     * @param map additional fields to be updated or null if only modifiedAt needs to be updated.
     * *
     */
    fun updateModifiedAtWordlist(id: String, map: MutableMap<String, Any>?, listener: WordListListener) {
        var map = map
        val firebase = listFirebaseRef.child(id)
        if (map == null)
            map = HashMap<String, Any>()

        map.put("modifiedAt", DateUtils.getTimestampUTC())
        firebase.updateChildren(map) { firebaseError, firebase -> handleListener(firebaseError, firebase.toString(), listener) }
    }

    /**
     * Handles firebase onComplete.

     * @param firebaseError
     * *
     * @param listener
     * *
     */
    private fun handleListener(firebaseError: DatabaseError?, referenceString: String, listener: WordListListener?) {
        if (firebaseError != null) {
            //Some Error has occurred.
            listener?.onFailure(firebaseError)
        } else {
            //List saved successfully.
            listener?.onSuccess(referenceString)
        }
    }

    /**
     * Handles firebase onComplete.

     * @param firebaseError
     * *
     * @param listener
     * *
     */
    private fun handleListener(firebaseError: DatabaseError?, referenceString: String, listener: WordListener?) {
        if (firebaseError != null) {
            //Some Error has occurred.
            listener?.onFailure(firebaseError)
        } else {
            //List saved successfully.
            listener?.onSuccess(referenceString)
        }
    }

    /**
     * @return DatabaseReference instance pointing to the users wordlist.
     * *
     */
    //.child(firebase.getAuth().getUid())
    val listFirebaseRef: DatabaseReference
        get() {
            val firebase = App.getApp().firebaseDatabase
            return firebase.child(FirebaseKeys.WORD_LIST).child(FirebaseKeys.WORD_LIST_LISTS)
        }

    /**
     * @return DatabaseReference instance pointing to the user's wordlist content.
     * *
     */
    //.child(firebase.getAuth().getUid())
    val contentFirebaseRef: DatabaseReference
        get() {
            val firebase = App.getApp().firebaseDatabase
            return firebase.child(FirebaseKeys.WORD_LIST).child(FirebaseKeys.WORD_LIST_CONTENT)
        }

    fun urlToWordId(url: String): String? {
        try {
            var wordId = encryptor.encrypt(url)
            wordId = wordId.replace("/", "__")
            return wordId
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

    }

    fun wordIdToUrl(wordId: String): String? {
        try {
            var url = encryptor.decrypt(wordId)
            url = url.replace("__", "/")
            return url
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

    }

    //TODO: Like wordlists. in Future Releases.

}

