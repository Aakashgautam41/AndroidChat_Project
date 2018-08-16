package com.example.aakash.chatapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

    //Validation for email
    private boolean checkEmail(String email){
        return  email.contains("@");
    }

    //Validation for password
    private boolean checkPassword(String password){
       String confirmPassword = myConfirmPassword.getText().toString() ;
       return confirmPassword.equals(password) && password.length()>6 ;

    }

    //Signup user at firebase
    private void createUser(){
        String email = myEmail.getText().toString();
        String password = myPassword.getText().toString();

        //Call method from firebase
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.i("TAG","User creation situation"+task.isSuccessful());
                if (!task.isSuccessful()){
                    showErrorBox("Registration failed !!");
                }
                else {
                    saveUserName();
                    Toast.makeText(RegisterActivity.this,"Registrtion Successful",Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
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
                .setTitle("Aladin motherfucker")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok,null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
