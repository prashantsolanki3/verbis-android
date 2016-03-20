package com.blackshift.verbis.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import static com.blackshift.verbis.App.*;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.prashantsolanki3.utiloid.Utiloid;

public class LoginActivity extends AppCompatActivity implements OnConnectionFailedListener {

    @Bind(R.id.app_bar) AppBarLayout abl;
    @Bind(R.id.signingrp) LinearLayout signingrp;
    @Bind(R.id.signupgrp) LinearLayout signupgrp;
    @Bind(R.id.link_signin) TextView link_signin;
    @Bind(R.id.link_signup) TextView link_signup;
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.name) EditText name;
    GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "LogInActivity";

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);

    }

    @Bind(R.id.email) EditText email;
    @Bind(R.id.password) EditText password;
    @Bind(R.id.signemail) EditText signemail;
    @Bind(R.id.signpassword) EditText signpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CoordinatorLayout.LayoutParams cl =new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
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
               mGoogleApiClient= new GoogleApiClient.Builder(this)
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

    @OnClick(R.id.link_signin) void signuptosignin()
    {
        signingrp.setVisibility(View.VISIBLE);
        signupgrp.setVisibility(View.GONE);
    }
    @OnClick(R.id.link_signup) void signintosignup()
    {
        signingrp.setVisibility(View.GONE);
        signupgrp.setVisibility(View.VISIBLE);
    }
    @OnClick(R.id.sign_in_button) void googlesignin(){
        signIn();

    }
    private void signIn(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess()+"status:"+result.getStatus());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            String res= "Signed in as"+acct.getDisplayName();
            Snackbar.make(findViewById(R.id.signingrp),res,Snackbar.LENGTH_LONG);
            startMainActivity();

        } else {
            Snackbar.make(findViewById(R.id.signingrp),"Unauthenticated User",Snackbar.LENGTH_LONG);
            startMainActivity();

        }
    }


    @OnClick(R.id.signupbtn) void signup(){
        /*final ProgressDialog progressDialog=new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating your Account...");
        progressDialog.show();*/

        Firebase ref = getApp().getFirebase();
        String fireemail=email.getText().toString();
        String firepassword=password.getText().toString();
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
    @OnClick(R.id.signinbtn) void signin() {
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
      void startMainActivity(){
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

