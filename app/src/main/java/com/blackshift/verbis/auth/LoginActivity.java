package com.blackshift.verbis.auth;

import android.accounts.Account;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import static com.blackshift.verbis.App.*;

import android.provider.SyncStateContract;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blackshift.verbis.App;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

import com.blackshift.verbis.R;
import com.blackshift.verbis.ui.activity.HomePageActivity;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.prashantsolanki3.utiloid.Utiloid;

public class LoginActivity extends AppCompatActivity implements OnConnectionFailedListener {

    private static final int RC_GOOGLE_LOGIN = 1;
    @Bind(R.id.app_bar)
    AppBarLayout abl;
    @Bind(R.id.signingrp)
    LinearLayout signingrp;
    @Bind(R.id.signupgrp)
    LinearLayout signupgrp;
    @Bind(R.id.link_signin)
    TextView link_signin;
    @Bind(R.id.link_signup)
    TextView link_signup;
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.name)
    EditText name;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "LogInActivity";

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult.getErrorMessage());

    }

    @Bind(R.id.email)
    EditText email;
    @Bind(R.id.password)
    EditText password;
    @Bind(R.id.signemail)
    EditText signemail;
    @Bind(R.id.signpassword)
    EditText signpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CoordinatorLayout.LayoutParams cl = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        abl.setLayoutParams(cl);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle("Verbis");
        //Google LOGIN
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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @OnClick(R.id.link_signin)
    void signuptosignin() {
        signingrp.setVisibility(View.VISIBLE);
        signupgrp.setVisibility(View.GONE);
    }

    @OnClick(R.id.link_signup)
    void signintosignup() {
        signingrp.setVisibility(View.GONE);
        signupgrp.setVisibility(View.VISIBLE);
    }

   
    @OnClick(R.id.signupbtn)
    void signup() {
        final ProgressDialog progressDialog=new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating your Account...");
        progressDialog.show();

        Firebase ref = getApp().getFirebase();
        String fireemail = email.getText().toString();
        String firepassword = password.getText().toString();
        ref.createUser(fireemail, firepassword, new Firebase.ValueResultHandler<Map<String, Object>>() {


            @Override
            public void onSuccess(Map<String, Object> result) {
                Toast.makeText(getApplicationContext(), "Successfully created user account", Toast.LENGTH_SHORT);
                signuptosignin();
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                Toast.makeText(getApplicationContext(), "Successfully created user account", Toast.LENGTH_SHORT);

            }
        });

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
                GoogleSignInAccount acct = result.getSignInAccount();
                String emailAddress = acct.getEmail();
                getGoogleOAuthToken(emailAddress);
            }
        }
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
                    //Ntwork or server error
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
                Log.d("FPK", "OnPostExecute, token:" + token);
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
                String res = "User ID: " + authData.getUid() + ", Provider: " + authData.getProvider();
                Log.e("Result:", res);
                Snackbar.make(findViewById(R.id.signingrp), res, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                checkSession();
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                Snackbar.make(findViewById(R.id.signingrp), "Some Error occured", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();   // there was an error
            }

        });
    }

   /* class myAuthResultHandler implements Firebase.AuthResultHandler {
        @Override
        public void onAuthenticated(AuthData authData) {
            String res = "User ID: " + authData.getUid() + ", Provider: " + authData.getProvider();
            Snackbar.make(findViewById(R.id.signingrp), res, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            checkSession();
        }

        @Override
        public void onAuthenticationError(FirebaseError firebaseError) {
            Snackbar.make(findViewById(R.id.signingrp), "Some Error occured", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

        }*/

        //google stuff end

        @OnClick(R.id.signinbtn)
        void signin() {
            Firebase ref = getApp().getFirebase();
            String fireemail = signemail.getText().toString();
            String firepassword = signpassword.getText().toString();
            ref.authWithPassword(fireemail, firepassword, new Firebase.AuthResultHandler() {
                @Override
                public void onAuthenticated(AuthData authData) {
                    String res = "User ID: " + authData.getUid() + ", Provider: " + authData.getProvider();
                    Snackbar.make(findViewById(R.id.signingrp), res, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    checkSession();
                }

                @Override
                public void onAuthenticationError(FirebaseError firebaseError) {
                    Snackbar.make(findViewById(R.id.signingrp), "Some Error occured", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();   // there was an error
                }

            });
        }

        void startMainActivity() {
            this.finish();
            this.startActivity(new Intent(this, HomePageActivity.class));
        }

        void checkSession() {
            Firebase refs = getApp().getFirebase();
            refs.addAuthStateListener(new Firebase.AuthStateListener() {
                @Override
                public void onAuthStateChanged(AuthData authData) {
                    if (authData != null) {
                        startMainActivity();
                    } else {
                        Snackbar.make(findViewById(R.id.signingrp), "Looks like something went wrong. Try logging in again", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        signin();
                    }
                }
            });
        }

    }






