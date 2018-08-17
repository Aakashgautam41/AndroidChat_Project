 package com.example.aakash.chatapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

 public class LoginActivity extends AppCompatActivity {

     //Ref to fields
     private EditText myEmail;
     private EditText myPassword;

     //Firebase Reference
     private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Getting values on create
        myEmail = (EditText) findViewById(R.id.login_email);
        myPassword = (EditText) findViewById(R.id.login_password);

        //Getting firebase instance
        mAuth = FirebaseAuth.getInstance();
    }

     //Calling login method
     public void onLogin(View view){
        loginWithFirebase();
     }

     //Login with firebase
     private void loginWithFirebase(){
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
             myPassword.setError(getString(R.string.invalid_password));
             focusView = myPassword;
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
             Toast.makeText(LoginActivity.this, "Logging you in ...", Toast.LENGTH_SHORT).show();

             //Calling signIn method
             mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                 @Override
                 public void onComplete(@NonNull Task<AuthResult> task) {
                     Log.i("TAG","signInUserWithEmail:onComplete: " + task.isSuccessful());
                     if (!task.isSuccessful()){
                         showErrorBox("Login failed !!");
                     }
                     else {
                         Toast.makeText(LoginActivity.this,"Login Successful",Toast.LENGTH_SHORT).show();

                         Intent intent = new Intent(LoginActivity.this,MainChatActivity.class);
                         finish();
                         startActivity(intent);
                     }
                 }
             });
         }


     }

     //Validation for email
     private boolean checkEmail(String email){
         return  email.contains("@");
     }

     //Validation for password
     private boolean checkPassword(String password){
         return password.length()>=6 ;
     }

    //Moving to register activity
    public void onSignup(View view){
        Intent myIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        finish();
        startActivity(myIntent);
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
