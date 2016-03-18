package com.blackshift.verbis.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import static com.blackshift.verbis.App.*;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blackshift.verbis.R;
import com.blackshift.verbis.ui.activity.HomePageActivity;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.prashantsolanki3.utiloid.Utiloid;

public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.app_bar) AppBarLayout abl;
    @Bind(R.id.signingrp) LinearLayout signingrp;
    @Bind(R.id.signupgrp) LinearLayout signupgrp;
    @Bind(R.id.link_signin) TextView link_signin;
    @Bind(R.id.link_signup) TextView link_signup;
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.name) EditText name;
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

