package com.example.aakash.chatapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    public static final String CHAT_PREF = "ChatPref";
    public static final String DISPLAY_NAME = "UserName";

    //Ref to fields
    private EditText myUserName;
    private EditText myEmail;
    private EditText myPassword;
    private EditText myConfirmPassword;

    //Firebase Reference
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //get values on create
        myUserName = (EditText) findViewById(R.id.register_username);
        myEmail = (EditText) findViewById(R.id.register_email);
        myPassword = (EditText) findViewById(R.id.register_password);
        myConfirmPassword = (EditText) findViewById(R.id.register_confirmpassword);


        //get a hold of firebase instance
        mAuth = FirebaseAuth.getInstance();

    }
    public void signUp(View view){
        registerUser();
    }

    //Actual registration method
    private void registerUser(){
        myUserName.setError(null);
        myEmail.setError(null);
        myPassword.setError(null);
        myConfirmPassword.setError(null);

        //Grab values of email and password
        String email = myEmail.getText().toString();
        String password = myPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        //Password validation
        if(TextUtils.isEmpty(password) == true){
            myPassword.setError(getString(R.string.empty_password));
            focusView = myPassword;
            cancel = true;
        }
        else if (checkPassword(password) == false) {
            myConfirmPassword.setError(getString(R.string.conf_password_invalid));
            focusView = myConfirmPassword;
            cancel = true;
        }

        //Email validation
        if (TextUtils.isEmpty(email) == true){
            myEmail.setError(getString(R.string.invalid_email));
            focusView = myEmail;
            cancel = true;
        }
        else if(checkEmail(email) == false){
            myEmail.setError(getString(R.string.invalid_email));
            focusView = myEmail;
            cancel = true;
        }

        if (cancel){
            focusView.requestFocus();
        }
        else {
            createUser();
        }

    }

    //Validation for email
    private boolean checkEmail(String email){
        return  email.contains("@");
    }

    //Validation for password
    private boolean checkPassword(String password){
       String confirmPassword = myConfirmPassword.getText().toString() ;
       return confirmPassword.equals(password) && password.length()>=6 ;

    }

    //Signup user at firebase
    private void createUser(){
        String email = myEmail.getText().toString();
        String password = myPassword.getText().toString();

        Log.i("TAG","email: " + email );
        Log.i("TAG","password: " + password );

        //Call method from firebase
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.i("TAG","createUserWithEmail:onComplete: " + task.isSuccessful());
                if (!task.isSuccessful()){
                    showErrorBox("Registration failed !!");
                }
                else {
                    saveUserName();
                    Toast.makeText(RegisterActivity.this,"Registration Successful",Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                    finish();
                    startActivity(intent);
                }
            }
        });
    }


    //Use shared prefs for username
    private void saveUserName(){
        String userName = myUserName.getText().toString();
        SharedPreferences pref = getSharedPreferences(CHAT_PREF,0);
        pref.edit().putString(DISPLAY_NAME,userName).apply();
    }

    //Error box
    private void showErrorBox(String message){
        new AlertDialog.Builder(this)
                .setTitle("Message")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok,null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
