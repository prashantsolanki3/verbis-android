package com.blackshift.verbis.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blackshift.verbis.R;
import com.blackshift.verbis.ui.activity.HomePageActivity;
import com.blackshift.verbis.utils.FirebaseErrorHandler;
import com.bumptech.glide.Glide;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.LoginEvent;
import com.crashlytics.android.answers.SignUpEvent;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.prashantsolanki3.utiloid.Utiloid;
import retrofit.Response;
import retrofit.Retrofit;

import static com.blackshift.verbis.App.getApp;

public class LoginActivity extends AppCompatActivity implements OnConnectionFailedListener {

    private static final int RC_GOOGLE_LOGIN = 1;
    private static final int RC_TWITTER_LOGIN = 2;
    @Bind(R.id.app_bar)
    AppBarLayout abl;
    @Bind(R.id.signingrp)
    ViewGroup signingrp;
    private TwitterLoginButton loginButton;
    TwitterSession session;
    private LoginButton fbLoginButton;
    CallbackManager callbackManager;
    private String errorMessage;
    CollapsingToolbarLayout collapsingToolbarLayout;
    private ProgressDialog progressDialog;

    @Bind(R.id.toolbar) Toolbar toolbar;

    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "LogInActivity";

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult.getErrorMessage());

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        Glide.with(this)
                .load(R.drawable.icon)
                .centerCrop()
                .into((ImageView)findViewById(R.id.icon));
        Glide.with(this)
                .load(R.drawable.name)
                .centerCrop()
                .into((ImageView)findViewById(R.id.name));

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ViewGroup.LayoutParams toolbarParams = toolbar.getLayoutParams();
        toolbarParams.height = (int) Utiloid.DISPLAY_UTILS.getScreenWidthPixels()+10;
        toolbar.setLayoutParams(toolbarParams);

        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
            getSupportActionBar().setTitle("");

        CoordinatorLayout.LayoutParams cl = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        abl.setLayoutParams(cl);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        if (collapsingToolbarLayout != null) {
            collapsingToolbarLayout.setTitle("");
            collapsingToolbarLayout.setTitleEnabled(false);
            collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
            collapsingToolbarLayout.setContentScrimColor(getResources().getColor(android.R.color.transparent));
            collapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(android.R.color.transparent));
        }


        Firebase firebase = getApp().getFirebase();
        fbLoginButton = (LoginButton) findViewById(R.id.fb_login_button);

        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);


        //Twitter login starts
        assert loginButton != null;
        loginButton.setCallback(new Callback<TwitterSession>() {

            @Override
            public void onResponse(Response<TwitterSession> response, Retrofit retrofit) {

            }

            @Override
            public void onFailure(Throwable t) {

            }

            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                progressDialog = ProgressDialog.show(LoginActivity.this,"","  Logging you in.....");
                session = result.data;
                // TODO: Remove toast and use the TwitterSession's userID
                // with your app's user model
                String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";

                Answers.getInstance().logSignUp(new SignUpEvent().putMethod("twitter").putSuccess(true));
                Firebase ref = getApp().getFirebase();
                Map<String, String> options = new HashMap<String, String>();
                options.put("oauth_token", "719886346496643072-8AyUdNPxbQkIvMuuMfZpfnRaHcWdj1D");
                options.put("oauth_token_secret","bdKD4aIYh7MNRgRP5gTlne7iBr38aYQ2DKGy0mv0hj6bm");
                options.put("user_id",String.valueOf(session.getUserId()));
                ref.authWithOAuthToken("twitter",options, new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        Answers.getInstance().logLogin(new LoginEvent()
                                .putMethod("Twitter")
                                .putSuccess(true));

                        checkSession();
                    }
                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {

                        FirebaseErrorHandler firebaseErrorHandler = new FirebaseErrorHandler(firebaseError);
                        errorMessage = firebaseErrorHandler.checkErrorCode();
                        Snackbar.make(findViewById(R.id.signingrp), errorMessage, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        Answers.getInstance().logLogin(new LoginEvent()
                                .putMethod("Twitter")
                                .putSuccess(false));

                    }
                });
            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });
        //Twitter Login ends

        //Facebook Login Starts
        callbackManager= CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
              AccessToken accessToken = loginResult.getAccessToken();
                Firebase ref = getApp().getFirebase();
                        if (accessToken != null) {
                            ref.authWithOAuthToken("facebook", accessToken.getToken(), new Firebase.AuthResultHandler() {
                                @Override
                                public void onAuthenticated(AuthData authData) {
                                    /*String msg = "Provider:" + authData.getProvider() + "Name" + authData.getProviderData().get("displayName");
                                    Log.e("Facebook:", msg);*/
                                    Answers.getInstance().logLogin(new LoginEvent()
                                            .putMethod("Facebook")
                                            .putSuccess(true));
                                    checkSession();
                                }

                                @Override
                                public void onAuthenticationError(FirebaseError firebaseError) {
                                    FirebaseErrorHandler firebaseErrorHandler = new FirebaseErrorHandler(firebaseError);
                                    errorMessage = firebaseErrorHandler.checkErrorCode();
                                    Snackbar.make(findViewById(R.id.signingrp), errorMessage, Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                    Answers.getInstance().logLogin(new LoginEvent()
                                            .putMethod("Facebook")
                                            .putSuccess(false));

                                }
                            });

                    }


        }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });


        //Facebook Login ends







        firebase.addAuthStateListener(new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                if (authData != null) {
                    startMainActivity();
                } else {
                     final Handler handler =  new Handler();
                     handler.postDelayed(new Runnable() {
                         @Override
                         public void run() {
                             abl.setExpanded(false, true);

                            if(Build.VERSION.SDK_INT<21)
                             new Handler().postDelayed(new Runnable() {
                                 @Override
                                 public void run() {
                                     findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
                                 }
                             },1000);

                         }
                     },1500);
                }
            }
        });


        ///Google LOGIN
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }



    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }




    //Google stuff start

    @OnClick(R.id.sign_in_button)
    public void onGoogleLogin() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(intent, RC_GOOGLE_LOGIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_GOOGLE_LOGIN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                progressDialog = ProgressDialog.show(LoginActivity.this,"","  Logging you in.....");
                GoogleSignInAccount acct = result.getSignInAccount();
                String emailAddress = acct.getEmail();
                Log.d("Login", emailAddress);
                getGoogleOAuthToken(emailAddress);
            }
        }
        else if(requestCode== TwitterAuthConfig.DEFAULT_AUTH_REQUEST_CODE)
            loginButton.onActivityResult(requestCode, resultCode, data);
        else
            callbackManager.onActivityResult(requestCode,resultCode,data);



    }


    private void getGoogleOAuthToken(final String emailAddress) {
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            String errorMessage = null;

            @Override
            protected String doInBackground(Void... params) {
                String token = null;
                try {
                    String scope = "oauth2:profile email";
                    token = GoogleAuthUtil.getToken(LoginActivity.this, emailAddress, scope);
                } catch (IOException transientEx) {
                    //Network or server error
                    errorMessage = "Network Error:" + transientEx.getMessage();
                } catch (UserRecoverableAuthException e) {
                    //We probably need to ask for permissions, so start the intent if there is none pending
                    Intent recover = e.getIntent();
                    startActivityForResult(recover, LoginActivity.RC_GOOGLE_LOGIN);
                } catch (GoogleAuthException authEx) {
                    errorMessage = "Error Authentication with Google" + authEx.getMessage();
                }
                return token;

            }

            @Override
            protected void onPostExecute(String token) {

                if (token != null) {
                    onGoogleLoginWithToken(token);
                } else
                    showLoginError(errorMessage);

            }
        };
        task.execute();
    }
        private void showLoginError(String error) {
            Log.e("Login error:",error );
        }

    private void onGoogleLoginWithToken(String oAuthToken) {
        Firebase firebase = getApp().getFirebase();
        firebase.authWithOAuthToken("google", oAuthToken, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                /*String res = "User ID: " + authData.getUid() + ", Provider: " + authData.getProvider();
                Log.e("Result:", res);*/
                /*Snackbar.make(findViewById(R.id.signingrp), res, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()*/;
                Answers.getInstance().logLogin(new LoginEvent()
                        .putMethod("Google")
                        .putSuccess(true));
                checkSession();
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {

                FirebaseErrorHandler firebaseErrorHandler = new FirebaseErrorHandler(firebaseError);
                errorMessage = firebaseErrorHandler.checkErrorCode();
                Snackbar.make(findViewById(R.id.signingrp), errorMessage, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Answers.getInstance().logLogin(new LoginEvent()
                        .putMethod("Google")
                        .putSuccess(false));
                // there was an error
            }

        });
    }

        //google stuff end


        void startMainActivity() {
            this.finish();
            Intent i = new Intent(this, HomePageActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            this.startActivity(i);
        }

        void checkSession() {
            Firebase refs = getApp().getFirebase();
            refs.addAuthStateListener(new Firebase.AuthStateListener() {
                @Override
                public void onAuthStateChanged(AuthData authData) {
                    if (authData != null) {
                        progressDialog.dismiss();
                        startMainActivity();
                    } else {
                        progressDialog.dismiss();
                        Snackbar.make(findViewById(R.id.signingrp), "Looks like something went wrong. Try logging in again", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();

                    }
                }
            });
        }

    }






