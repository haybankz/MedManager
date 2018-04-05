package com.haybankz.medmanager.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.haybankz.medmanager.R;

public class LoginActivity extends AppCompatActivity {

    private String TAG = getClass().getName();

    private final int RC_SIGN_IN = 112;

    private EditText mNameEditText;
    private EditText mPasswordEditText;

    private Button mLoginBtn;



    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mNameEditText = findViewById(R.id.et_login_name);
        mPasswordEditText = findViewById(R.id.et_login_password);

        mLoginBtn = findViewById(R.id.btn_login);


        GoogleSignInOptions mGoogleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions);



//        SignInButton signInButton = findViewById(R.id.btn_signin);
//        signInButton.setSize(SignInButton.SIZE_STANDARD);


        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: button clicked" );
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
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
        bundle.putString("display_name", account.getDisplayName());
        bundle.putString("email", account.getEmail());
        bundle.putString("photo_url", "" + account.getPhotoUrl());
        bundle.putString("given_name", account.getGivenName());
        bundle.putString("family_name", account.getFamilyName());
        bundle.putString("id", account.getId());

        loggedInIntent.putExtras(bundle);
//
        startActivity(loggedInIntent);

//        Intent logg = new Intent(this, MainActivity.class);
//        startActivity(logg);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "onStart: ......" );



        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null){
//            Toast.makeText(this, account.toString() +"....already logged in", Toast.LENGTH_SHORT).show();
            openMedicationActivity(account);

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

                    openMedicationActivity(account);

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


}
