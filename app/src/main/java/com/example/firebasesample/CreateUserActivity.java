package com.example.firebasesample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class CreateUserActivity extends AppCompatActivity {

    private static final String TAG = "CreateUserActivity";

    private EditText mEditUserName;
    private EditText mEditTextEmail;
    private EditText mEditTextPassword;
    private Button mCreateUserButton;

    private String mNewUserName, mNewEmail, mNewPassword;

    //Get instance of firebase authorization
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();;

    //Get instance of shared preferences
    final SharedPreferences prefs = this.getSharedPreferences(
            "com.local.treehouse", Context.MODE_PRIVATE);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        mEditUserName = findViewById(R.id.editNewUserName);
        mEditTextEmail = findViewById(R.id.editTextNewEmailAddress);
        mEditTextPassword = findViewById(R.id.editTextNewPassword);
        mCreateUserButton = findViewById(R.id.create_button);

        //Upon clicking "Create profile": create user -> send to main screen
        mCreateUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNewUserName = mEditUserName.getText().toString();
                mNewEmail = mEditTextEmail.getText().toString();
                mNewPassword = mEditTextPassword.getText().toString();

                //Create new user using firebase prebuilt
                createNewUser(mNewEmail, mNewPassword);

                //Push user nickname to shared prefs for use later
                commitNicknameToSharedPrefs(mNewUserName);
                //Send user to main screen after signing him/her up
                launchMainActivity();

            }
        });

    }

    //Creates new user using firebase prebuilt
    private void createNewUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Log.d(TAG, "createNewUser: Success");
                        } else {
                            Log.w(TAG, "onComplete: ", task.getException());
                        }

                    }
                });
    }

    //Send user to main activity screen after signing up
    private void launchMainActivity() {
        Intent intent = new Intent(this, MainScreenActivity.class);
        startActivity(intent);
    }


    //Deprecated-->
    private void setUserProfile(String userName) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(userName)
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User profile updated.");
                            }
                        }
                    });
        }
    }

    //Save nickname to sharedPreferences
    private void commitNicknameToSharedPrefs(String nickname) {
        prefs.edit().putString("UserNickname", nickname).apply();
    }
}