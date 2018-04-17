package com.haybankz.medmanager.ui;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.haybankz.medmanager.R;
import com.haybankz.medmanager.data.user.UserContract;
import com.haybankz.medmanager.model.User;
import com.haybankz.medmanager.util.Constant;
import com.haybankz.medmanager.util.FileUtils;
import com.haybankz.medmanager.util.PreferenceUtils;
import com.haybankz.medmanager.util.UserDbUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class LoginActivity extends AppCompatActivity {

    private String TAG = getClass().getName();

    private final int RC_SIGN_IN = 112;

    private EditText mNameEditText;
    private EditText mPasswordEditText;

    private SignInButton mSignUpWithGoogleBtn;

    private Button mLoginBtn;

    private TextView signUpTextView;

    private Context mContext = this;


    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mNameEditText = findViewById(R.id.et_login_name);
        mPasswordEditText = findViewById(R.id.et_login_password);
        signUpTextView = findViewById(R.id.tv_sign_up);

        mSignUpWithGoogleBtn = findViewById(R.id.btn_login_with_google);

        mLoginBtn = findViewById(R.id.btn_login);


        GoogleSignInOptions mGoogleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions);

        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (mContext, SignUpActivity.class);
                startActivity(intent);

            }
        });


        mSignUpWithGoogleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: button clicked" );
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userNameEmail = mNameEditText.getText().toString().trim();
                String password = mPasswordEditText.getText().toString().trim();

                User user = UserDbUtils.login(mContext, userNameEmail, password);

                if(user != null){
                    PreferenceUtils.setLoggedInUser(mContext, user.getId());
                    openMedicationActivity(user);
                    finish();
                }
            }
        });

//        signUp();
    }

//    public GoogleApiClient buildClient(){
//
//
//
//        return new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi()
//                .addScope(new Scope(Scopes.PROFILE))
//                .build();
//    }

    void openMedicationActivity(GoogleSignInAccount account){
        Intent loggedInIntent = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.KEY_ACCT_DISPLAY_NAME, account.getDisplayName());
        bundle.putString(Constant.KEY_ACCT_EMAIL, account.getEmail());
        bundle.putString(Constant.KEY_ACCT_PHOTO_URL, "" + account.getPhotoUrl());
        bundle.putString(Constant.KEY_ACCT_GIVEN_NAME, account.getGivenName());
        bundle.putString(Constant.KEY_ACCT_FAMILY_NAME, account.getFamilyName());
        bundle.putString(Constant.KEY_ACCT_ID, account.getId());

        loggedInIntent.putExtras(bundle);
        startActivity(loggedInIntent);


    }

    void openMedicationActivity(User user){
        Intent loggedInIntent = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.KEY_ACCT_DISPLAY_NAME, user.getDisplayName());
        bundle.putString(Constant.KEY_ACCT_EMAIL, user.getEmail());
        bundle.putString(Constant.KEY_ACCT_PHOTO_URL, "" + user.getPhotoUrl());
        bundle.putString(Constant.KEY_ACCT_GIVEN_NAME, user.getGivenName());
        bundle.putString(Constant.KEY_ACCT_FAMILY_NAME, user.getFamilyName());
        bundle.putString(Constant.KEY_ACCT_PROFILE_ID, user.getProfileId());
        bundle.putInt(Constant.KEY_ACCT_ID, user.getId());


        loggedInIntent.putExtras(bundle);

        startActivity(loggedInIntent);


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "onStart: ......" );

        long userId = PreferenceUtils.getLoggedInUser(mContext);
        if(userId > 0){
            User user = UserDbUtils.getUserById(mContext, userId);
            openMedicationActivity(user);
        }else {

            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
            if (account != null) {
//            Toast.makeText(this, account.toString() +"....already logged in", Toast.LENGTH_SHORT).show();
                openMedicationActivity(account);

            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(@NonNull Task<GoogleSignInAccount> completedTask){

        completedTask.addOnCompleteListener(new OnCompleteListener<GoogleSignInAccount>() {
            @Override
            public void onComplete(@NonNull Task<GoogleSignInAccount> task) {

                try{
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    Log.e(TAG, "handleSignInResult: Account: "+account.toString() );

//                    Toast.makeText(LoginActivity.this,"handleSignInResult: Account: "
//                            + account.getDisplayName() , Toast.LENGTH_SHORT).show();

                    User user = UserDbUtils.loginUser(mContext, account.getEmail());

                    if(user != null){

                        PreferenceUtils.setLoggedInUser(mContext, user.getId());
                        Intent intent = new Intent(mContext, MainActivity.class);
                        intent.putExtra(Constant.KEY_ACCT_ID, user.getId());

                        startActivity(intent);
                        finish();


                    }else {
                        int id = signUpGmail(account);
                        if (id > 0) {

                            PreferenceUtils.setLoggedInUser(mContext, id);
                            Intent intent = new Intent(mContext, MainActivity.class);
                            intent.putExtra(Constant.KEY_ACCT_ID, id);
                            intent.putExtra(Constant.KEY_ACCT_PHOTO_URL, ""+account.getPhotoUrl());

                            startActivity(intent);
                            finish();
                        }

                    }

                }catch (ApiException e){
                    Log.e(TAG, "handleSignInResult: failed code = "+ e.getStatusCode());
                }

            }
        });

        completedTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(LoginActivity.this, "Google sign up failed", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onFailure: ....here\n\n\n\n", e );
            }
        });


    }

    private int signUpGmail(GoogleSignInAccount account){

//        Picasso.with(mContext)
//                .load(account.getPhotoUrl())
//                .into(getTarget("" + account.getPhotoUrl()));

        Toast.makeText(mContext, "PhotoUrl == "+mPhotoUrl, Toast.LENGTH_SHORT).show();

        ContentValues values = new ContentValues();

        values.put(UserContract.UserEntry.COLUMN_USER_DISPLAY_NAME, account.getDisplayName());
        values.put(UserContract.UserEntry.COLUMN_USER_GIVEN_NAME, account.getGivenName());
        values.put(UserContract.UserEntry.COLUMN_USER_FAMILY_NAME, account.getFamilyName());
        values.put(UserContract.UserEntry.COLUMN_USER_PASSWORD, "");
        values.put(UserContract.UserEntry.COLUMN_USER_EMAIL, account.getEmail());
        values.put(UserContract.UserEntry.COLUMN_USER_PROFILE_ID, account.getId());
        values.put(UserContract.UserEntry.COLUMN_USER_PHOTO_URL, "" + account.getPhotoUrl());

        return UserDbUtils.signUp(mContext, values);
    }

    String mPhotoUrl;



}
