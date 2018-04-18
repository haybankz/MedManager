package com.haybankz.medmanager.ui;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.haybankz.medmanager.R;
import com.haybankz.medmanager.adapter.PicassoCircleTransformation;
import com.haybankz.medmanager.data.user.UserContract;
import com.haybankz.medmanager.model.User;
import com.haybankz.medmanager.util.Constant;
import com.haybankz.medmanager.util.FileUtils;
import com.haybankz.medmanager.util.ImageUtils;
import com.haybankz.medmanager.util.PreferenceUtils;
import com.haybankz.medmanager.util.UserDbUtils;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;

public class UpdateUserActivity extends AppCompatActivity {

    EditText mEmailEditText;
    EditText mPasswordEditText;
    EditText mFirstNameEditText;
    EditText mLastNameEditText;
    ImageView mProfilePicImageView;
    Button mSelectImageButton;
    Button mSaveButton;

    private int mUserId = 0;
    private String mProfilePicUrl = "";
    private final int ACTIVITY_CHOOSE_FILE = 1002;
    private String TAG = getClass().getName();

    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_update_user);

        mEmailEditText = findViewById(R.id.et_sign_up_email);
        mPasswordEditText = findViewById(R.id.et_sign_up_password);
        mFirstNameEditText = findViewById(R.id.et_sign_up_first_name);
        mLastNameEditText = findViewById(R.id.et_sign_up_last_name);
        mProfilePicImageView = findViewById(R.id.img_sign_up_profile_pics);
        mSelectImageButton = findViewById(R.id.btn_sign_up_select_image);
        mSaveButton = findViewById(R.id.btn_sign_up_save);

        mEmailEditText.setEnabled(false);

        mUserId = (int) PreferenceUtils.getLoggedInUser(mContext);

        User user = UserDbUtils.getUserById(mContext, mUserId);

        if(user != null){
            mEmailEditText.setText(user.getEmail());
            mPasswordEditText.setText(user.getPassword());
            mFirstNameEditText.setText(user.getFamilyName());
            mLastNameEditText.setText(user.getGivenName());
            mProfilePicUrl = user.getPhotoUrl();

            if (user.getPhotoUrl() != null && !user.getPhotoUrl().equals("")) {
                Picasso.with(this)
                        .load(user.getPhotoUrl())
                        .transform(new PicassoCircleTransformation())
                        .fit()
                        .placeholder(R.drawable.ic_person_white)
                        .error(R.drawable.ic_person_dark)
//                                    .networkPolicy(NetworkPolicy.OFFLINE)
                        .noFade()
                        .into(mProfilePicImageView);
            }else{
                mProfilePicImageView.setImageResource(R.drawable.ic_person_dark);
            }

        }


        mSelectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseFile;
                chooseFile = new Intent(Intent.ACTION_GET_CONTENT);

                chooseFile.setType("image/jpeg | image/png");
                chooseFile.addCategory(Intent.CATEGORY_OPENABLE);

                startActivityForResult(chooseFile, ACTIVITY_CHOOSE_FILE);
            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailEditText.getText().toString().trim();
                String password = mPasswordEditText.getText().toString().trim();
                String firstName = mFirstNameEditText.getText().toString().trim();
                String lastName = mLastNameEditText.getText().toString().trim();
                String profilePicUrl = "";

                if(!mProfilePicUrl.equals("")){
                    File savedProfilePicsFile = FileUtils.saveImage(mContext, ImageUtils.getBitmapFromPath(mContext,mProfilePicUrl));
                    profilePicUrl = "file://" + savedProfilePicsFile.getAbsolutePath();
                }


                ContentValues values = new ContentValues();
                values.put(UserContract.UserEntry.COLUMN_USER_PROFILE_ID, "");
                values.put(UserContract.UserEntry.COLUMN_USER_EMAIL, email);
                values.put(UserContract.UserEntry.COLUMN_USER_FAMILY_NAME, firstName);
                values.put(UserContract.UserEntry.COLUMN_USER_GIVEN_NAME, lastName);
                values.put(UserContract.UserEntry.COLUMN_USER_DISPLAY_NAME, firstName +" "+ lastName);
                values.put(UserContract.UserEntry.COLUMN_USER_PHOTO_URL, profilePicUrl);
                values.put(UserContract.UserEntry.COLUMN_USER_PASSWORD, password);

                int i = UserDbUtils.updateUser(mContext, mUserId, values);

                if(i > 0){
//                    Intent intent = new Intent(mContext, MainActivity.class);
//                    intent.putExtra(Constant.KEY_ACCT_ID, i);
//                    PreferenceUtils.setLoggedInUser(mContext, i);
//                    startActivity(intent);
//                    Toast.makeText(mContext, "Dear Awesome " +firstName +", Welcome to Med_Manager", Toast.LENGTH_SHORT).show();
                    finish();
                }



            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode != RESULT_OK) return;


        String filePath;
        if(requestCode == ACTIVITY_CHOOSE_FILE){

            Uri uri = data.getData();

            if(uri != null) {


                filePath = FileUtils.getPath(this, uri);

                Log.e(TAG, "onActivityResult: FilePath == " + filePath);

                if(FileUtils.isImageFile(filePath)) {
                    mProfilePicUrl = filePath;
                    Picasso.with(this)
                            .load(filePath)
                            .networkPolicy(NetworkPolicy.OFFLINE)
                            .transform(new PicassoCircleTransformation())
                            .fit()
                            .noFade()
                            .into(mProfilePicImageView);

                }else{
                    Toast.makeText(this, "Selected file format is not supported.\nSelect a jpg or png file.", Toast.LENGTH_SHORT).show();
                }
            }


        }
    }
}
