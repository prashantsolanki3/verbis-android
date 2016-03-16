package com.blackshift.verbis;

import android.app.Application;
import android.content.Context;

import com.blackshift.verbis.rest.service.DictionaryService;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.firebase.client.Firebase;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.MaterialModule;
import com.prashantsolanki.secureprefmanager.encryptor.AESEncryptor;
import com.squareup.okhttp.OkHttpClient;

import io.fabric.sdk.android.Fabric;
import io.github.prashantsolanki3.shoot.Shoot;
import io.github.prashantsolanki3.utiloid.Utiloid;
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

    final static public String FIREBASE_BASE_URL="https://verbis.firebaseio.com";
    final static public String DICTIONARY_API_ENDPOINT = "http://api.pearson.com/v2/dictionaries";


    DictionaryService dictionaryService = null;
    static App app;
    private Firebase firebase;

    @Override
    public void onCreate() {
        super.onCreate();
        app =this;
        Fabric.with(this, new Crashlytics(),new Answers());
        Firebase.setAndroidContext(this);
        firebase = new Firebase(FIREBASE_BASE_URL);
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

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DICTIONARY_API_ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient())
                .build();

        if(dictionaryService ==null)
            dictionaryService = retrofit.create(DictionaryService.class);

    }

    public synchronized static App getApp(){
        return app;
    }

    public static Context getContext(){
        return getApp().getApplicationContext();
    }

    public synchronized Firebase getFirebase(){
        return firebase;
    }

    public synchronized DictionaryService getDictionaryService(){
        return dictionaryService;
    }

}