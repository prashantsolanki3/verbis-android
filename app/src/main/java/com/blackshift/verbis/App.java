package com.blackshift.verbis;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;

import com.blackshift.verbis.auth.LoginActivity;
import com.blackshift.verbis.rest.model.wordapimodels.WordsApiResult;
import com.blackshift.verbis.rest.model.wordapimodels.WordsApiResultDeserializer;
import com.blackshift.verbis.rest.service.DictionaryService;
import com.blackshift.verbis.rest.service.VerbisService;
import com.blackshift.verbis.utils.manager.WordOfTheDayManager;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.MaterialModule;
import com.prashantsolanki.secureprefmanager.encryptor.AESEncryptor;
import com.squareup.okhttp.OkHttpClient;

import io.fabric.sdk.android.Fabric;
import io.github.prashantsolanki3.shoot.Shoot;
import io.github.prashantsolanki3.utiloid.Utiloid;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

import static com.prashantsolanki.secureprefmanager.SecurePrefManagerInit.Configuration;
import static com.prashantsolanki.secureprefmanager.SecurePrefManagerInit.Initializer;

/**
 * Package com.blackshift.verbis
 * <p>
 * Created by Prashant on 3/16/2016.
 * <p>
 * Email: solankisrp2@gmail.com
 * Github: @prashantsolanki3
 */
public class App extends Application {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "	rPxTRBmwxi3OOOjXOPoEDRf1p ";
    private static final String TWITTER_SECRET = "lD1UoXrxJIXsTULZ53SIszf7ftmfazCNKWAhV0ZEiTgX3gxeR4 ";

    final static public String FIREBASE_BASE_URL="https://verbis.firebaseio.com";
    final static public String DICTIONARY_API_ENDPOINT = "http://api.pearson.com/v2/dictionaries/";
    final static public String WORDS_API_ENDPOINT = "https://wordsapiv1.p.mashape.com/words/";
    final static public String VERBIS_ENDPOINT = "https://verbis-backend.herokuapp.com/";

    private Tracker mTracker;
    static DictionaryService dictionaryService = null;
    static VerbisService verbisService = null;
    static App app;
    private Firebase firebase;
    private AuthData authData;

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app =this;
//        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this/*, new Twitter(authConfig)*/, new Crashlytics(), new Answers());
        Firebase.setAndroidContext(this);
        Firebase.getDefaultConfig().setPersistenceEnabled(true);
        firebase = new Firebase(FIREBASE_BASE_URL);
        firebase.keepSynced(true);
        firebase.addAuthStateListener(new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                if(authData == null){
                    startLoginActivity();
                }
            }
        });
        Iconify.with(new MaterialModule());
        Shoot.with(this);
        Utiloid.init(this);
        //Secure Preference Manager Init
        try {
            Configuration configuration = new Configuration(this)
                    .setCustomEncryption(new AESEncryptor(this));
            new Initializer(this)
                    .setDefaultConfiguration(configuration)
                    .initialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(WordsApiResult.class,new WordsApiResultDeserializer())
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WORDS_API_ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(new OkHttpClient())
                .build();

        Retrofit retrofitVerbis = new Retrofit.Builder()
                .baseUrl(VERBIS_ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(new OkHttpClient())
                .build();

        if(dictionaryService == null)
            dictionaryService = retrofit.create(DictionaryService.class);

        if(verbisService==null)
            verbisService = retrofitVerbis.create(VerbisService.class);
        //Fetches Word of the day and adds it to Realm
        new WordOfTheDayManager(this).getWordsOfTheWeek();
    }



    public static String getTwitterSecret(){return TWITTER_SECRET;}

    public synchronized static App getApp(){
        return app;
    }

    public static Context getContext(){
        return getApp().getApplicationContext();
    }

    public synchronized Firebase getFirebase(){
        return firebase;
    }

    public synchronized static DictionaryService getDictionaryService(){
        return dictionaryService;
    }

    public synchronized static VerbisService getVerbisService(){
        return verbisService;
    }

    void startLoginActivity(){
        Intent i = new Intent(this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(i);
    }
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }
}